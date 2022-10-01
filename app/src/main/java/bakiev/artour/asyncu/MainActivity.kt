package bakiev.artour.asyncu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import bakiev.artour.asyncu.ui.theme.AsyncUTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsyncUTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(Modifier.width(IntrinsicSize.Max)) {
        // The child with no weight will have the specified size.
        Box(
            Modifier.weight(1f)
        )
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                //your onclick code here
            }) {
                Text(text = stringResource(id = R.string.google_drive_temp))
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                //your onclick code here
            }) {
                Text(text = "Simple Button")
            }
        }
        Box(
            Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AsyncUTheme {
        MainScreen()
    }
}