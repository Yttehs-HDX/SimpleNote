package org.eviyttehsarch.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import org.eviyttehsarch.note.ui.theme.WriterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WriterTheme {
                MainApp(
                    viewModel = viewModel(
                        factory = AppViewModelProvider.Factory
                    )
                )
            }
        }
    }
}