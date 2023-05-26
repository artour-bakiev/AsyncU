package bakiev.artour.asyncu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bakiev.artour.asyncu.services.GoogleDrive

@Composable
fun GoogleFoldersScreen(files: List<GoogleDrive.File>, onClicked: (GoogleDrive.File) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        items(files) {
            FolderItem(file = it, onClicked = onClicked)
        }
    }
}

@Preview
@Composable
fun GoogleFoldersScreenPreview() {
    val file = GoogleDrive.File.folder("1", "Documents")
    val file2 = GoogleDrive.File.folder("2", "Personal")
    GoogleFoldersScreen(listOf(file, file2)) {}
}