package top.eviarch.simplenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import top.eviarch.simplenote.core.AppViewModelProvider
import top.eviarch.simplenote.ui.MainApp
import top.eviarch.simplenote.ui.theme.SimpleNoteTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleNoteTheme {
                SystemBar()
                @Suppress("DEPRECATION")
                MainApp(
                    navController = rememberAnimatedNavController(),
                    settingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
                    mainViewModel = viewModel(factory = AppViewModelProvider.Factory)
                )
            }
        }
    }
}

@Suppress("DEPRECATION")
@Composable
fun SystemBar(
    statusBarColor: Color = MaterialTheme.colorScheme.primaryContainer,
    navigationColor: Color = MaterialTheme.colorScheme.background
) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.apply {
            setStatusBarColor(statusBarColor)
            setNavigationBarColor(navigationColor)
        }
    }
}