package com.fz.friendzone.core.media

import com.fz.friendzone.core.model.MediaAsset

interface PersistentMediaMetadataStore {

    fun save(asset: MediaAsset)

    fun getById(id: String): MediaAsset?

    fun getByOwner(ownerId: String): List<MediaAsset>

    fun getAll(): List<MediaAsset>

    fun delete(id: String)
}
