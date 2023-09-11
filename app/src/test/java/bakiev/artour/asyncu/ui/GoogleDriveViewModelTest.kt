package bakiev.artour.asyncu.ui

import bakiev.artour.asyncu.MainDispatcherRule
import bakiev.artour.asyncu.services.GoogleDrive
import bakiev.artour.asyncu.services.MockDispatcherProvider
import org.amshove.kluent.should
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class GoogleDriveViewModelTest {

    @get:Rule
    internal val dispatcherRule = MainDispatcherRule()

    private val dispatcherProvider = MockDispatcherProvider()

    /*
    / -
        /A1 -
            /A2 -
                 /A3
                 /B3
                 /C3
            /B2
            /C2
        /B1
        /C1
     */
    private val a1Folder = GoogleDrive.File.folder("A1", "RA1", GoogleDrive.File.root)
    private val a2Folder = GoogleDrive.File.folder("A2", "RA2", a1Folder)
    private val b1Folder = GoogleDrive.File.folder("B1", "RB1", GoogleDrive.File.root)
    private val c1Folder = GoogleDrive.File.folder("C1", "RC1", GoogleDrive.File.root)
    private val b2Folder = GoogleDrive.File.folder("B2", "RB2", a1Folder)
    private val c2Folder = GoogleDrive.File.folder("C2", "RC2", a1Folder)
    private val a3Folder = GoogleDrive.File.folder("A3", "RA3", a2Folder)
    private val b3Folder = GoogleDrive.File.folder("B3", "RB3", a2Folder)
    private val c3Folder = GoogleDrive.File.folder("C3", "RC3", a2Folder)

    private val googleDrive: MockGoogleDrive = MockGoogleDrive(
        mapOf(
            GoogleDrive.File.root.id to listOf(a1Folder, b1Folder, c1Folder),
            a1Folder.id to listOf(a2Folder, b2Folder, c2Folder),
            a2Folder.id to listOf(a3Folder, b3Folder, c3Folder)
        )
    )

    @Test
    fun `should list content of the root folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFoldersInCurrentFolder()

        viewModel.uiState.value.folders?.size shouldBeEqualTo 3
        viewModel.uiState.value.folders?.let { files ->
            files[0].name shouldBeEqualTo a1Folder.name
            files[0].id shouldBeEqualTo a1Folder.id
            files[0].parent shouldBeEqualTo GoogleDrive.File.root
            files[1].name shouldBeEqualTo b1Folder.name
            files[1].id shouldBeEqualTo b1Folder.id
            files[1].parent shouldBeEqualTo GoogleDrive.File.root
            files[2].name shouldBeEqualTo c1Folder.name
            files[2].id shouldBeEqualTo c1Folder.id
            files[2].parent shouldBeEqualTo GoogleDrive.File.root
        }
    }

    @Test
    fun `should expose path for the root folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFoldersInCurrentFolder()

        viewModel.uiState.value.path shouldBeEqualTo GoogleDrive.File.root.name
    }

    @Test
    fun `should list content of the first level folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFolders(a1Folder)

        viewModel.uiState.value.folders?.size shouldBeEqualTo 4
        viewModel.uiState.value.folders?.let { files ->
            files[0].name shouldBeEqualTo ".."
            files[0].id shouldBeEqualTo a1Folder.id
            files[0].parent shouldBeEqualTo GoogleDrive.File.root
            files[1].name shouldBeEqualTo a2Folder.name
            files[1].id shouldBeEqualTo a2Folder.id
            files[1].parent shouldBeEqualTo a1Folder
            files[2].name shouldBeEqualTo b2Folder.name
            files[2].id shouldBeEqualTo b2Folder.id
            files[2].parent shouldBeEqualTo a1Folder
            files[3].name shouldBeEqualTo c2Folder.name
            files[3].id shouldBeEqualTo c2Folder.id
            files[3].parent shouldBeEqualTo a1Folder
        }
    }

    @Test
    fun `should expose path for the first level folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFolders(a1Folder)

        viewModel.uiState.value.path shouldBeEqualTo "${GoogleDrive.File.root.name}/${a1Folder.name}"
    }

    @Test
    fun `should list content of the second level folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFolders(a1Folder)
        viewModel.loadListOfChildFolders(a2Folder)

        viewModel.uiState.value.folders?.size shouldBeEqualTo 4
        viewModel.uiState.value.folders?.let { files ->
            files[0].name shouldBeEqualTo ".."
            files[0].id shouldBeEqualTo a2Folder.id
            files[0].parent shouldBeEqualTo a1Folder
            files[1].name shouldBeEqualTo a3Folder.name
            files[1].id shouldBeEqualTo a3Folder.id
            files[1].parent shouldBeEqualTo a2Folder
            files[2].name shouldBeEqualTo b3Folder.name
            files[2].id shouldBeEqualTo b3Folder.id
            files[2].parent shouldBeEqualTo a2Folder
            files[3].name shouldBeEqualTo c3Folder.name
            files[3].id shouldBeEqualTo c3Folder.id
            files[3].parent shouldBeEqualTo a2Folder
        }
    }

    @Test
    fun `should expose path for the second level folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFolders(a1Folder)
        viewModel.loadListOfChildFolders(a2Folder)

        viewModel.uiState.value.path shouldBeEqualTo "${GoogleDrive.File.root.name}/${a1Folder.name}/${a2Folder.name}"
    }

    @Test
    fun `should expose path after jumping to the first level folder`() {
        val viewModel = GoogleDriveViewModel(googleDrive, dispatcherProvider, mock())
        viewModel.loadListOfChildFoldersInCurrentFolder()

        viewModel.uiState.value.folders!!.size.should { this > 1 }
        viewModel.loadListOfChildFolders(viewModel.uiState.value.folders!![0])
        viewModel.uiState.value.folders!!.size.should { this > 1 }
        viewModel.loadListOfChildFolders(viewModel.uiState.value.folders!![0])
//        viewModel.loadListOfChildFolders(a1Folder)
//        viewModel.loadListOfChildFolders(a2Folder)
//        viewModel.loadListOfChildFolders(a1Folder)

        viewModel.uiState.value.path shouldBeEqualTo GoogleDrive.File.root.name
    }

    private class MockGoogleDrive(private val folders: Map<String, List<GoogleDrive.File>>) :
        GoogleDrive {
        override fun childFolders(parentFolder: GoogleDrive.File): List<GoogleDrive.File> {
            val children = folders[parentFolder.id]
            children ?: throw IllegalArgumentException("No $parentFolder found")
            val parent = parentFolder.parent
            return if (parent != null) {
                listOf(GoogleDrive.File.folder(parentFolder.id, "..", parent)) + children
            } else {
                children
            }
        }
    }
}
