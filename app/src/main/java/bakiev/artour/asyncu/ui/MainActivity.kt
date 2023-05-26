package bakiev.artour.asyncu.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bakiev.artour.asyncu.ui.theme.AsyncUTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsyncUTheme(this) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.Home.route) {
                    composable(Routes.Home.route) {
                        HomeScreen(navController)
                    }
                    composable(Routes.GoogleDrive.route) {
                        GoogleDriveScreen()
                    }
                }
            }
        }
    }
}
