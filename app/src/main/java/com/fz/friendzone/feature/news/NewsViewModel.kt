package com.fz.friendzone.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fz.friendzone.R
import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.core.model.Friend
import com.fz.friendzone.core.model.Follow
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostAudience
import com.fz.friendzone.core.model.PostLifecycleState
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.FriendRepository
import com.fz.friendzone.data.repository.FollowRepository
import com.fz.friendzone.data.repository.PostRepository
import com.fz.friendzone.feature.media.MediaLibrary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val friendRepository: FriendRepository? = null,
    private val followRepository: FollowRepository? = null,
    private val mediaLibrary: MediaLibrary? = null,
    private val currentUserId: String = ""
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading

            try {
                val posts = postRepository.getPosts()
                    .filter { it.lifecycleState == PostLifecycleState.ACTIVE }

                val visiblePosts = posts.mapNotNull { post ->
                    val permissions = resolvePostPermissions(post)

                    if (!permissions.canView) {
                        null
                    } else {
                        val profile = try {
                            postRepository.getProfile(post.userId)
                        } catch (_: Exception) {
                            null
                        }

                        val comments = try {
                            commentRepository.getComments(post.id)
                        } catch (_: Exception) {
                            emptyList()
                        }

                        NewsPostUiModel(
                            post = post,
                            profile = profile,
                            reactionCount = 0,
                            comments = comments,
                            canView = permissions.canView,
                            canLike = permissions.canLike,
                            canComment = permissions.canComment,
                            canShare = permissions.canShare,
                            canSave = permissions.canSave,
                            canManage = permissions.canManage
                        )
                    }
                }

                _uiState.value = NewsUiState.Success(visiblePosts)
            } catch (_: Exception) {
                _uiState.value = NewsUiState.Error(
                    messageResId = R.string.app_name
                )
            }
        }
    }

    fun saveReaction(postId: String) {
        val currentState = _uiState.value

        if (currentState !is NewsUiState.Success) {
            return
        }

        val postUiModel = currentState.posts.firstOrNull {
            it.post.id == postId
        } ?: return

        if (!postUiModel.canLike) {
            return
        }

        /*
         * Reaction persistence will be connected to the dedicated
         * reaction repository/feature without changing the permission
         * foundation established here.
         */
    }

    fun saveComment(
        postId: String,
        text: String
    ) {
        val trimmedText = text.trim()

        if (trimmedText.isEmpty()) {
            return
        }

        val currentState = _uiState.value

        if (currentState !is NewsUiState.Success) {
            return
        }

        val postUiModel = currentState.posts.firstOrNull {
            it.post.id == postId
        } ?: return

        if (!postUiModel.canComment) {
            return
        }

        /*
         * The existing comment repository remains responsible for
         * persistence. Permission is checked before the write.
         */
        viewModelScope.launch {
            try {
                commentRepository.saveComment(
                    Comment(
                        id = "",
                        postId = postId,
                        userId = currentUserId,
                        text = trimmedText
                    )
                )

                loadPosts()
            } catch (_: Exception) {
                // Keep the current UI state when comment persistence fails.
            }
        }
    }

    fun deletePost(postId: String) {
        val currentState = _uiState.value

        if (currentState !is NewsUiState.Success) {
            return
        }

        val postUiModel = currentState.posts.firstOrNull {
            it.post.id == postId
        } ?: return

        if (!postUiModel.canManage) {
            return
        }

        viewModelScope.launch {
            try {
                val post = postUiModel.post

                postRepository.savePost(
                    post.copy(
                        lifecycleState = PostLifecycleState.BIN,
                        deletedAt = System.currentTimeMillis()
                    )
                )

                loadPosts()
            } catch (_: Exception) {
                // Keep the current UI state when deletion fails.
            }
        }
    }

    fun updatePostAudience(
        postId: String,
        audience: PostAudience
    ) {
        val currentState = _uiState.value

        if (currentState !is NewsUiState.Success) {
            return
        }

        val postUiModel = currentState.posts.firstOrNull {
            it.post.id == postId
        } ?: return

        if (!postUiModel.canManage) {
            return
        }

        viewModelScope.launch {
            try {
                postRepository.savePost(
                    postUiModel.post.copy(
                        audience = audience
                    )
                )

                loadPosts()
            } catch (_: Exception) {
                // Keep the current UI state when the update fails.
            }
        }
    }

    fun updatePostCaption(
        postId: String,
        caption: String
    ) {
        val currentState = _uiState.value

        if (currentState !is NewsUiState.Success) {
            return
        }

        val postUiModel = currentState.posts.firstOrNull {
            it.post.id == postId
        } ?: return

        if (!postUiModel.canManage) {
            return
        }

        viewModelScope.launch {
            try {
                postRepository.savePost(
                    postUiModel.post.copy(
                        caption = caption
                    )
                )

                loadPosts()
            } catch (_: Exception) {
                // Keep the current UI state when the update fails.
            }
        }
    }

    private fun resolvePostPermissions(
        post: Post
    ): PostPermissions {
        val userId = currentUserId.trim()

        if (userId.isEmpty()) {
            return PostPermissions(
                canView = false,
                canLike = false,
                canComment = false,
                canShare = false,
                canSave = false,
                canManage = false
            )
        }

        val isOwner = post.userId == userId

        /*
         * Private:
         * - Owner can see the post.
         * - Non-owner cannot see the post.
         * - Owner still has Save and management.
         * - Like / Comment / Share are intentionally suppressed.
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
            currentUserId = userId,
            otherUserId = post.userId
        )

        val isFriendOfFriend = isFriendOfFriend(
            currentUserId = userId,
            postOwnerId = post.userId
        )

        val isFollowing = isFollowing(
            followerId = userId,
            followingId = post.userId
        )

        /*
         * Post visibility rule:
         *
         * Public / Followers / Friends of Friends / Friends
         * -----------------------------------------------
         * The post itself remains visible to every FriendZone
         * account holder.
         *
         * Audience controls the COMMENT interaction scope,
         * not post-level visibility.
         */
        val canComment = when (post.audience) {
            PostAudience.PUBLIC -> true

            PostAudience.FOLLOWERS ->
                isFollowing || isFriendOfFriend || isFriend

            PostAudience.FRIENDS_OF_FRIENDS ->
                isFriendOfFriend || isFriend

            PostAudience.FRIENDS ->
                isFriend

            PostAudience.PRIVATE ->
                false
        }

        /*
         * Like and Share are available to all viewers of
         * non-private posts.
         *
         * Save is a basic right of a visible viewer.
         * It is not a Private-only permission.
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
        val repository = friendRepository ?: return false

        return try {
            repository.getFriends(currentUserId).any {
                it.userId == currentUserId &&
                    it.friendUserId == otherUserId
            } || repository.getFriends(otherUserId).any {
                it.userId == otherUserId &&
                    it.friendUserId == currentUserId
            }
        } catch (_: Exception) {
            false
        }
    }

    private fun isFollowing(
        followerId: String,
        followingId: String
    ): Boolean {
        val repository = followRepository ?: return false

        return try {
            repository.getFollowing(followerId).any {
                it.followerId == followerId &&
                    it.followingId == followingId
            }
        } catch (_: Exception) {
            false
        }
    }

    private fun isFriendOfFriend(
        currentUserId: String,
        postOwnerId: String
    ): Boolean {
        val repository = friendRepository ?: return false

        return try {
            val currentUserFriends = repository
                .getFriends(currentUserId)
                .map { it.friendUserId }
                .filter { it != postOwnerId }
                .toSet()

            if (currentUserFriends.isEmpty()) {
                return false
            }

            val ownerFriends = repository
                .getFriends(postOwnerId)
                .map { it.friendUserId }
                .toSet()

            /*
             * Exactly one degree:
             * current user -> friend -> post owner.
             *
             * Direct friendship also qualifies as FoF for the
             * cumulative comment rule, so direct friendship is
             * checked separately by isFriend().
             */
            currentUserFriends.any { friendId ->
                ownerFriends.contains(friendId)
            }
        } catch (_: Exception) {
            false
        }
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
