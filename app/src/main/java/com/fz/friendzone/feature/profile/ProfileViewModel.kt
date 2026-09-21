package com.fz.friendzone.feature.profile

import androidx.lifecycle.ViewModel
import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val profile: Profile? = null
)

sealed interface ProfileAction {
    data object Load : ProfileAction
    data class Save(val profile: Profile) : ProfileAction
}

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            profile = repository.getProfile()
        )
    )

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.Load -> {
                val profile = repository.getProfile()

                _uiState.value = ProfileUiState(
                    profile = profile
                )
            }

            is ProfileAction.Save -> {
                repository.saveProfile(action.profile)

                val savedProfile = repository.getProfile()

                _uiState.value = ProfileUiState(
                    profile = savedProfile
                )
            }
        }
    }
}
