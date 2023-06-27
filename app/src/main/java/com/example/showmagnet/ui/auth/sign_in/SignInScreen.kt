package com.example.showmagnet.ui.auth.sign_in

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.showmagnet.R
import com.example.showmagnet.ui.auth.components.EmailTextField
import com.example.showmagnet.ui.auth.components.SignButton
import com.example.showmagnet.ui.auth.components.SignTextFiled
import com.example.showmagnet.ui.auth.components.TitleField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    state: SignInContract.State,
    handleEvent: (SignInContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var openResetPassword by rememberSaveable { mutableStateOf(false) }


    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.loading)
            CircularProgressIndicator()
        else {
            ResetPasswordDialog(
                onResetClick = {
                    openResetPassword = false
                    handleEvent(SignInContract.Event.ResetPassword(it))
                },
                onDismissRequest = { openResetPassword = false },
                keyboardController = keyboardController,
                open = openResetPassword
            )
            Column(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TitleField(
                    title = "Welcome Back!",
                    subTitle = "Please sign in to your account",
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.weight(.5f))
                EmailTextField(
                    email = state.email,
                    onValueChange = { handleEvent(SignInContract.Event.EmailChanged(it)) },
                    keyboardController = keyboardController,
                )
                PasswordField(
                    password = state.password,
                    onValueChange = { handleEvent(SignInContract.Event.PasswordChanged(it)) },
                    isHidden = passwordHidden,
                    onIsHiddenChange = { passwordHidden = it },
                    keyboardController = keyboardController,
                )
                ForgetPasswordField(
                    Modifier.align(Alignment.End),
                    onClick = { openResetPassword = true }
                )
                Spacer(modifier = Modifier.weight(1f))
                SignButton(enabled = state.isValuedSignUp,
                    onClick = { handleEvent(SignInContract.Event.SignInWithEmail) }, content = {
                        Text(text = "Sign In")
                    })
                SignWithGoogleButton(
                    modifier = Modifier,
                    onClick = { handleEvent(SignInContract.Event.StartSignInWithGoogle) })
                SignUpField(
                    Modifier,
                    onClick = { handleEvent(SignInContract.Event.NavigateToSignUp) })
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordDialog(
    onResetClick: (email: String) -> Unit,
    onDismissRequest: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    open: Boolean,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    AnimatedVisibility(visible = open) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                EmailTextField(
                    email = email,
                    onValueChange = { email = it },
                    keyboardController = keyboardController
                )
                Button(onClick = { onResetClick(email) }) {
                    Text(text = "Reset Password")
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PasswordField(
    password: String,
    isHidden: Boolean,
    onValueChange: (String) -> Unit,
    onIsHiddenChange: (Boolean) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier
) {
    SignTextFiled(
        value = password,
        onValueChange = onValueChange,
        label = {
            Text(
                "Password",
                style = MaterialTheme.typography.labelMedium,
            )
        },
        visualTransformation =
        if (isHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            IconButton(onClick = { onIsHiddenChange(!isHidden) }) {
                val visibilityIcon =
                    if (isHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (isHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
        modifier = modifier,
    )
}

@Composable
private fun SignUpField(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(modifier.padding(8.dp)) {
        Text(
            text = "Don't have an Account?",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onClick() },
        ) {
            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ForgetPasswordField(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() },
    ) {
        Text(
            text = "Forget Password?",
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun SignWithGoogleButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SignButton(
        onClick = onClick,
        content = {
            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google Icon",
                Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Sign In with Google")
        },
        modifier = modifier,
    )
}
