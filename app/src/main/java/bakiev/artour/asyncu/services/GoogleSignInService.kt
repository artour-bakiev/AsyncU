package bakiev.artour.asyncu.services

import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface GoogleSignInService {
    val lastSignInAccount: GoogleSignInAccount?

    fun signInContract(): ActivityResultContract<Void?, GoogleSignInAccount?>
}
