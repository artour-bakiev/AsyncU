package bakiev.artour.asyncu.services

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class DefaultGoogleAuthenticationService(private val context: Context) : GoogleAuthenticationService {
    private val googleSignInClient by lazy(mode = LazyThreadSafetyMode.NONE) {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, signInOptions)
    }

    override val lastSignedInAccount: GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(context)

    override val signInIntent: Intent
        get() = googleSignInClient.signInIntent

    override fun signOut() {
        googleSignInClient.signOut()
    }
}
