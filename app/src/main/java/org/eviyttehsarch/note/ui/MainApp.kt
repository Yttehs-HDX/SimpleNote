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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.eviyttehsarch.note.AppDestination
import org.eviyttehsarch.note.MainViewModel
import org.eviyttehsarch.note.SettingsViewModel
import org.eviyttehsarch.note.data.EmptyId
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
        var targetDestination by remember { mutableStateOf(AppDestination.NotesColumnDestination.route) }
        val allNotes by mainViewModel.noteListFlow.collectAsState(initial = emptyList())
        var targetNote by remember { mutableStateOf(NoteEntity()) }
        var searchState by remember { mutableStateOf(false) }
        var searchedNotes by remember { mutableStateOf<List<NoteEntity>>(emptyList()) }
        Scaffold(
            floatingActionButton = {
                val location by settingsViewModel.location.collectAsState()
                AddNoteFloatingButton(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route
                            && !searchState,
                    startLocation = location,
                    onClick = {
                        targetNote = NoteEntity(id = (allNotes.size + 1).toLong())
                        val route = AppDestination.EditNoteDestination.route
                        targetDestination = route
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
                        searchedNotes = allNotes.filter { input in it.title.lowercase() }
                    },
                    onClickSettingsButton = {
                        val route = AppDestination.SettingsDestination.route
                        targetDestination = route
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
                        if (note.id == EmptyId) {
                            mainViewModel.insertOrUpdate(note.copy(id = mainViewModel.generateUniqueId()))
                        } else {
                            mainViewModel.insertOrUpdate(note)
                        }
                    },
                    onDeleteNote = { note ->
                        mainViewModel.deleteNote(note)
                    },
                    onBack = {
                        targetDestination = homeRoute
                        navController.navigateBack()
                    }
                )
                SettingsTopBar(
                    visible = targetDestination == AppDestination.SettingsDestination.route,
                    onClickResetButton = {
                        settingsViewModel.resetSettings()
                    },
                    onBack = {
                        targetDestination = homeRoute
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
                            targetDestination = route
                            targetNote = note
                            navController.navigateSingleTopTo(route)
                        }
                    )
                }
                composable(route = AppDestination.EditNoteDestination.route) {
                    AppDestination.EditNoteDestination.Content(
                        note = targetNote,
                        onDone = { note ->
                            targetNote = note
                            if (note.title != "" || note.content != "") {
                                if (note.id == EmptyId) {
                                    mainViewModel.insertOrUpdate(note.copy(id = mainViewModel.generateUniqueId()))
                                } else {
                                    mainViewModel.insertOrUpdate(note)
                                }
                            }
                        },
                        onBack = {
                            targetDestination = homeRoute
                            navController.navigateBack()
                        }
                    )
                }
                composable(route = AppDestination.SettingsDestination.route) {
                    AppDestination.SettingsDestination.Content(
                        viewModel = settingsViewModel,
                        onBack = {
                            targetDestination = homeRoute
                            navController.navigateBack()
                        }
                    )
                }
            }
        }
    }
}