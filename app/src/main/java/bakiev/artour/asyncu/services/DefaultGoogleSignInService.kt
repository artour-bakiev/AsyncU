package bakiev.artour.asyncu.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class DefaultGoogleSignInService(private val context: Context) : GoogleSignInService {

    private val signInClient by lazy(mode = LazyThreadSafetyMode.NONE) {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, signInOptions)
    }

    override val lastSignInAccount: GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(context)

    override fun signInContract(): ActivityResultContract<Void?, GoogleSignInAccount?> = SignInContract(signInClient)

    override fun signOut() {
        signInClient.signOut()
    }

    private class SignInContract(private val signInClient: GoogleSignInClient) :
        ActivityResultContract<Void?, GoogleSignInAccount?>() {
        override fun createIntent(context: Context, input: Void?): Intent = signInClient.signInIntent

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
