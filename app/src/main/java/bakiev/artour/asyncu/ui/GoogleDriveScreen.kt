package bakiev.artour.asyncu.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleDriveScreen(viewModel: GoogleDriveViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(title = {
                Log.i("====>", "Path: ${uiState.path}")
                Text(text = uiState.path ?: "", maxLines = 1, overflow = TextOverflow.Visible)
            }, scrollBehavior = scrollBehavior)
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
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
}
