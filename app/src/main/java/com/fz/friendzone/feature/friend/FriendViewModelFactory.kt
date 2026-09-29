package com.fz.friendzone.feature.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fz.friendzone.data.repository.FriendRepository

class FriendViewModelFactory(
    private val repository: FriendRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(FriendViewModel::class.java)) {
            return FriendViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
