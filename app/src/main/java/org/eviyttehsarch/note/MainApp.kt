package org.eviyttehsarch.note

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.extra.navigateBack
import org.eviyttehsarch.note.extra.navigateSingleTopTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: AppViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        var targetDestination by remember { mutableStateOf(AppDestination.NotesColumnDestination.route) }
        var targetId by rememberSaveable { mutableLongStateOf(-1) }
        var useMainTopBar by remember { mutableStateOf(true) }
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.insertOrUpdate(NoteEntity(-1, "", "", System.currentTimeMillis()))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        tint = Color.Black,
                        contentDescription = null
                    )
                }
            },
            topBar = {
                if (useMainTopBar) {
                    TopAppBar(
                        title = {
                            Text(text = "Writer")
                        },
                        actions = {
                            // Search
                            IconButton(
                                onClick = {
                                    // TODO
                                }
                            ) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = targetDestination
            ) {
                composable(
                    route = AppDestination.NotesColumnDestination.route
                ) {
                    AppDestination.NotesColumnDestination.Content(
                        viewModel = viewModel,
                        onClick = { id ->
                            val route = AppDestination.NoteEditDestination.route
                            useMainTopBar = false
                            targetDestination = route
                            targetId = id
                            navController.navigateSingleTopTo(route)
                        }
                    )
                }
                composable(
                    route = AppDestination.NoteEditDestination.route
                ) {
                    val targetNote by viewModel.getNoteById(targetId).collectAsState(
                        initial = NoteEntity(
                            targetId,
                            "",
                            "",
                            System.currentTimeMillis()
                        )
                    )
                    AppDestination.NoteEditDestination.Content(
                        note = targetNote,
                        onSaveNote = { note ->
                            viewModel.insertOrUpdate(note)
                        },
                        onBack = {
                            val route = AppDestination.NotesColumnDestination.route
                            useMainTopBar = true
                            targetDestination = route
                            navController.navigateBack()
                        }
                    )
                }
            }
        }
    }
}