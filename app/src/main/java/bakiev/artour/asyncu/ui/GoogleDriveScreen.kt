package bakiev.artour.asyncu.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GoogleDriveScreen(viewModel: GoogleDriveViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val files = uiState.folders
            if (files != null) {
                GoogleFoldersScreen(files = files) { file ->
                    viewModel.loadListOfChildFolders(file)
                }
            } else {
                val intent = uiState.consentIntent
                if (intent == null) {
                    viewModel.loadListOfChildFoldersInCurrentFolder()
                } else {
                    val launcher = viewModel.rememberConsentLauncher()
                    LaunchedEffect(intent) {
                        launcher.launch(intent)
                    }
                }
            }
        }
    }
}
