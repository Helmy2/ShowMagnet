package com.example.showmagnet.ui.auth.sign_in

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.showmagnet.ui.assertTextFeildIsVariableAndHasValueWithTage
import com.example.showmagnet.ui.common.base.TestTage
import com.example.showmagnet.ui.common.theme.ShowMagnetTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class SignInScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialState() {
        initScreenState(state = SignInContract.State(),
            effect = emptyFlow(),
            handleEvent = {},
            onNavigationRequested = {})

        with(composeTestRule) {
            onNodeWithText("Welcome Back!").assertIsDisplayed()
            onNodeWithText("Please sign in to your account").assertIsDisplayed()

            assertTextFeildIsVariableAndHasValueWithTage(TestTage.EMAIL_TEXT_FIELD_TAGE, "")
            assertTextFeildIsVariableAndHasValueWithTage(TestTage.PASSWORD_TEXT_FIELD_TAGE, "")

            onNodeWithText("Sign In").assertIsDisplayed()
        }
    }

    @Test
    fun validState() {

        initScreenState(state = SignInContract.State(
            email = "test@test.test", password = "Password1234"
        ), effect = emptyFlow(), handleEvent = {}, onNavigationRequested = {})

        with(composeTestRule) {
            assertTextFeildIsVariableAndHasValueWithTage(
                TestTage.EMAIL_TEXT_FIELD_TAGE,
                "test@test.test"
            )
            onNodeWithText("Sign In").assertIsEnabled()
        }
    }

    @Test
    fun loadingWithEmailState() {

        initScreenState(
            state = SignInContract.State(loadingWithEmail = true),
            effect = emptyFlow(),
            handleEvent = {},
            onNavigationRequested = {})

        composeTestRule.onNodeWithTag(TestTage.LOADING).assertIsDisplayed()
    }

    @Test
    fun loadingWithGoogleState() {

        initScreenState(
            state = SignInContract.State(loadingWithGoogle = true),
            effect = emptyFlow(),
            handleEvent = {},
            onNavigationRequested = {})

        composeTestRule.onNodeWithTag(TestTage.LOADING).assertIsDisplayed()
    }

    @Test
    fun changeEmailEvent() {
        var email = ""

        initScreenState(state = SignInContract.State(), effect = emptyFlow(), handleEvent = {
            when (it) {
                is SignInContract.Event.EmailChanged -> {
                    email = it.email
                }

                else -> {}
            }
        }, onNavigationRequested = {})

        composeTestRule.onNodeWithTag(TestTage.EMAIL_TEXT_FIELD_TAGE)
            .performTextInput("test@test.test")

        assert(email == "test@test.test")
    }

    @Test
    fun changePasswordEvent() {
        var password = ""

        initScreenState(state = SignInContract.State(), effect = emptyFlow(), handleEvent = {
            when (it) {
                is SignInContract.Event.PasswordChanged -> {
                    password = it.password
                }

                else -> {}
            }
        }, onNavigationRequested = {})

        composeTestRule.onNodeWithTag(TestTage.PASSWORD_TEXT_FIELD_TAGE)
            .performTextInput("Password1234")

        assert(password == "Password1234")
    }

    @Test
    fun resetPasswordEvent() {
        var email = ""

        initScreenState(state = SignInContract.State(), effect = emptyFlow(), handleEvent = {
            when (it) {
                is SignInContract.Event.ResetPassword -> {
                    email = it.email
                }

                else -> {}
            }
        }, onNavigationRequested = {})

        composeTestRule.onNodeWithText("Forget Password?").performClick()
        composeTestRule.onNodeWithTag(TestTage.FORGET_PASSWORD_TEXT_FIELD_TAGE)
            .performTextInput("test@test.test")
        composeTestRule.onNodeWithText("Reset Password").performClick()

        assert(email == "test@test.test")
    }

    @Test
    fun showErrorToastEffect() {
        runBlocking {
            initScreenState(state = SignInContract.State(),
                effect = flow { emit(SignInContract.Effect.ShowErrorToast("Error")) },
                handleEvent = {},
                onNavigationRequested = {})
        }

        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
    }

    @Test
    fun showSuccessToastEffect() {
        runBlocking {
            initScreenState(state = SignInContract.State(),
                effect = flow { emit(SignInContract.Effect.ShowSuccessToast("Success")) },
                handleEvent = {},
                onNavigationRequested = {})
        }

        composeTestRule.onNodeWithText("Success").assertIsDisplayed()
    }

    @Test
    fun navigateToSignUpEvent() {
        var isNavigationRequested = false
        runBlocking {
            initScreenState(state = SignInContract.State(), effect = emptyFlow(), handleEvent = {
                when (it) {
                    SignInContract.Event.Navigation.ToSignUp -> {
                        isNavigationRequested = true
                    }

                    else -> {}
                }
            }, onNavigationRequested = {})
        }
        composeTestRule.onNodeWithText("Sign Up").performClick()

        assert(isNavigationRequested)
    }

    @Test
    fun signInWithEmailEvent() {
        var isSignInRequested = false
        runBlocking {
            initScreenState(
                state = SignInContract.State(
                    email = "test@test.test",
                    password = "Password1234"
                ), effect = emptyFlow(), handleEvent = {
                    when (it) {
                        is SignInContract.Event.SignInWithEmail -> {
                            isSignInRequested = true
                        }

                        else -> {}
                    }
                }, onNavigationRequested = { })
        }
        composeTestRule.onNodeWithText("Sign In").performClick()

        assert(isSignInRequested)
    }

    @Test
    fun signInWithGoogleEvent() {
        var isSignInRequested = false
        runBlocking {
            initScreenState(
                state = SignInContract.State(), effect = emptyFlow(), handleEvent = {
                    when (it) {
                        is SignInContract.Event.StartSignInWithGoogle -> {
                            isSignInRequested = true
                        }

                        else -> {}
                    }
                }, onNavigationRequested = { })
        }
        composeTestRule.onNodeWithTag(TestTage.SIGN_IN_WITH_GOOGLE).performClick()

        assert(isSignInRequested)
    }

    @Test
    fun navigateToSignUpEffect() {
        var isNavigationRequested = false
        runBlocking {
            initScreenState(state = SignInContract.State(),
                effect = flow { emit(SignInContract.Effect.Navigation.ToSignUp) },
                handleEvent = {},
                onNavigationRequested = {
                    isNavigationRequested = true
                })
        }

        assert(isNavigationRequested)
    }

    private fun initScreenState(
        state: SignInContract.State,
        effect: Flow<SignInContract.Effect>,
        handleEvent: (SignInContract.Event) -> Unit,
        onNavigationRequested: (SignInContract.Effect.Navigation) -> Unit
    ) {
        composeTestRule.setContent {
            ShowMagnetTheme {
                SignInScreen(state, effect, handleEvent, onNavigationRequested)
            }
        }
    }
}