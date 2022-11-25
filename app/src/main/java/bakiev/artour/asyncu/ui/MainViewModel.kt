package bakiev.artour.asyncu.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bakiev.artour.asyncu.services.GoogleAuthenticationService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val googleAuthenticationService: GoogleAuthenticationService) :
    ViewModel() {

    var googleSignInAccount: GoogleSignInAccount? by mutableStateOf(googleAuthenticationService.lastSignedInAccount)
        private set

    @Composable
    fun rememberGoogleSignInLauncher() =
        rememberLauncherForActivityResult(contract = SignInContract(googleAuthenticationService.signInIntent)) {
            googleSignInAccount = googleAuthenticationService.lastSignedInAccount
        }

    fun googleSingOut() {
        googleAuthenticationService.signOut()
        googleSignInAccount = googleAuthenticationService.lastSignedInAccount
    }

    private class SignInContract(private val signInIntent: Intent) :
        ActivityResultContract<Void?, GoogleSignInAccount?>() {
        override fun createIntent(context: Context, input: Void?): Intent = signInIntent

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
