package bakiev.artour.asyncu.services

import android.content.Context
import bakiev.artour.asyncu.R
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.FileList

class DefaultGoogleDrive(
    private val context: Context,
    private val googleAuthenticationService: GoogleAuthenticationService
) : GoogleDrive {
    private val drive: Drive by lazy {
        val credential = GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE))
        credential.selectedAccount = googleAuthenticationService.lastSignedInAccount?.account
        Drive.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }

    override fun childFolders(parentFolder: GoogleDrive.File): List<GoogleDrive.File> {
        var pageToken: String? = null
        val files = mutableListOf<GoogleDrive.File>()
        do {
            val result: FileList = drive.files().list()
                .setQ("'${parentFolder.id}' in parents and trashed=false and mimeType='application/vnd.google-apps.folder'")
                .setSpaces("drive")
                .setFields("nextPageToken, files(id, name)")
                .setOrderBy("name")
                .setPageToken(pageToken)
                .execute()
            val parent = parentFolder.parent
            if (parent != null) {
                files.add(GoogleDrive.File.parentFolder(parent))
            }
            files.addAll(result.files.map { GoogleDrive.File.folder(it.id, it.name, parentFolder) })
            for (file in result.files) {
                println("Found file: name=${file.name} id=${file.id}")
            }
            pageToken = result.nextPageToken
        } while (pageToken != null)
        return files
    }

//    override fun childFolders(parentFolderId: String?): List<GoogleDrive.File> {
//        var pageToken: String? = null
//        val files = mutableListOf<GoogleDrive.File>()
//        do {
//            val result: FileList = drive.files().list()
//                .setQ("'${parentFolderId ?: "root"}' in parents and trashed=false and mimeType='application/vnd.google-apps.folder'")
//                .setSpaces("drive")
//                .setFields("nextPageToken, files(id, name)")
//                .setOrderBy("name")
//                .setPageToken(pageToken)
//                .execute()
//            files.addAll(result.files.map { GoogleDrive.File.folder(it.id, it.name) })
//            for (file in result.files) {
//                println("Found file: name=${file.name} id=${file.id}")
//            }
//            pageToken = result.nextPageToken
//        } while (pageToken != null)
//        return files
//    }
}