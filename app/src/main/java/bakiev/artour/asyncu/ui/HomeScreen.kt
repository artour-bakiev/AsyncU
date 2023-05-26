package bakiev.artour.asyncu.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(Modifier.width(IntrinsicSize.Max)) {
            Box(Modifier.weight(1f))
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                val account = uiState.account
                if (account == null) {
                    val launcher = viewModel.rememberGoogleSignInLauncher()
                    GoogleSignInWidget { launcher.launch(null) }
                } else {
                        GoogleDriveManageWidget(
                            account,
                            onManageDrive = { navController.navigate(Routes.GoogleDrive.route) },
                            onSignOut = { viewModel.googleSingOut() }
                        )
                }
            }
            Box(Modifier.weight(1f))
        }
    }
}
