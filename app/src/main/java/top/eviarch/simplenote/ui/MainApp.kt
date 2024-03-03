package top.eviarch.simplenote.ui

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import top.eviarch.simplenote.AppDestination
import top.eviarch.simplenote.MainViewModel
import top.eviarch.simplenote.SettingsViewModel
import top.eviarch.simplenote.data.NoteEntity
import top.eviarch.simplenote.extra.navigateBack
import top.eviarch.simplenote.extra.navigateSingleTopTo
import top.eviarch.simplenote.ui.topbar.EditNoteTopBar
import top.eviarch.simplenote.ui.topbar.NoteColumnTopBar
import top.eviarch.simplenote.ui.topbar.SettingsTopBar

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainApp(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val homeRoute = AppDestination.NotesColumnDestination.route
        val targetDestination by mainViewModel.targetDestination.collectAsState()
        val allNotes by mainViewModel.noteListFlow.collectAsState(initial = emptyList())
        val targetNote by mainViewModel.targetNote.collectAsState()
        var searchState by rememberSaveable { mutableStateOf(false) }
        var matchedString by rememberSaveable { mutableStateOf("") }
        var searchedNotes by rememberSaveable { mutableStateOf<List<NoteEntity>>(emptyList()) }
        Scaffold(
            floatingActionButton = {
                val configuration = LocalConfiguration.current
                val isLandscape = remember(configuration) {
                    configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                }
                val verticalPosition by settingsViewModel.verticalPosition.collectAsState()
                val horizontalPosition by settingsViewModel.horizontalPosition.collectAsState()
                AddNoteFloatingButton(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route
                            && !searchState,
                    landscape = isLandscape,
                    verticalStartPosition = verticalPosition,
                    horizontalStartPosition = horizontalPosition,
                    onClick = {
                        mainViewModel.updateNote(NoteEntity(id = mainViewModel.generateUniqueId()))
                        val route = AppDestination.EditNoteDestination.route
                        mainViewModel.updateDestination(route)
                        navController.navigateSingleTopTo(route)
                    },
                    onStop = { position ->
                        if (isLandscape) {
                            settingsViewModel.saveHorizontalPositionData(position)
                        } else {
                            settingsViewModel.saveVerticalPositionData(position)
                        }
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
                        matchedString = ""
                        searchState = false
                    },
                    onSearch = { input ->
                        matchedString = input
                        searchedNotes = allNotes.filter { eachNote ->
                            input in eachNote.title || input in eachNote.content
                        }
                    },
                    onClickSettingsButton = {
                        val route = AppDestination.SettingsDestination.route
                        mainViewModel.updateDestination(route)
                        navController.navigateSingleTopTo(route)
                    },
                    onBack = {
                        matchedString = ""
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
                        matchedString = ""
                        mainViewModel.updateDestination(homeRoute)
                        navController.navigateBack()
                    }
                )
            }
        ) { paddingValues ->
            @Suppress("DEPRECATION")
            AnimatedNavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = AppDestination.NotesColumnDestination.route,
            ) {
                composable(route = AppDestination.NotesColumnDestination.route) {
                    AppDestination.NotesColumnDestination.Content(
                        viewModel = settingsViewModel,
                        noteList = if (searchState) searchedNotes else allNotes,
                        searchState = searchState,
                        matchedString = matchedString,
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
                composable(
                    route = AppDestination.EditNoteDestination.route,
                    enterTransition = {
                        expandIn(
                            animationSpec = tween(500),
                            expandFrom = Alignment.Center
                        ) { IntSize.Zero }
                    },
                    exitTransition = {
                        shrinkOut(
                            animationSpec = tween(500),
                            shrinkTowards = Alignment.Center
                        ) { IntSize.Zero }
                    }
                ) {
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