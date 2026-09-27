package com.fz.friendzone.feature.news

import androidx.lifecycle.ViewModel
import com.fz.friendzone.core.model.Comment
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.core.model.Reaction
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReactionRepository
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NewsViewModelFactoryTest {

    @Test
    fun create_returnsNewsViewModel() {
        val factory = createFactory()

        val viewModel = factory.create(NewsViewModel::class.java)

        assertNotNull(viewModel)
        assertTrue(viewModel is NewsViewModel)
    }

    @Test
    fun create_returnsViewModelForRequestedClass() {
        val factory = createFactory()

        val viewModel: ViewModel =
            factory.create(NewsViewModel::class.java)

        assertTrue(viewModel is NewsViewModel)
    }

    private fun createFactory(): NewsViewModelFactory {
        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> {
                return emptyList()
            }

            override fun savePost(post: Post) {}
        }

        val profileRepository = object : ProfileRepository {
            override fun getProfile(): Profile? {
                return null
            }

            override fun getProfile(userId: String): Profile? {
                return null
            }

            override fun saveProfile(profile: Profile) {}
        }

        val reactionRepository = object : ReactionRepository {
            override fun getReactions(postId: String): List<Reaction> {
                return emptyList()
            }

            override fun saveReaction(reaction: Reaction) {}
        }

        val commentRepository = object : CommentRepository {
            override fun getComments(postId: String): List<Comment> {
                return emptyList()
            }

            override fun saveComment(comment: Comment) {}
        }

        return NewsViewModelFactory(
            newsRepository = newsRepository,
            profileRepository = profileRepository,
            reactionRepository = reactionRepository,
            commentRepository = commentRepository
        )
    }
}
