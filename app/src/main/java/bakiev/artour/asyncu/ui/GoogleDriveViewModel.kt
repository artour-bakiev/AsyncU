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
    private val currentPath: MutableList<String> = savedState[currentPathKey]
        ?: mutableListOf<String>().also { it.add(GoogleDrive.rootFolderId) }

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
        loadListOfChildFolders(currentPath.last())
    }

    fun loadListOfChildFolders(parentFolder: GoogleDrive.File) {
        parentFolder.changeCurrentPath(currentPath)
        savedState[currentPathKey] = currentPath
        loadListOfChildFolders(parentFolder.id)
    }

    private fun loadListOfChildFolders(parentFolderId: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val directoriesOrConsentIntent = loadFoldersOrGetConsentIntent(parentFolderId)
        _uiState.update { directoriesOrConsentIntent.createUiState(it) }
    }

    private suspend fun loadFoldersOrGetConsentIntent(parentFolderId: String): FoldersOrConsentIntent =
        withContext(dispatcherProvider.io) {
            try {
                val files = drive.childFolders(parentFolderId).toMutableList()
                if (currentPath.size > 1) {
                    files.add(0, GoogleDrive.File.parentFolder(currentPath[currentPath.size - 2]))
                }
                FoldersOrConsentIntent.Folders(files)
            } catch (e: UserRecoverableAuthIOException) {
                FoldersOrConsentIntent.ConsentIntent(e.intent)
            }
        }

    private sealed class FoldersOrConsentIntent {
        abstract fun createUiState(uiState: GoogleDriveScreenUiState): GoogleDriveScreenUiState

        data class Folders(val files: List<GoogleDrive.File>) : FoldersOrConsentIntent() {
            override fun createUiState(uiState: GoogleDriveScreenUiState): GoogleDriveScreenUiState =
                uiState.copy(
                    isLoading = false,
                    folders = files,
                    consentIntent = null,
                )
        }

        data class ConsentIntent(val intent: Intent) : FoldersOrConsentIntent() {
            override fun createUiState(uiState: GoogleDriveScreenUiState): GoogleDriveScreenUiState =
                uiState.copy(
                    isLoading = false,
                    folders = null,
                    consentIntent = intent
                )
        }
    }

    private class ConsentContract : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent): Intent = input
        override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
            resultCode == Activity.RESULT_OK
    }

    companion object {
        private const val currentPathKey = "currentPath"
    }
}
