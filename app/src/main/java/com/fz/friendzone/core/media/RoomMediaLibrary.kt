package com.fz.friendzone.core.media

import com.fz.friendzone.core.model.MediaAsset

class RoomMediaLibrary(
    private val metadataStore: PersistentMediaMetadataStore
) : MediaLibrary {

    override fun add(
        asset: MediaAsset
    ) {
        metadataStore.save(asset)
    }

    override fun getById(
        id: String
    ): MediaAsset? {
        return metadataStore.getById(id)
    }

    override fun getByOwner(
        ownerId: String
    ): List<MediaAsset> {
        return metadataStore.getByOwner(ownerId)
    }

    override fun getAll(): List<MediaAsset> {
        return metadataStore.getAll()
    }

    override fun delete(
        id: String
    ) {
        metadataStore.delete(id)
    }
}
