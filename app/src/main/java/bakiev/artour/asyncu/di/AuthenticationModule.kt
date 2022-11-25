package bakiev.artour.asyncu.di

import android.content.Context
import android.content.Intent
import bakiev.artour.asyncu.services.GoogleAuthenticationService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationModule {

    @Provides
    fun provideGoogleAuthenticationService(@ApplicationContext context: Context): GoogleAuthenticationService =
        object : GoogleAuthenticationService {

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
}
