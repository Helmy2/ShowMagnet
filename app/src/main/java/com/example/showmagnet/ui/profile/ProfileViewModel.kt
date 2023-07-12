package com.example.showmagnet.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.showmagnet.domain.use_case.user.GetUserInfoUserCase
import com.example.showmagnet.domain.use_case.user.SignOutUseCase
import com.example.showmagnet.domain.use_case.user.UpdateDarkThemeUseCase
import com.example.showmagnet.domain.use_case.user.UpdateProfileUseCase
import com.example.showmagnet.domain.use_case.user.UserPreferencesUserCase
import com.example.showmagnet.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserInfoUserCase,
    private val signOutUserCase: SignOutUseCase,
    private val updateDarkThemeUseCase: UpdateDarkThemeUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    userPreferencesUserCase: UserPreferencesUserCase
) : BaseViewModel<ProfileContract.Event, ProfileContract.State, ProfileContract.Effect>() {

    override fun setInitialState() = ProfileContract.State(dark = false)

    override fun handleEvents(event: ProfileContract.Event) {
        when (event) {
            ProfileContract.Event.Refresh -> refresh()
            ProfileContract.Event.SignOut -> signOut()
            is ProfileContract.Event.ToggleTheme -> toggleTheme(event.darkTheme)
            is ProfileContract.Event.UpdateProfile -> updateProfile(event.name, event.bitmap)
        }
    }

    private fun updateProfile(name: String, bitmap: Bitmap?) = viewModelScope.launch {
        setState { copy(loading = true) }
        val result = updateProfileUseCase(name, bitmap)
        if (result.isSuccess) {
            refresh()
        } else {
            setEffect {
                ProfileContract.Effect.ShowErrorToast(
                    result.exceptionOrNull()?.localizedMessage ?: ""
                )
            }
        }
        setState { copy(loading = false) }
    }

    private fun toggleTheme(darkTheme: Boolean) = viewModelScope.launch {
        updateDarkThemeUseCase(darkTheme)
    }

    private fun signOut() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            val result = signOutUserCase()
            if (result.isFailure) {
                setState { copy(loading = false) }
                setEffect {
                    ProfileContract.Effect.ShowErrorToast(
                        result.exceptionOrNull()?.message ?: ""
                    )
                }
            }
        }
    }

    private fun refresh() = viewModelScope.launch {
        setState { copy(loading = true) }
        val result = getUserUseCase()
        if (result.isSuccess) {
            setState { copy(loading = false, connected = true, user = result.getOrNull()) }
        }

        setState { copy(loading = false) }
    }


    init {
        viewModelScope.launch {
            userPreferencesUserCase().collectLatest {
                setState { copy(dark = it.darkTheme) }
            }
        }
        refresh()
    }


}