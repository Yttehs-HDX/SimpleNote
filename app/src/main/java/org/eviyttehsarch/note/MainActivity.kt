package org.eviyttehsarch.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.eviyttehsarch.note.ui.theme.WriterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WriterTheme {
                SystemBar()
                MainApp(
                    viewModel = viewModel(
                        factory = AppViewModelProvider.Factory
                    )
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