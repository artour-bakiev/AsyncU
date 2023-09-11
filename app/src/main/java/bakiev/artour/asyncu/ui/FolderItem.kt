package bakiev.artour.asyncu.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import bakiev.artour.asyncu.R
import bakiev.artour.asyncu.services.GoogleDrive

@Composable
fun FolderItem(file: GoogleDrive.File, onClicked: (GoogleDrive.File) -> Unit) {
    ListItem(
        headlineContent = { Text(text = file.displayName) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked(file) },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_folder_open_24),
                contentDescription = null
            )
        }
    )
//    Text(
//        text = file.name,
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = { onClicked(file) }),
//        style = MaterialTheme.typography.bodyLarge
//    )
}

@Preview
@Composable
fun FolderItemPreview() {
    val file = GoogleDrive.File.folder("1", "Documents", GoogleDrive.File.root)
    FolderItem(file = file) {}
}
