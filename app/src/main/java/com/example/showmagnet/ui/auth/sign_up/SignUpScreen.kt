package com.example.showmagnet.ui.auth.sign_up

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.showmagnet.ui.auth.components.EmailTextField
import com.example.showmagnet.ui.auth.components.LoadingButton
import com.example.showmagnet.ui.auth.components.SignTextFiled
import com.example.showmagnet.ui.auth.components.TitleField
import com.example.showmagnet.ui.common.utils.TestTage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    state: SignUpContract.State,
    effect: Flow<SignUpContract.Effect>,
    handleEvent: (SignUpContract.Event) -> Unit,
    handleNavigation: (SignUpContract.Navigation) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordHidden by rememberSaveable { mutableStateOf(true) }


    LaunchedEffect(key1 = effect) {
        effect.collectLatest {
            when (it) {
                is SignUpContract.Effect.ShowSuccessToast -> {
                    scope.launch {
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = it.message,
                            duration = SnackbarDuration.Long,
                            actionLabel = "Sign In",
                            withDismissAction = true
                        )
                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            handleNavigation(SignUpContract.Navigation.ToSignIn)
                        }
                    }
                }

                is SignUpContract.Effect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            TitleField(
                title = "Create a new account",
                subTitle = "Please fill the form to continue",
                modifier = Modifier,
            )
            Spacer(modifier = Modifier.weight(.5f))
            NameTextField(
                name = state.name,
                onValueChange = { handleEvent(SignUpContract.Event.NameChanged(it)) },
                modifier = Modifier
                    .testTag(TestTage.NAME_TEXT_FIELD_TAGE)
                    .fillMaxWidth(0.8f)
            )
            EmailTextField(
                email = state.email,
                onValueChange = { handleEvent(SignUpContract.Event.EmailChanged(it)) },
                keyboardController = keyboardController,
                modifier = Modifier
                    .testTag(TestTage.EMAIL_TEXT_FIELD_TAGE)
                    .fillMaxWidth(0.8f)
            )
            PasswordField(
                password = state.password ?: "",
                onValueChange = { handleEvent(SignUpContract.Event.PasswordChanged(it)) },
                keyboardController = keyboardController,
                isHidden = passwordHidden,
                onIsHiddenChange = { passwordHidden = it },
                modifier = Modifier
                    .testTag(TestTage.PASSWORD_TEXT_FIELD_TAGE)
                    .fillMaxWidth(0.8f)
            )
            AnimatedVisibility(visible = state.passwordRequirement != null) {
                PasswordRequirements(
                    state.passwordRequirement ?: "",
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
            Spacer(modifier = Modifier.weight(.5f))
            LoadingButton(
                enabled = state.isValuedSignUp,
                loading = state.loading,
                onClick = { handleEvent(SignUpContract.Event.SignUP) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = "Sign Up")
            }
            SignInField(onClick = { handleNavigation(SignUpContract.Navigation.ToSignIn) })
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun NameTextField(
    name: String, onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SignTextFiled(
        modifier = modifier,
        value = name,
        onValueChange = onValueChange,
        label = {
            Text(
                "Name",
                style = MaterialTheme.typography.labelMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
        )
    )
}

@Composable
fun PasswordRequirements(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(MaterialTheme.typography.bodyMedium.fontSize.value.dp),
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.clearAndSetSemantics { },
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
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
        visualTransformation = if (isHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            IconButton(onClick = { onIsHiddenChange(!isHidden) }) {
                val visibilityIcon =
                    if (isHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (isHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        },
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        modifier = modifier,
    )
}

@Composable
private fun SignInField(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Row(modifier.padding(8.dp)) {
        Text(
            text = "have an Account?", style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onClick() },
        ) {
            Text(
                text = "Sign In",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
