package bakiev.artour.asyncu.ui

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class HomeScreenUiState(val account: GoogleSignInAccount?, val inProgress: Boolean, val error: String?) {

    fun clearGoogleAccount(): HomeScreenUiState = copy(account = null, error = null)

    fun copy(account: GoogleSignInAccount?) : HomeScreenUiState {
        val error = if (account == null) {
            "No account selected"
        } else {
            null
        }
        return this.copy(account = account, error = error)
    }
}