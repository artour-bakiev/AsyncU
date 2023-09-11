package bakiev.artour.asyncu.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bakiev.artour.asyncu.services.DispatcherProvider
import bakiev.artour.asyncu.services.GoogleDrive
import bakiev.artour.asyncu.services.path
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class GoogleDriveViewModel @Inject constructor(
    private val drive: GoogleDrive,
    private val dispatcherProvider: DispatcherProvider,
    private val savedState: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(GoogleDriveScreenUiState.initialState)

    //    private val currentPath: MutableList<GoogleDrive.File> = savedState[currentPathKey]
//        ?: mutableListOf<GoogleDrive.File>().also { it.add(GoogleDrive.File.root) }
//        ?: mutableListOf()
//    private var currentPath: List<String> = savedState[currentPathKey] ?: emptyList()
    private var currentDir: GoogleDrive.File = savedState[currentDirKey] ?: GoogleDrive.File.root

    val uiState: StateFlow<GoogleDriveScreenUiState>
        get() = _uiState.asStateFlow()

    @Composable
    fun rememberConsentLauncher() =
        rememberLauncherForActivityResult(contract = ConsentContract()) { result ->
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = false) }
            }
        }

    fun loadListOfChildFoldersInCurrentFolder() {
        loadListOfChildFolders(currentDir)
    }

//    fun loadListOfChildFolders(parentFolder: GoogleDrive.File) {
//        loadListOfChildFolders(parentFolder.id)
//        parentFolder.changeCurrentPath(currentPath)
//        savedState[currentPathKey] = currentPath
//    }

    fun loadListOfChildFolders(parentFolder: GoogleDrive.File) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val newState = loadFoldersOrGetConsentIntent(parentFolder)
        _uiState.update { newState }
    }

    private suspend fun loadFoldersOrGetConsentIntent(parentFolder: GoogleDrive.File): GoogleDriveScreenUiState =
        withContext(dispatcherProvider.io) {
            try {
//                val files = drive.childFolders(parentFolderId).toMutableList()
//                if (currentPath.size > 1) {
//                    files.add(0, GoogleDrive.File.parentFolder(currentPath[currentPath.size - 2]))
//                }
                val files = drive.childFolders(parentFolder)
                GoogleDriveScreenUiState(
                    isLoading = false,
                    consentIntent = null,
                    folders = files,
                    path = parentFolder.path()
                )
            } catch (e: UserRecoverableAuthIOException) {
                GoogleDriveScreenUiState(
                    isLoading = false,
                    consentIntent = e.intent,
                    folders = null,
                    path = null
                )
            }
        }

    private class ConsentContract : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent): Intent = input
        override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
            resultCode == Activity.RESULT_OK
    }

    companion object {
        private const val currentDirKey = "currentDirKey"
    }
}
