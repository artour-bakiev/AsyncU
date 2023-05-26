package bakiev.artour.asyncu.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bakiev.artour.asyncu.services.GoogleSignInService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val googleSignInService: GoogleSignInService) : ViewModel() {

    var googleSignInAccount: GoogleSignInAccount? by mutableStateOf(googleSignInService.lastSignInAccount)

    @Composable
    fun rememberGoogleSignInLauncher() =
        rememberLauncherForActivityResult(contract = googleSignInService.signInContract()) {
            googleSignInAccount = googleSignInService.lastSignInAccount
        }

    fun googleSingOut() {
        googleSignInService.signOut()
        googleSignInAccount = googleSignInService.lastSignInAccount
    }
}
