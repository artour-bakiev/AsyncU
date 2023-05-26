package bakiev.artour.asyncu.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton

@Composable
fun GoogleSignInWidget(onClick: () -> Unit) {
    AndroidView(
        factory = { context ->
            SignInButton(context).apply { setOnClickListener { onClick() } }
        },
        update = { view -> view.setSize(SignInButton.SIZE_WIDE) }
    )
}