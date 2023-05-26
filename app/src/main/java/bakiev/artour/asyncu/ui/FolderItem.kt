package bakiev.artour.asyncu.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bakiev.artour.asyncu.services.GoogleDrive

@Composable
fun FolderItem(file: GoogleDrive.File, onClicked: (GoogleDrive.File) -> Unit) {
    Text(
        text = file.name,
        modifier = Modifier.clickable(onClick = { onClicked(file) }),
        style = MaterialTheme.typography.bodyLarge
    )
}
