package com.fz.friendzone.core.media

import com.fz.friendzone.core.model.MediaAsset
import com.fz.friendzone.core.model.MediaSource
import com.fz.friendzone.core.model.MediaType

class RoomPersistentMediaMetadataStore(
    private val dao: MediaAssetDao
) : PersistentMediaMetadataStore {

    override fun save(asset: MediaAsset) {
        dao.save(
            MediaAssetEntity(
                id = asset.id,
                ownerId = asset.ownerId,
                uri = asset.uri,
                type = asset.type.name,
                source = asset.source.name,
                createdAt = asset.createdAt
            )
        )
    }

    override fun getById(
        id: String
    ): MediaAsset? {
        return dao.getById(id)?.toDomain()
    }

    override fun getByOwner(
        ownerId: String
    ): List<MediaAsset> {
        return dao.getByOwner(ownerId).map {
            it.toDomain()
        }
    }

    override fun getAll(): List<MediaAsset> {
        return dao.getAll().map {
            it.toDomain()
        }
    }

    override fun delete(
        id: String
    ) {
        dao.delete(id)
    }

    private fun MediaAssetEntity.toDomain(): MediaAsset {
        return MediaAsset(
            id = id,
            ownerId = ownerId,
            uri = uri,
            type = MediaType.valueOf(type),
            source = MediaSource.valueOf(source),
            createdAt = createdAt
        )
    }
}
