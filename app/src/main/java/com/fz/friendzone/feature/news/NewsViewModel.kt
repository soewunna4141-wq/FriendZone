package com.fz.friendzone.feature.news

import androidx.lifecycle.ViewModel
import com.fz.friendzone.R
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
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
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)

    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    val currentProfile: Profile?
        get() = profileRepository.getProfile()

    fun onAction(action: NewsAction) {
        when (action) {
            NewsAction.Load -> loadPosts()
            is NewsAction.CreatePost -> createPost(action.post)
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

    private fun saveReaction(
        reaction: com.fz.friendzone.core.model.Reaction
    ) {
        reactionRepository.saveReaction(reaction)
        loadPosts()
    }

    private fun saveComment(
        comment: com.fz.friendzone.core.model.Comment
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
                        .size
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
