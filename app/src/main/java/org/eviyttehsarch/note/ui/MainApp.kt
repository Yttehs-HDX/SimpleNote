package org.eviyttehsarch.note.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.eviyttehsarch.note.AppDestination
import org.eviyttehsarch.note.MainViewModel
import org.eviyttehsarch.note.SettingsViewModel
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.extra.navigateBack
import org.eviyttehsarch.note.extra.navigateSingleTopTo
import org.eviyttehsarch.note.ui.topbar.EditNoteTopBar
import org.eviyttehsarch.note.ui.topbar.NoteColumnTopBar
import org.eviyttehsarch.note.ui.topbar.SettingsTopBar

@Composable
fun MainApp(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val homeRoute = AppDestination.NotesColumnDestination.route
        val targetDestination by mainViewModel.targetDestination.collectAsState()
        val allNotes by mainViewModel.noteListFlow.collectAsState(initial = emptyList())
        val targetNote by mainViewModel.targetNote.collectAsState()
        var searchState by rememberSaveable { mutableStateOf(false) }
        var searchedNotes by rememberSaveable { mutableStateOf<List<NoteEntity>>(emptyList()) }
        Scaffold(
            floatingActionButton = {
                val location by settingsViewModel.location.collectAsState()
                AddNoteFloatingButton(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route
                            && !searchState,
                    startLocation = location,
                    onClick = {
                        mainViewModel.updateNote(NoteEntity(id = mainViewModel.generateUniqueId()))
                        val route = AppDestination.EditNoteDestination.route
                        mainViewModel.updateDestination(route)
                        navController.navigateSingleTopTo(route)
                    },
                    onStop = { targetLocation ->
                        settingsViewModel.saveLocationData(targetLocation)
                    }
                )
            },
            topBar = {
                NoteColumnTopBar(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route,
                    searchState = searchState,
                    onSearchStart = {
                        searchState = true
                    },
                    onSearchStop = {
                        searchState = false
                    },
                    onSearch = { input ->
                        searchedNotes = allNotes.filter { eachNote ->
                            input.lowercase() in eachNote.title.lowercase() ||
                                    input.lowercase() in eachNote.content.lowercase()
                        }
                    },
                    onClickSettingsButton = {
                        val route = AppDestination.SettingsDestination.route
                        mainViewModel.updateDestination(route)
                        navController.navigateSingleTopTo(route)
                    },
                    onBack = {
                        searchState = false
                    }
                )
                EditNoteTopBar(
                    visible = targetDestination == AppDestination.EditNoteDestination.route,
                    note = targetNote,
                    onSaveNote = { note ->
                        mainViewModel.updateNote(note)
                        mainViewModel.insertOrUpdate(targetNote)
                    },
                    onDeleteNote = { note ->
                        mainViewModel.deleteNote(note)
                    }
                ) {
                    mainViewModel.updateDestination(homeRoute)
                    navController.navigateBack()
                }
                SettingsTopBar(
                    visible = targetDestination == AppDestination.SettingsDestination.route,
                    onClickResetButton = {
                        settingsViewModel.resetSettings()
                    },
                    onBack = {
                        mainViewModel.updateDestination(homeRoute)
                        navController.navigateBack()
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = AppDestination.NotesColumnDestination.route
            ) {
                composable(route = AppDestination.NotesColumnDestination.route) {
                    AppDestination.NotesColumnDestination.Content(
                        viewModel = settingsViewModel,
                        noteList = if (searchState) searchedNotes else allNotes,
                        onClick = { note ->
                            val route = AppDestination.EditNoteDestination.route
                            mainViewModel.updateDestination(route)
                            mainViewModel.updateNote(note)
                            navController.navigateSingleTopTo(route)
                        },
                        onDeleteNote = { note ->
                            mainViewModel.deleteNote(note)
                        }
                    )
                }
                composable(route = AppDestination.EditNoteDestination.route) {
                    AppDestination.EditNoteDestination.Content(
                        note = targetNote,
                        onDone = { note ->
                            mainViewModel.updateNote(note)
                            mainViewModel.insertOrUpdate(targetNote)
                        },
                        onBack = {
                            mainViewModel.updateDestination(homeRoute)
                            navController.navigateBack()
                        }
                    )
                }
                composable(route = AppDestination.SettingsDestination.route) {
                    AppDestination.SettingsDestination.Content(
                        viewModel = settingsViewModel,
                        onBack = {
                            mainViewModel.updateDestination(homeRoute)
                            navController.navigateBack()
                        }
                    )
                }
            }
        }
    }
}