package com.fz.friendzone.app

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fz.friendzone.core.media.FriendZoneDatabase
import com.fz.friendzone.core.media.MediaLibrary
import com.fz.friendzone.core.media.PersistentMediaMetadataStore
import com.fz.friendzone.core.media.RoomMediaLibrary
import com.fz.friendzone.core.media.RoomPersistentMediaMetadataStore
import com.fz.friendzone.core.saved.RoomSavedPostRepository
import com.fz.friendzone.core.saved.SavedPostRepository
import com.fz.friendzone.data.local.InMemoryReelsLocalDataSource
import com.fz.friendzone.data.repository.AccountRepository
import com.fz.friendzone.data.repository.AccountRepositoryFactory
import com.fz.friendzone.data.repository.AuthenticationRepository
import com.fz.friendzone.data.repository.AuthenticationRepositoryFactory
import com.fz.friendzone.data.repository.CommentRepository
import com.fz.friendzone.data.repository.CommentRepositoryFactory
import com.fz.friendzone.data.repository.FriendRepository
import com.fz.friendzone.data.repository.FriendRepositoryFactory
import com.fz.friendzone.data.repository.FollowRepository
import com.fz.friendzone.data.repository.FollowRepositoryFactory
import com.fz.friendzone.data.repository.NewsRepository
import com.fz.friendzone.data.repository.NewsRepositoryFactory
import com.fz.friendzone.data.repository.ProfileRepository
import com.fz.friendzone.data.repository.ProfileRepositoryFactory
import com.fz.friendzone.data.repository.ReactionRepository
import com.fz.friendzone.data.repository.ReactionRepositoryFactory
import com.fz.friendzone.data.repository.ReelsRepository
import com.fz.friendzone.data.repository.ReelsRepositoryImpl
import com.fz.friendzone.data.repository.UserRepository
import com.fz.friendzone.data.repository.UserRepositoryFactory
import com.fz.friendzone.feature.account.AccountViewModelFactory
import com.fz.friendzone.feature.follow.FollowViewModelFactory
import com.fz.friendzone.feature.friend.FriendViewModelFactory
import com.fz.friendzone.feature.login.LoginViewModelFactory
import com.fz.friendzone.feature.profile.ProfileViewModelFactory

class FriendZoneDependencies(
    private val context: Context
) {

    val database: FriendZoneDatabase by lazy {
        Room.databaseBuilder(
            context,
            FriendZoneDatabase::class.java,
            "friendzone.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    val persistentMediaMetadataStore: PersistentMediaMetadataStore by lazy {
        RoomPersistentMediaMetadataStore(
            database.mediaAssetDao()
        )
    }

    val mediaLibrary: MediaLibrary by lazy {
        RoomMediaLibrary(
            persistentMediaMetadataStore
        )
    }

    val savedPostRepository: SavedPostRepository by lazy {
        RoomSavedPostRepository(
            database.savedPostDao()
        )
    }

    val accountRepository: AccountRepository by lazy {
        AccountRepositoryFactory.create()
    }

    val accountViewModelFactory: AccountViewModelFactory by lazy {
        AccountViewModelFactory(accountRepository)
    }

    val authenticationRepository: AuthenticationRepository by lazy {
        AuthenticationRepositoryFactory.create()
    }

    val loginViewModelFactory: LoginViewModelFactory by lazy {
        LoginViewModelFactory(authenticationRepository)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryFactory.create()
    }

    val profileViewModelFactory: ProfileViewModelFactory by lazy {
        ProfileViewModelFactory(profileRepository)
    }

    val newsRepository: NewsRepository by lazy {
        NewsRepositoryFactory.create()
    }

    val reactionRepository: ReactionRepository by lazy {
        ReactionRepositoryFactory.create()
    }

    val commentRepository: CommentRepository by lazy {
        CommentRepositoryFactory.create()
    }

    val reelsRepository: ReelsRepository by lazy {
        ReelsRepositoryImpl(
            InMemoryReelsLocalDataSource()
        )
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryFactory.create()
    }

    val friendRepository: FriendRepository by lazy {
        FriendRepositoryFactory.create()
    }

    val friendViewModelFactory: FriendViewModelFactory by lazy {
        FriendViewModelFactory(friendRepository)
    }

    val followRepository: FollowRepository by lazy {
        FollowRepositoryFactory.create()
    }

    val followViewModelFactory: FollowViewModelFactory by lazy {
        FollowViewModelFactory(followRepository)
    }

    private companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(
                database: SupportSQLiteDatabase
            ) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `saved_posts` (
                        `userId` TEXT NOT NULL,
                        `postId` TEXT NOT NULL,
                        `savedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`userId`, `postId`)
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
