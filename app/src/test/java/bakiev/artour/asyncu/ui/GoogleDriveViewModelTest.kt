package bakiev.artour.asyncu.ui

import bakiev.artour.asyncu.MainDispatcherRule
import bakiev.artour.asyncu.services.GoogleDrive
import bakiev.artour.asyncu.services.MockDispatcherProvider
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class GoogleDriveViewModelTest {

    @get:Rule
    internal val dispatcherRule = MainDispatcherRule()

    private val dispatcherProvider = MockDispatcherProvider()
    private val googleDrive = MockGoogleDrive(
        mapOf(
            GoogleDrive.rootFolderId to listOf(
                GoogleDrive.File.folder("A1", "RA1"),
                GoogleDrive.File.folder("B1", "RB1"),
                GoogleDrive.File.folder("C1", "RC1")
            ),
            "A1" to listOf(
                GoogleDrive.File.folder("A2", "RA2"),
                GoogleDrive.File.folder("B2", "RB2"),
                GoogleDrive.File.folder("C2", "RC2")
            ),
            "A2" to listOf(
                GoogleDrive.File.folder("A3", "RA3"),
                GoogleDrive.File.folder("B3", "RB3"),
                GoogleDrive.File.folder("C3", "RC3")
            )
        )
    )

    @Test
    fun `should list content of the root folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFoldersInCurrentFolder()

        viewModel.uiState.value.folders?.size shouldBeEqualTo 3
        viewModel.uiState.value.folders?.let { files ->
            files[0].name shouldBeEqualTo "RA1"
            files[0].id shouldBeEqualTo "A1"
            files[1].name shouldBeEqualTo "RB1"
            files[1].id shouldBeEqualTo "B1"
            files[2].name shouldBeEqualTo "RC1"
            files[2].id shouldBeEqualTo "C1"
        }
    }

    @Test
    fun `should list content of the first level folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFolders(GoogleDrive.File.folder("A1", "RA1"))

        viewModel.uiState.value.folders?.size shouldBeEqualTo 4
        viewModel.uiState.value.folders?.let { files ->
            files[0].name shouldBeEqualTo ".."
            files[0].id shouldBeEqualTo GoogleDrive.rootFolderId
            files[1].name shouldBeEqualTo "RA2"
            files[1].id shouldBeEqualTo "A2"
            files[2].name shouldBeEqualTo "RB2"
            files[2].id shouldBeEqualTo "B2"
            files[3].name shouldBeEqualTo "RC2"
            files[3].id shouldBeEqualTo "C2"
        }
    }

    @Test
    fun `should list content of the second level folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFolders(GoogleDrive.File.folder("A1", "RA1"))
        viewModel.loadListOfChildFolders(GoogleDrive.File.folder("A2", "RA2"))

        viewModel.uiState.value.folders?.size shouldBeEqualTo 4
        viewModel.uiState.value.folders?.let { files ->
            files[0].name shouldBeEqualTo ".."
            files[0].id shouldBeEqualTo "A1"
            files[1].name shouldBeEqualTo "RA3"
            files[1].id shouldBeEqualTo "A3"
            files[2].name shouldBeEqualTo "RB3"
            files[2].id shouldBeEqualTo "B3"
            files[3].name shouldBeEqualTo "RC3"
            files[3].id shouldBeEqualTo "C3"
        }
    }

    private class MockGoogleDrive(private val folders: Map<String?, List<GoogleDrive.File>>) :
        GoogleDrive {
        override fun childFolders(parentFolderId: String): List<GoogleDrive.File> =
            folders[parentFolderId] ?: throw IllegalArgumentException("No $parentFolderId found")
    }
}
