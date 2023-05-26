package bakiev.artour.asyncu.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import bakiev.artour.asyncu.services.GoogleSignInService
import bakiev.artour.asyncu.ui.theme.AsyncUTheme
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleSignInService: GoogleSignInService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsyncUTheme(this) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(googleSignInService)
                }
            }
        }
    }
}

@Composable
fun MainScreen(googleSignInService: GoogleSignInService) {
    Column(Modifier.width(IntrinsicSize.Max)) {
        Box(
            Modifier.weight(1f)
        )
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val account: GoogleSignInAccount? by remember { mutableStateOf(googleSignInService.lastSignInAccount) }
            if (account == null) {
                SignInBlock(googleSignInService)
            }
        }
        Box(
            Modifier.weight(1f)
        )
    }
}

@Composable
private fun SignInBlock(googleSignInService: GoogleSignInService) {
    val launcher =
        rememberLauncherForActivityResult(contract = googleSignInService.signInContract()) { account: GoogleSignInAccount? -> }
    AndroidView(
        factory = { context ->
            SignInButton(context).apply {
                setOnClickListener {
                    launcher.launch(null)
                }
            }
        },
        update = { view ->
            view.setSize(SignInButton.SIZE_STANDARD)
        }
    )
}
