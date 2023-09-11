package bakiev.artour.asyncu.ui

import android.content.Intent
import bakiev.artour.asyncu.services.GoogleDrive

data class GoogleDriveScreenUiState(
    val isLoading: Boolean,
    val consentIntent: Intent?,
    val folders: List<GoogleDrive.File>?,
    val path: String?
) {
//    fun readablePath(): String {
//        return path?.joinToString(separator = "/") ?: "My Drive"
//    }

    companion object {
        val initialState =
            GoogleDriveScreenUiState(isLoading = false, consentIntent = null, folders = null, path = null)
    }
}