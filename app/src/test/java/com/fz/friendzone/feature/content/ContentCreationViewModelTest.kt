package com.fz.friendzone.feature.content

import com.fz.friendzone.core.model.MediaAsset
import com.fz.friendzone.core.model.MediaSource
import com.fz.friendzone.core.model.MediaType
import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostMediaType
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.core.model.Reel
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ReelsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentCreationViewModelTest {

    @Test
    fun initialState_usesNewsTarget() {
        val viewModel = createViewModel(ContentCreationTarget.NEWS)

        assertEquals(
            ContentCreationTarget.NEWS,
            viewModel.uiState.value.target
        )
        assertNull(viewModel.uiState.value.selectedMedia)
        assertEquals("", viewModel.uiState.value.caption)
        assertFalse(viewModel.uiState.value.isCreated)
    }

    @Test
    fun initialState_usesReelsTarget() {
        val viewModel = createViewModel(ContentCreationTarget.REELS)

        assertEquals(
            ContentCreationTarget.REELS,
            viewModel.uiState.value.target
        )
        assertNull(viewModel.uiState.value.selectedMedia)
        assertEquals("", viewModel.uiState.value.caption)
        assertFalse(viewModel.uiState.value.isCreated)
    }

    @Test
    fun mediaSelected_updatesSelectedMedia() {
        val viewModel = createViewModel(ContentCreationTarget.NEWS)
        val media = imageAsset()

        viewModel.onAction(
            ContentCreationAction.MediaSelected(media)
        )

        assertEquals(media, viewModel.uiState.value.selectedMedia)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun captionChanged_updatesCaption() {
        val viewModel = createViewModel(ContentCreationTarget.NEWS)

        viewModel.onAction(
            ContentCreationAction.CaptionChanged("Hello FriendZone")
        )

        assertEquals(
            "Hello FriendZone",
            viewModel.uiState.value.caption
        )
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun createWithoutMedia_setsErrorAndDoesNotSave() {
        val newsRepository = RecordingNewsRepository()
        val reelsRepository = RecordingReelsRepository()
        val viewModel = ContentCreationViewModel(
            newsRepository = newsRepository,
            reelsRepository = reelsRepository,
            profileRepository = RecordingProfileRepository(),
            target = ContentCreationTarget.NEWS
        )

        viewModel.onAction(ContentCreationAction.Create)

        assertEquals(
            "Please add text or select media.",
            viewModel.uiState.value.errorMessage
        )
        assertFalse(viewModel.uiState.value.isCreated)
        assertTrue(newsRepository.savedPosts.isEmpty())
        assertTrue(reelsRepository.savedReels.isEmpty())
    }

    @Test
    fun createWithoutProfile_setsErrorAndDoesNotSave() {
        val newsRepository = RecordingNewsRepository()
        val reelsRepository = RecordingReelsRepository()
        val viewModel = ContentCreationViewModel(
            newsRepository = newsRepository,
            reelsRepository = reelsRepository,
            profileRepository = RecordingProfileRepository(
                profile = null
            ),
            target = ContentCreationTarget.NEWS
        )

        viewModel.onAction(
            ContentCreationAction.CaptionChanged("No profile")
        )
        viewModel.onAction(ContentCreationAction.Create)

        assertEquals(
            "Profile is required to create content.",
            viewModel.uiState.value.errorMessage
        )
        assertFalse(viewModel.uiState.value.isCreated)
        assertTrue(newsRepository.savedPosts.isEmpty())
        assertTrue(reelsRepository.savedReels.isEmpty())
    }

    @Test
    fun newsTextOnly_usesProfileIdentity() {
        val newsRepository = RecordingNewsRepository()
        val viewModel = ContentCreationViewModel(
            newsRepository = newsRepository,
            reelsRepository = RecordingReelsRepository(),
            profileRepository = RecordingProfileRepository(
                profile = Profile(
                    userId = "profile-user-001",
                    displayName = "FriendZone User"
                )
            ),
            target = ContentCreationTarget.NEWS
        )

        viewModel.onAction(
            ContentCreationAction.CaptionChanged("Text-only News")
        )
        viewModel.onAction(ContentCreationAction.Create)

        val post = newsRepository.savedPosts.single()

        assertEquals("profile-user-001", post.userId)
        assertEquals("Text-only News", post.caption)
        assertNull(post.mediaAssetId)
        assertNull(post.mediaUrl)
        assertEquals(PostMediaType.TEXT, post.mediaType)
        assertTrue(viewModel.uiState.value.isCreated)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun newsImage_usesProfileIdentityAndMediaReference() {
        val newsRepository = RecordingNewsRepository()
        val media = imageAsset()
        val viewModel = ContentCreationViewModel(
            newsRepository = newsRepository,
            reelsRepository = RecordingReelsRepository(),
            profileRepository = RecordingProfileRepository(
                profile = Profile(
                    userId = "profile-user-001",
                    displayName = "FriendZone User"
                )
            ),
            target = ContentCreationTarget.NEWS
        )

        viewModel.onAction(ContentCreationAction.MediaSelected(media))
        viewModel.onAction(
            ContentCreationAction.CaptionChanged("News image")
        )
        viewModel.onAction(ContentCreationAction.Create)

        val post = newsRepository.savedPosts.single()

        assertEquals("profile-user-001", post.userId)
        assertEquals("News image", post.caption)
        assertEquals(media.id, post.mediaAssetId)
        assertEquals(PostMediaType.IMAGE, post.mediaType)
        assertNull(post.mediaUrl)
        assertTrue(viewModel.uiState.value.isCreated)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun newsVideo_usesProfileIdentityAndMediaReference() {
        val newsRepository = RecordingNewsRepository()
        val media = videoAsset()
        val viewModel = ContentCreationViewModel(
            newsRepository = newsRepository,
            reelsRepository = RecordingReelsRepository(),
            profileRepository = RecordingProfileRepository(
                profile = Profile(
                    userId = "profile-user-001",
                    displayName = "FriendZone User"
                )
            ),
            target = ContentCreationTarget.NEWS
        )

        viewModel.onAction(ContentCreationAction.MediaSelected(media))
        viewModel.onAction(ContentCreationAction.Create)

        val post = newsRepository.savedPosts.single()

        assertEquals("profile-user-001", post.userId)
        assertEquals(media.id, post.mediaAssetId)
        assertEquals(PostMediaType.VIDEO, post.mediaType)
        assertTrue(viewModel.uiState.value.isCreated)
    }

    @Test
    fun reelsVideo_usesProfileIdentity() {
        val reelsRepository = RecordingReelsRepository()
        val media = videoAsset()
        val viewModel = ContentCreationViewModel(
            newsRepository = RecordingNewsRepository(),
            reelsRepository = reelsRepository,
            profileRepository = RecordingProfileRepository(
                profile = Profile(
                    userId = "profile-user-001",
                    displayName = "FriendZone User"
                )
            ),
            target = ContentCreationTarget.REELS
        )

        viewModel.onAction(ContentCreationAction.MediaSelected(media))
        viewModel.onAction(
            ContentCreationAction.CaptionChanged("My reel")
        )
        viewModel.onAction(ContentCreationAction.Create)

        val reel = reelsRepository.savedReels.single()

        assertEquals("profile-user-001", reel.userId)
        assertEquals(media.uri, reel.videoUrl)
        assertEquals("My reel", reel.caption)
        assertTrue(viewModel.uiState.value.isCreated)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun reelsImage_isRejectedWithoutSaving() {
        val reelsRepository = RecordingReelsRepository()
        val media = imageAsset()
        val viewModel = ContentCreationViewModel(
            newsRepository = RecordingNewsRepository(),
            reelsRepository = reelsRepository,
            profileRepository = RecordingProfileRepository(),
            target = ContentCreationTarget.REELS
        )

        viewModel.onAction(ContentCreationAction.MediaSelected(media))
        viewModel.onAction(ContentCreationAction.Create)

        assertEquals(
            "Reels require video media.",
            viewModel.uiState.value.errorMessage
        )
        assertFalse(viewModel.uiState.value.isCreated)
        assertTrue(reelsRepository.savedReels.isEmpty())
    }

    @Test
    fun newsRepositoryFailure_setsErrorState() {
        val newsRepository = object : NewsRepository {
            override fun getPosts(): List<Post> = emptyList()

            override fun getBinPosts(): List<Post> = emptyList()

            override fun savePost(post: Post) {
                error("Test save error")
            }

            override fun updatePost(post: Post) {
            }

            override fun movePostToBin(postId: String) {
            }

            override fun restorePost(postId: String) {
            }

            override fun movePostToAsh(postId: String) {
            }
        }

        val viewModel = ContentCreationViewModel(
            newsRepository = newsRepository,
            reelsRepository = RecordingReelsRepository(),
            profileRepository = RecordingProfileRepository(),
            target = ContentCreationTarget.NEWS
        )

        viewModel.onAction(
            ContentCreationAction.MediaSelected(imageAsset())
        )
        viewModel.onAction(ContentCreationAction.Create)

        assertFalse(viewModel.uiState.value.isCreated)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(
            "Test save error",
            viewModel.uiState.value.errorMessage
        )
    }

    @Test
    fun reelsRepositoryFailure_setsErrorState() {
        val reelsRepository = object : ReelsRepository {
            override fun getReels(): List<Reel> = emptyList()

            override fun saveReel(reel: Reel) {
                error("Test reel save error")
            }
        }

        val viewModel = ContentCreationViewModel(
            newsRepository = RecordingNewsRepository(),
            reelsRepository = reelsRepository,
            profileRepository = RecordingProfileRepository(),
            target = ContentCreationTarget.REELS
        )

        viewModel.onAction(
            ContentCreationAction.MediaSelected(videoAsset())
        )
        viewModel.onAction(ContentCreationAction.Create)

        assertFalse(viewModel.uiState.value.isCreated)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(
            "Test reel save error",
            viewModel.uiState.value.errorMessage
        )
    }

    private fun createViewModel(
        target: ContentCreationTarget
    ): ContentCreationViewModel {
        return ContentCreationViewModel(
            newsRepository = RecordingNewsRepository(),
            reelsRepository = RecordingReelsRepository(),
            profileRepository = RecordingProfileRepository(),
            target = target
        )
    }

    private fun imageAsset(): MediaAsset {
        return MediaAsset(
            id = "media-image-1",
            ownerId = "media-owner-999",
            uri = "content://friendzone/image-1",
            type = MediaType.IMAGE,
            source = MediaSource.GALLERY
        )
    }

    private fun videoAsset(): MediaAsset {
        return MediaAsset(
            id = "media-video-1",
            ownerId = "media-owner-999",
            uri = "content://friendzone/video-1",
            type = MediaType.VIDEO,
            source = MediaSource.GALLERY
        )
    }

    private class RecordingProfileRepository(
        private val profile: Profile? = Profile(
            userId = "user-1",
            displayName = "FriendZone User"
        )
    ) : ProfileRepository {

        override fun getProfile(): Profile? {
            return profile
        }

        override fun saveProfile(profile: Profile) {
        }
    }

    private class RecordingNewsRepository : NewsRepository {
        val savedPosts = mutableListOf<Post>()

        override fun getPosts(): List<Post> {
            return savedPosts.toList()
        }

        override fun getBinPosts(): List<Post> {
            return emptyList()
        }

        override fun savePost(post: Post) {
            savedPosts.add(post)
        }

        override fun updatePost(post: Post) {
        }

        override fun movePostToBin(postId: String) {
        }

        override fun restorePost(postId: String) {
        }

        override fun movePostToAsh(postId: String) {
        }
    }

    private class RecordingReelsRepository : ReelsRepository {
        val savedReels = mutableListOf<Reel>()

        override fun getReels(): List<Reel> {
            return savedReels.toList()
        }

        override fun saveReel(reel: Reel) {
            savedReels.add(reel)
        }
    }
}
