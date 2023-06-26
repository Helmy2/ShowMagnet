package com.example.showmagnet.ui.auth

//@HiltViewModel
//class AuthViewModel @Inject constructor(
//    private val authRepositoryImpl: AuthRepository
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(AuthState())
//    val state = _state.asStateFlow()
//
//    init {
//        _state.update {
//            it.copy(
//                isSignInSuccessful = authRepositoryImpl.isSignedIn(),
//                signInError = ""
//            )
//        }
//    }
//
//    fun onSignInResult(result: Intent?) {
//        viewModelScope.launch {
//            val signInResult = authRepositoryImpl.signInWithIntent(
//                intent = result!!
//            )
//            _state.update {
//                it.copy(
//                    isSignInSuccessful = signInResult.isSignedIn,
//                    signInError = signInResult.errorMessage
//                )
//            }
//        }
//    }
//
//    fun signOut() = viewModelScope.launch {
//        val isSignedOut = authRepositoryImpl.signOut()
//        _state.update {
//            it.copy(
//                isSignInSuccessful = !isSignedOut,
//                signInError = ""
//            )
//        }
//    }
//
//    fun signIn(email: String, password: String) = viewModelScope.launch {
//        val signInResult = authRepositoryImpl.signIn(email, password)
//        _state.update {
//            it.copy(
//                isSignInSuccessful = signInResult.isSignedIn,
//                signInError = ""
//            )
//        }
//    }
//
//    fun signUp(email: String, password: String) = viewModelScope.launch {
//        val signInResult = authRepositoryImpl.signUp(email, password)
//        _state.update {
//            it.copy(
//                isSignInSuccessful = signInResult.isSignedIn,
//                signInError = ""
//            )
//        }
//    }
//
//    suspend fun getSignInIntentSender(): IntentSender? = authRepositoryImpl.signInWithGoogle()
//
//}