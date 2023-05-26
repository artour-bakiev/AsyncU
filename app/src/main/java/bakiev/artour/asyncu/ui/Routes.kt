package bakiev.artour.asyncu.ui

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object GoogleDrive : Routes("google-drive")
}
