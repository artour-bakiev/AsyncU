package bakiev.artour.asyncu.ui

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object GoogleDrive : Routes("google-drive")
}
