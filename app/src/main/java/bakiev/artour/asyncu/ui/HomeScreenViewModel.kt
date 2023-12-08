package bakiev.artour.asyncu.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bakiev.artour.asyncu.services.GoogleAuthenticationService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val googleAuthenticationService: GoogleAuthenticationService) :
    ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeScreenUiState(account = googleAuthenticationService.lastSignedInAccount, inProgress = false, error = null)
    )

    val uiState: StateFlow<HomeScreenUiState>
        get() = _uiState.asStateFlow()

    @Composable
    fun rememberGoogleSignInLauncher() =
        rememberLauncherForActivityResult(SignInContract(googleAuthenticationService.signInIntent)) { account ->
            viewModelScope.launch {
                _uiState.update {
                    it.copy(account = account)
                }
            }
        }

    fun googleSignIn(launcher: ManagedActivityResultLauncher<Void, GoogleSignInAccount?>) {
        viewModelScope.launch { _uiState.update { it.copy(inProgress = true) } }
        launcher.launch(null)
    }

    fun googleSingOut() {
        googleAuthenticationService.signOut()
        viewModelScope.launch { _uiState.update { it.clearGoogleAccount() } }
    }

    private class SignInContract(private val signInIntent: Intent) :
        ActivityResultContract<Void, GoogleSignInAccount?>() {
        override fun createIntent(context: Context, input: Void): Intent = signInIntent

        override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? =
            if (resultCode == Activity.RESULT_OK) {
                // The Task returned from this call is always completed, no need to attach a listener.
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
                task.getResult(ApiException::class.java)
            } else {
                null
            }
    }
}
