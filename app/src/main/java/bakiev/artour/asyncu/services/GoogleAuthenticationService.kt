package bakiev.artour.asyncu.services

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface GoogleAuthenticationService {
    val lastSignedInAccount: GoogleSignInAccount?
    val signInIntent: Intent
    fun signOut()
}
