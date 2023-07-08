package com.example.showmagnet.ui.auth.sign_up

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.showmagnet.ui.assertTextFeildIsVariableAndHasValueWithTage
import com.example.showmagnet.ui.common.theme.ShowMagnetTheme
import com.example.showmagnet.ui.common.utils.TestTage.EMAIL_TEXT_FIELD_TAGE
import com.example.showmagnet.ui.common.utils.TestTage.LOADING
import com.example.showmagnet.ui.common.utils.TestTage.NAME_TEXT_FIELD_TAGE
import com.example.showmagnet.ui.common.utils.TestTage.PASSWORD_TEXT_FIELD_TAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialState() {
        initScreenState(state = SignUpContract.State(),
            effect = emptyFlow(),
            handleEvent = {},
            onNavigationRequested = {})

        with(composeTestRule) {
            onNodeWithText("Create a new account").assertIsDisplayed()
            onNodeWithText("Please fill the form to continue").assertIsDisplayed()

            assertTextFeildIsVariableAndHasValueWithTage(NAME_TEXT_FIELD_TAGE, "")
            assertTextFeildIsVariableAndHasValueWithTage(EMAIL_TEXT_FIELD_TAGE, "")
            assertTextFeildIsVariableAndHasValueWithTage(
                PASSWORD_TEXT_FIELD_TAGE, ""
            )

            onNodeWithText("Sign Up").assertIsDisplayed()
        }
    }

    @Test
    fun validState() {

        initScreenState(state = SignUpContract.State(
            name = "test", email = "test@test.test", password = "Password1234"
        ), effect = emptyFlow(), handleEvent = {}, onNavigationRequested = {})

        with(composeTestRule) {
            assertTextFeildIsVariableAndHasValueWithTage(NAME_TEXT_FIELD_TAGE, "test")
            assertTextFeildIsVariableAndHasValueWithTage(
                EMAIL_TEXT_FIELD_TAGE, "test@test.test"
            )
            onNodeWithText("Sign Up").assertIsEnabled()
        }
    }

    @Test
    fun loadingState() {

        initScreenState(
            state = SignUpContract.State(loading = true),
            effect = emptyFlow(),
            handleEvent = {},
            onNavigationRequested = {})

        composeTestRule.onNodeWithTag(LOADING).assertIsDisplayed()
    }

    @Test
    fun changeNameEvent() {
        var name = ""

        initScreenState(state = SignUpContract.State(), effect = emptyFlow(), handleEvent = {
            when (it) {
                is SignUpContract.Event.NameChanged -> {
                    name = it.name
                }

                else -> {}
            }
        }, onNavigationRequested = {})

        composeTestRule.onNodeWithTag(NAME_TEXT_FIELD_TAGE).performTextInput("Name")

        assert(name == "Name")
    }

    @Test
    fun changeEmailEvent() {
        var email = ""

        initScreenState(state = SignUpContract.State(), effect = emptyFlow(), handleEvent = {
            when (it) {
                is SignUpContract.Event.EmailChanged -> {
                    email = it.email
                }

                else -> {}
            }
        }, onNavigationRequested = {})

        composeTestRule.onNodeWithTag(EMAIL_TEXT_FIELD_TAGE).performTextInput("test@test.test")

        assert(email == "test@test.test")
    }

    @Test
    fun changePasswordEvent() {
        var password = ""

        initScreenState(state = SignUpContract.State(), effect = emptyFlow(), handleEvent = {
            when (it) {
                is SignUpContract.Event.PasswordChanged -> {
                    password = it.password
                }

                else -> {}
            }
        }, onNavigationRequested = {})

        composeTestRule.onNodeWithTag(PASSWORD_TEXT_FIELD_TAGE).performTextInput("Password1234")

        assert(password == "Password1234")
    }

    @Test
    fun navigateToSignInEvent() {
        var isNavigationRequested = false
        runBlocking {
            initScreenState(
                state = SignUpContract.State(),
                effect = emptyFlow(),
                handleEvent = {},
                onNavigationRequested = {
                    when (it) {
                        SignUpContract.Navigation.ToSignIn -> {
                            isNavigationRequested = true
                        }
                    }
                })
        }
        composeTestRule.onNodeWithText("Sign In").performClick()

        assert(isNavigationRequested)
    }


    @Test
    fun showErrorToastEffect() {
        runBlocking {
            initScreenState(state = SignUpContract.State(),
                effect = flow { emit(SignUpContract.Effect.ShowErrorToast("Error")) },
                handleEvent = {},
                onNavigationRequested = {})
        }

        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
    }

    @Test
    fun showSuccessToastEffect() {
        runBlocking {
            initScreenState(state = SignUpContract.State(),
                effect = flow { emit(SignUpContract.Effect.ShowSuccessToast("Success")) },
                handleEvent = {},
                onNavigationRequested = {})
        }

        composeTestRule.onNodeWithText("Success").assertIsDisplayed()
    }


    private fun initScreenState(
        state: SignUpContract.State,
        effect: Flow<SignUpContract.Effect>,
        handleEvent: (SignUpContract.Event) -> Unit,
        onNavigationRequested: (SignUpContract.Navigation) -> Unit
    ) {
        composeTestRule.setContent {
            ShowMagnetTheme {
                SignUpScreen(state, effect, handleEvent, onNavigationRequested)
            }
        }
    }
}