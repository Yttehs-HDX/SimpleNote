package org.eviyttehsarch.note

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val noteList by viewModel.noteListFlow.collectAsState(initial = emptyList())
        val navController = rememberNavController()
        var targetDestination by remember { mutableStateOf(AppDestination.NotesColumnDestination.route) }
        var targetNote by remember { mutableStateOf(NoteEntity()) }
        Scaffold(
            floatingActionButton = {
                AnimatedVisibility(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FloatingActionButton(
                        onClick = {
                            targetNote = NoteEntity()
                            val route = AppDestination.NoteEditDestination.route
                            targetDestination = route
                            navController.navigateSingleTopTo(route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            tint = Color.Black,
                            contentDescription = "Edit"
                        )
                    }
                }
            },
            topBar = {
                when (targetDestination) {
                    AppDestination.NotesColumnDestination.route -> {
                        TopAppBar(
                            title = {
                                Text(text = "Writer")
                            },
                            actions = {
                                // Search
                                IconButton(
                                    onClick = {
                                        // TODO()
                                    }
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = "Search")
                                }
                            }
                        )
                    }

                    AppDestination.NoteEditDestination.route -> {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Edit Note"
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        viewModel.insertOrUpdate(targetNote)
                                        val route = AppDestination.NotesColumnDestination.route
                                        targetDestination = route
                                        navController.navigateBack()
                                    }
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        viewModel.deleteNote(targetNote)
                                        val route = AppDestination.NotesColumnDestination.route
                                        targetDestination = route
                                        navController.navigateBack()
                                    }
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                                }
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = AppDestination.NotesColumnDestination.route
            ) {
                composable(
                    route = AppDestination.NotesColumnDestination.route
                ) {
                    AppDestination.NotesColumnDestination.Content(
                        noteList = noteList,
                        onClick = { note ->
                            val route = AppDestination.NoteEditDestination.route
                            targetDestination = route
                            targetNote = note
                            navController.navigateSingleTopTo(route)
                        }
                    )
                }
                composable(
                    route = AppDestination.NoteEditDestination.route
                ) {
                    AppDestination.NoteEditDestination.Content(
                        note = targetNote,
                        onDone = { targetNote = it }
                    )
                }
            }
        }
    }
}