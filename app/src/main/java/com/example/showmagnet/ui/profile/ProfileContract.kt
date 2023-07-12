package com.example.showmagnet.ui.profile

import android.graphics.Bitmap
import com.example.showmagnet.domain.model.user.UserData
import com.example.showmagnet.ui.common.ViewEffect
import com.example.showmagnet.ui.common.ViewEvent
import com.example.showmagnet.ui.common.ViewState

class ProfileContract {
    sealed class Event : ViewEvent {
        object Refresh : Event()
        object SignOut : Event()
        class ToggleTheme(val darkTheme: Boolean) : Event()
        class UpdateProfile(val name: String, val bitmap: Bitmap?) : Event()
    }

    data class State(
        val loading: Boolean = true,
        val connected: Boolean = true,
        val user: UserData? = null,
        val dark: Boolean
    ) : ViewState

    sealed class Effect : ViewEffect {
        class ShowErrorToast(val message: String) : Effect()
    }
}