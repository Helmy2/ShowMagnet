package com.example.showmagnet.ui.profile

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.showmagnet.R
import com.example.showmagnet.ui.common.ui.NameTextField
import com.example.showmagnet.ui.common.ui.SettingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    state: ProfileContract.State,
    effect: Flow<ProfileContract.Effect>,
    handleEvent: (ProfileContract.Event) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = effect) {
        effect.collectLatest {
            when (it) {
                is ProfileContract.Effect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message, duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
        ) {
            if (state.user != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(state.user.url)
                        .error(R.drawable.account).placeholder(R.drawable.account).crossfade(true)
                        .build(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = state.user.username ?: "user",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = state.user.email, style = MaterialTheme.typography.bodyMedium
                )
                EditProfileFeild(
                    state.user.username.orEmpty(),
                    state.user.url.orEmpty(),
                    onSave = { name, uri ->
                        val bitmap = uri?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                MediaStore.Images
                                    .Media.getBitmap(context.contentResolver, it)
                            } else {
                                val source = ImageDecoder
                                    .createSource(context.contentResolver, it)
                                ImageDecoder.decodeBitmap(source)
                            }
                        }

                        handleEvent(
                            ProfileContract.Event.UpdateProfile(
                                name,
                                bitmap
                            )
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                ThemeMode(
                    onThemeMode = { handleEvent(ProfileContract.Event.ToggleTheme(it)) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                SignOutFeild(
                    onSignOut = { handleEvent(ProfileContract.Event.SignOut) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileFeild(
    userName: String,
    imageUrl: String,
    onSave: (String, Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf(userName) }


    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    selectedImageUri = uri
                }
            }
        )

    BackHandler(sheetState.isVisible) {
        scope.launch { sheetState.hide() }
    }

    SettingItem(Icons.Outlined.Person, "Edit Profile", modifier) {
        scope.launch {
            sheetState.show()
        }
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.imePadding()
            ) {
                Text(text = "Edit Profile", style = MaterialTheme.typography.headlineLarge)
                Divider()

                AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(
                    if (selectedImageUri != null) selectedImageUri else imageUrl
                ).error(R.drawable.account).placeholder(R.drawable.account).crossfade(true)
                    .build(),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable {
                            launcher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        })

                NameTextField(name = name, onValueChange = { name = it })

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }
                        }, modifier = Modifier.weight(3f)
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            onSave(name, selectedImageUri)
                            scope.launch {
                                sheetState.hide()
                            }
                        }, modifier = Modifier.weight(3f)
                    ) {
                        Text(text = "Save")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun ThemeMode(onThemeMode: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    var enable by remember { mutableStateOf(isSystemInDarkTheme) }
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Outlined.RemoveRedEye,
            contentDescription = null,
        )
        Text(text = "Dark Mode")
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = enable, onCheckedChange = {
            enable = it
            Log.d("TAG", "ThemeMode: $it")
            onThemeMode(enable)
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignOutFeild(
    onSignOut: () -> Unit, modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        scope.launch { sheetState.hide() }
    }

    SettingItem(Icons.Default.Logout, "Sign Out", modifier) {
        scope.launch {
            sheetState.show()
        }
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "Sign Out", style = MaterialTheme.typography.headlineLarge)
                Divider()
                Text(text = "Are you sure you want to sign out?")
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }
                        }, modifier = Modifier.weight(3f)
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = onSignOut, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ), modifier = Modifier.weight(3f)
                    ) {
                        Text(text = "Sign Out")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


