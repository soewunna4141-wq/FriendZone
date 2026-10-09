package com.fz.friendzone.data.local

import com.fz.friendzone.core.model.Post
import com.fz.friendzone.core.model.PostLifecycleState

class RoomNewsLocalDataSource(
    private val postDao: PostDao
) : NewsLocalDataSource {

    override fun getPosts(): List<Post> {
        return postDao.getActivePosts().map { entity ->
            entity.toPost()
        }
    }

    override fun getBinPosts(): List<Post> {
        return postDao.getBinPosts().map { entity ->
            entity.toPost()
        }
    }

    override fun savePost(post: Post) {
        postDao.insertOrReplace(
            PostEntity.fromPost(
                post.copy(
                    lifecycleState = PostLifecycleState.ACTIVE,
                    deletedAt = null
                )
            )
        )
    }

    override fun updatePost(post: Post) {
        postDao.update(
            PostEntity.fromPost(post)
        )
    }

    override fun movePostToBin(postId: String) {
        postDao.moveToBin(
            postId = postId,
            deletedAt = System.currentTimeMillis()
        )
    }

    override fun restorePost(postId: String) {
        postDao.restoreFromBin(postId)
    }

    override fun movePostToAsh(postId: String) {
        postDao.moveToAsh(
            postId = postId,
            deletedAt = System.currentTimeMillis()
        )
    }
}
