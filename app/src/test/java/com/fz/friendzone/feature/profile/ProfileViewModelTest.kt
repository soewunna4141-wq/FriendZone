package com.fz.friendzone.feature.profile

import com.fz.friendzone.core.model.Profile
import com.fz.friendzone.data.local.FakeProfileLocalDataSource
import com.fz.friendzone.data.repository.ProfileRepositoryImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileViewModelTest {

    @Test
    fun initialState_loadsProfileFromRepository() {
        val dataSource = FakeProfileLocalDataSource()

        val existingProfile = Profile(
            userId = "existing-user",
            displayName = "Existing User",
            bio = "Existing Bio"
        )

        dataSource.saveProfile(existingProfile)

        val repository = ProfileRepositoryImpl(
            localDataSource = dataSource
        )

        val viewModel = ProfileViewModel(
            repository = repository
        )

        assertEquals(
            existingProfile,
            viewModel.uiState.value.profile
        )
    }

    @Test
    fun saveAction_updatesUiStateWithSavedProfile() {
        val dataSource = FakeProfileLocalDataSource()

        val repository = ProfileRepositoryImpl(
            localDataSource = dataSource
        )

        val viewModel = ProfileViewModel(
            repository = repository
        )

        val profile = Profile(
            userId = "test-user",
            displayName = "Test User",
            bio = "Test Bio"
        )

        viewModel.onAction(
            ProfileAction.Save(profile)
        )

        assertEquals(
            profile,
            viewModel.uiState.value.profile
        )
    }
}
