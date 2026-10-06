package com.fz.friendzone.feature.news

import androidx.lifecycle.ViewModel
import com.fz.friendzone.R
import com.fz.friendzone.core.media.MediaLibrary
import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostLifecycleState
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.repository.CommentRepository
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
    private val mediaLibrary: MediaLibrary
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)

    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    val currentProfile: Profile?
        get() = profileRepository.getProfile()

    fun onAction(action: NewsAction) {
        when (action) {
            NewsAction.Load -> loadPosts()
            is NewsAction.CreatePost -> createPost(action.post)
            is NewsAction.UpdatePost -> updatePost(action.post)
            is NewsAction.MovePostToBin -> movePostToBin(action.postId)
            is NewsAction.RestorePost -> restorePost(action.postId)
            is NewsAction.MovePostToAsh -> movePostToAsh(action.postId)
            is NewsAction.SaveReaction -> saveReaction(action.reaction)
            is NewsAction.SaveComment -> saveComment(action.comment)
        }
    }

    private fun createPost(post: Post) {
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
        reactionRepository.saveReaction(reaction)
        loadPosts()
    }

    private fun saveComment(
        comment: Comment
    ) {
        commentRepository.saveComment(comment)
        loadPosts()
    }

    private fun loadPosts() {
        _uiState.value = NewsUiState.Loading

        runCatching {
            newsRepository.getPosts()
        }.onSuccess { posts ->
            val postUiModels = posts.map { post ->
                NewsPostUiModel(
                    post = post,
                    profile = profileRepository.getProfile(post.userId),
                    reactionCount = reactionRepository
                        .getReactions(post.id)
                        .size,
                    comments = commentRepository
                        .getComments(post.id)
                )
            }

            _uiState.value = NewsUiState.Success(postUiModels)
        }.onFailure {
            _uiState.value = NewsUiState.Error(
                messageResId = R.string.news_load_error
            )
        }
    }
}
