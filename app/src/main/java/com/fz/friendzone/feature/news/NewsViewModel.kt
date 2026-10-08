package com.fz.friendzone.feature.news

import androidx.lifecycle.ViewModel
import com.fz.friendzone.R
import com.fz.friendzone.core.media.MediaLibrary
import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostAudience
import com.fz.friendzone.core.model.PostLifecycleState
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.FollowRepository
import com.fz.friendzone.data.repository.FriendRepository
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val profileRepository: ProfileRepository,
    private val reactionRepository: ReactionRepository,
    private val commentRepository: CommentRepository,
    private val mediaLibrary: MediaLibrary,
    private val friendRepository: FriendRepository? = null,
    private val followRepository: FollowRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(
        NewsUiState.Loading
    )

    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    val currentProfile: Profile?
        get() = profileRepository.getProfile()

    fun onAction(action: NewsAction) {
        when (action) {
            NewsAction.Load -> loadPosts()

            is NewsAction.CreatePost -> {
                createPost(action.post)
            }

            is NewsAction.UpdatePost -> {
                updatePost(action.post)
            }

            is NewsAction.MovePostToBin -> {
                movePostToBin(action.postId)
            }

            is NewsAction.RestorePost -> {
                restorePost(action.postId)
            }

            is NewsAction.MovePostToAsh -> {
                movePostToAsh(action.postId)
            }

            is NewsAction.SaveReaction -> {
                saveReaction(action.reaction)
            }

            is NewsAction.SaveComment -> {
                saveComment(action.comment)
            }
        }
    }

    private fun createPost(post: Post) {
        val currentUserId = currentProfile?.userId

        if (currentUserId.isNullOrBlank()) {
            return
        }

        if (post.userId != currentUserId) {
            return
        }

        runCatching {
            newsRepository.savePost(post)
        }.onSuccess {
            loadPosts()
        }.onFailure {
            _uiState.value = NewsUiState.Error(
                messageResId = R.string.news_load_error
            )
        }
    }

    private fun updatePost(post: Post) {
        val currentUserId = currentProfile?.userId

        if (currentUserId.isNullOrBlank()) {
            return
        }

        val existingPost = findPost(post.id) ?: return

        if (existingPost.userId != currentUserId) {
            return
        }

        val updatedPost = existingPost.copy(
            caption = post.caption,
            mediaAssetId = post.mediaAssetId,
            mediaUrl = post.mediaUrl,
            mediaType = post.mediaType,
            audience = post.audience,
            lifecycleState = existingPost.lifecycleState,
            deletedAt = existingPost.deletedAt
        )

        runCatching {
            newsRepository.updatePost(updatedPost)
        }.onSuccess {
            loadPosts()
        }.onFailure {
            _uiState.value = NewsUiState.Error(
                messageResId = R.string.news_load_error
            )
        }
    }

    private fun movePostToBin(postId: String) {
        val post = findPost(postId) ?: return

        if (!isCurrentUserOwner(post)) {
            return
        }

        if (post.lifecycleState != PostLifecycleState.ACTIVE) {
            return
        }

        runCatching {
            newsRepository.movePostToBin(postId)
        }.onSuccess {
            loadPosts()
        }.onFailure {
            _uiState.value = NewsUiState.Error(
                messageResId = R.string.news_load_error
            )
        }
    }

    private fun restorePost(postId: String) {
        val post = findPost(postId) ?: return

        if (!isCurrentUserOwner(post)) {
            return
        }

        if (post.lifecycleState != PostLifecycleState.BIN) {
            return
        }

        runCatching {
            newsRepository.restorePost(postId)
        }.onSuccess {
            loadPosts()
        }.onFailure {
            _uiState.value = NewsUiState.Error(
                messageResId = R.string.news_load_error
            )
        }
    }

    private fun movePostToAsh(postId: String) {
        val post = findPost(postId) ?: return

        if (!isCurrentUserOwner(post)) {
            return
        }

        if (post.lifecycleState != PostLifecycleState.ACTIVE) {
            return
        }

        runCatching {
            newsRepository.movePostToAsh(postId)
        }.onSuccess {
            loadPosts()
        }.onFailure {
            _uiState.value = NewsUiState.Error(
                messageResId = R.string.news_load_error
            )
        }
    }

    private fun findPost(postId: String): Post? {
        return (
            newsRepository.getPosts() +
                newsRepository.getBinPosts()
            ).firstOrNull { post ->
                post.id == postId
            }
    }

    private fun isCurrentUserOwner(post: Post): Boolean {
        val currentUserId = currentProfile?.userId

        return !currentUserId.isNullOrBlank() &&
            post.userId == currentUserId
    }

    private fun saveReaction(
        reaction: Reaction
    ) {
        val currentUserId = currentProfile?.userId

        if (currentUserId.isNullOrBlank()) {
            return
        }

        if (reaction.userId != currentUserId) {
            return
        }

        val post = findPost(reaction.postId) ?: return

        val permissions = resolvePostPermissions(post)

        if (!permissions.canLike) {
            return
        }

        runCatching {
            reactionRepository.saveReaction(reaction)
        }.onSuccess {
            loadPosts()
        }
    }

    private fun saveComment(
        comment: Comment
    ) {
        val currentUserId = currentProfile?.userId

        if (currentUserId.isNullOrBlank()) {
            return
        }

        if (comment.userId != currentUserId) {
            return
        }

        if (comment.text.trim().isEmpty()) {
            return
        }

        val post = findPost(comment.postId) ?: return

        val permissions = resolvePostPermissions(post)

        if (!permissions.canComment) {
            return
        }

        runCatching {
            commentRepository.saveComment(comment)
        }.onSuccess {
            loadPosts()
        }
    }

    private fun loadPosts() {
        _uiState.value = NewsUiState.Loading

        runCatching {
            newsRepository.getPosts()
        }.onSuccess { posts ->

            val activePosts = posts.filter { post ->
                post.lifecycleState == PostLifecycleState.ACTIVE
            }

            /*
             * IMPORTANT:
             *
             * Public / Followers / FoF / Friends
             * ----------------------------------
             * The POST ITSELF is visible to every
             * FriendZone account holder.
             *
             * Audience controls interaction scope,
             * especially Comment.
             *
             * Private
             * -------
             * Only the owner can see the post.
             */
            val postUiModels = activePosts.mapNotNull { post ->

                val permissions = resolvePostPermissions(post)

                if (!permissions.canView) {
                    null
                } else {
                    NewsPostUiModel(
                        post = post,
                        profile = profileRepository.getProfile(
                            post.userId
                        ),
                        reactionCount = reactionRepository
                            .getReactions(post.id)
                            .size,
                        comments = commentRepository
                            .getComments(post.id),
                        canView = permissions.canView,
                        canLike = permissions.canLike,
                        canComment = permissions.canComment,
                        canShare = permissions.canShare,
                        canSave = permissions.canSave,
                        canManage = permissions.canManage
                    )
                }
            }

            _uiState.value = NewsUiState.Success(
                posts = postUiModels
            )
        }.onFailure {
            _uiState.value = NewsUiState.Error(
                messageResId = R.string.news_load_error
            )
        }
    }

    private fun resolvePostPermissions(
        post: Post
    ): PostPermissions {

        val currentUserId = currentProfile?.userId

        if (currentUserId.isNullOrBlank()) {
            return PostPermissions(
                canView = false,
                canLike = false,
                canComment = false,
                canShare = false,
                canSave = false,
                canManage = false
            )
        }

        val isOwner = post.userId == currentUserId

        /*
         * PRIVATE
         *
         * Owner:
         * - View       YES
         * - Save       YES
         * - Manage     YES
         * - Like       NO
         * - Comment    NO
         * - Share      NO
         *
         * Non-owner:
         * - Cannot see the post.
         */
        if (post.audience == PostAudience.PRIVATE) {
            return if (isOwner) {
                PostPermissions(
                    canView = true,
                    canLike = false,
                    canComment = false,
                    canShare = false,
                    canSave = true,
                    canManage = true
                )
            } else {
                PostPermissions(
                    canView = false,
                    canLike = false,
                    canComment = false,
                    canShare = false,
                    canSave = false,
                    canManage = false
                )
            }
        }

        val isFriend = isFriend(
            currentUserId = currentUserId,
            otherUserId = post.userId
        )

        val isFriendOfFriend = isFriendOfFriend(
            currentUserId = currentUserId,
            postOwnerId = post.userId
        )

        val isFollowing = isFollowing(
            followerId = currentUserId,
            followingId = post.userId
        )

        /*
         * COMMENT audience is cumulative:
         *
         * PUBLIC
         * -> All app users
         *
         * FOLLOWERS
         * -> Followers OR FoF OR Friends
         *
         * FoF
         * -> FoF OR Friends
         *
         * FRIENDS
         * -> Friends
         */
        val canComment = when (post.audience) {

            PostAudience.PUBLIC -> {
                true
            }

            PostAudience.FOLLOWERS -> {
                isFollowing ||
                    isFriendOfFriend ||
                    isFriend
            }

            PostAudience.FRIENDS_OF_FRIENDS -> {
                isFriendOfFriend ||
                    isFriend
            }

            PostAudience.FRIENDS -> {
                isFriend
            }

            PostAudience.PRIVATE -> {
                false
            }
        }

        /*
         * For every non-private post:
         *
         * View  -> YES
         * Like  -> YES
         * Share -> YES
         * Save  -> YES
         *
         * Save is NOT a Private-only permission.
         * It is available to any viewer who can see
         * the post.
         */
        return PostPermissions(
            canView = true,
            canLike = true,
            canComment = canComment,
            canShare = true,
            canSave = true,
            canManage = isOwner
        )
    }

    private fun isFriend(
        currentUserId: String,
        otherUserId: String
    ): Boolean {
        val repository = friendRepository
            ?: return false

        return runCatching {

            repository.getFriends(currentUserId).any { friend ->
                (
                    friend.userId == currentUserId &&
                        friend.friendUserId == otherUserId
                    ) ||
                    (
                        friend.friendUserId == currentUserId &&
                            friend.userId == otherUserId
                        )
            } || repository.getFriends(otherUserId).any { friend ->
                (
                    friend.userId == otherUserId &&
                        friend.friendUserId == currentUserId
                    ) ||
                    (
                        friend.friendUserId == otherUserId &&
                            friend.userId == currentUserId
                        )
            }

        }.getOrDefault(false)
    }

    private fun isFollowing(
        followerId: String,
        followingId: String
    ): Boolean {
        val repository = followRepository
            ?: return false

        return runCatching {
            repository.getFollowing(followerId).any { follow ->
                follow.followerId == followerId &&
                    follow.followingId == followingId
            }
        }.getOrDefault(false)
    }

    private fun isFriendOfFriend(
        currentUserId: String,
        postOwnerId: String
    ): Boolean {
        val repository = friendRepository
            ?: return false

        if (currentUserId == postOwnerId) {
            return false
        }

        return runCatching {

            val currentUserFriends = repository
                .getFriends(currentUserId)
                .mapNotNull { friend ->
                    when {
                        friend.userId == currentUserId -> {
                            friend.friendUserId
                        }

                        friend.friendUserId == currentUserId -> {
                            friend.userId
                        }

                        else -> null
                    }
                }
                .filter { friendId ->
                    friendId != postOwnerId
                }
                .toSet()

            if (currentUserFriends.isEmpty()) {
                false
            } else {

                val ownerFriends = repository
                    .getFriends(postOwnerId)
                    .mapNotNull { friend ->
                        when {
                            friend.userId == postOwnerId -> {
                                friend.friendUserId
                            }

                            friend.friendUserId == postOwnerId -> {
                                friend.userId
                            }

                            else -> null
                        }
                    }
                    .toSet()

                /*
                 * Exactly one degree:
                 *
                 * Current User
                 *      ↓
                 *   Friend
                 *      ↓
                 * Post Owner
                 *
                 * No recursive / multi-degree traversal.
                 */
                currentUserFriends.any { friendId ->
                    friendId in ownerFriends
                }
            }
        }.getOrDefault(false)
    }

    private data class PostPermissions(
        val canView: Boolean,
        val canLike: Boolean,
        val canComment: Boolean,
        val canShare: Boolean,
        val canSave: Boolean,
        val canManage: Boolean
    )
}
