package bakiev.artour.asyncu.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bakiev.artour.asyncu.services.GoogleDrive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleFoldersScreen(files: List<GoogleDrive.File>, onClicked: (GoogleDrive.File) -> Unit) {
//    Column(modifier = Modifier.fillMaxSize()) {
//        TopAppBar(title = { Text(text = "Google Drive") })
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        items(files) {
            FolderItem(file = it, onClicked = onClicked)
            Divider()
        }
    }
//    }
}

@Preview
@Composable
fun GoogleFoldersScreenPreview() {
    val file = GoogleDrive.File.folder("1", "Documents", GoogleDrive.File.root)
    val file2 = GoogleDrive.File.folder("2", "Personal", GoogleDrive.File.root)
    val file3 = GoogleDrive.File.folder("3", "Archive", GoogleDrive.File.root)
    GoogleFoldersScreen(listOf(file, file2, file3)) {}
}
