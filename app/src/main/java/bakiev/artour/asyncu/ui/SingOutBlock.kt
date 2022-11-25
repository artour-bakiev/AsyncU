package bakiev.artour.asyncu.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

@Composable
fun SingOutBlock(account: GoogleSignInAccount, onClick: () -> Unit) {
    Column(Modifier.width(IntrinsicSize.Max)) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Signed as '${account.email}'")
        }
        Button(onClick = onClick) { Text(text = "Sign Out") }
    }
}