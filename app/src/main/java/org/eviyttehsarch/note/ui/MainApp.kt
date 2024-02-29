package org.eviyttehsarch.note.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.containerColor
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.eviyttehsarch.note.AppDestination
import org.eviyttehsarch.note.LocationValue
import org.eviyttehsarch.note.MainViewModel
import org.eviyttehsarch.note.SettingsViewModel
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.extra.navigateBack
import org.eviyttehsarch.note.extra.navigateSingleTopTo
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel
) {
    val size = remember { mutableStateOf(Size.Zero) }
    Surface(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                size.value = coordinates.size.toSize()
            }
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var targetDestination by remember { mutableStateOf(AppDestination.NotesColumnDestination.route) }
        val noteList by mainViewModel.noteListFlow.collectAsState(initial = emptyList())
        var targetNote by remember { mutableStateOf(NoteEntity()) }
        var isSearchState by remember { mutableStateOf(false) }
        var searchedNotes by remember { mutableStateOf<List<NoteEntity>>(emptyList()) }
        var isNewNote by remember { mutableStateOf(false) }
        val location by settingsViewModel.location.collectAsState()
        Scaffold(
            floatingActionButton = {
                AnimatedVisibility(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route
                            && !isSearchState,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 100,
                            easing = LinearEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 100,
                            easing = LinearEasing
                        )
                    )
                ) {
                    var offset by remember { mutableStateOf(Offset(location.first, location.second)) }
                    FloatingActionButton(
                        modifier = Modifier
                            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                            .pointerInput(Unit) {
                                detectDragGesturesAfterLongPress(
                                    onDragEnd = {
                                        settingsViewModel.saveLocationData(
                                            LocationValue(offset.x, offset.y)
                                        )
                                    },
                                    onDrag = { _: PointerInputChange, dragAmount: Offset ->
                                        offset += dragAmount
                                        offset = Offset(
                                            x = offset.x.coerceIn(-size.value.width + 230f, 0f),
                                            y = offset.y.coerceIn(-size.value.height + 200f, 0f),
                                        )
                                    }
                                )
                            },
                        onClick = {
                            isNewNote = true
                            targetNote = NoteEntity(id = (noteList.size + 1).toLong())
                            val route = AppDestination.NoteEditDestination.route
                            targetDestination = route
                            navController.navigateSingleTopTo(route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            tint = MaterialTheme.colorScheme.contentColorFor(containerColor),
                            contentDescription = "Edit"
                        )
                    }
                }
            },
            topBar = {
                AnimatedVisibility(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                ) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        title = {
                            AnimatedVisibility(
                                visible = (!isSearchState),
                                enter = fadeIn(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = LinearEasing
                                    )
                                ),
                                exit = fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 30,
                                        easing = LinearEasing
                                    )
                                )
                            ) {
                                Text(
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Cursive,
                                    text = "Simple Note"
                                )
                            }
                        },
                        actions = {
                            var searchText by remember { mutableStateOf("") }
                            Row {
                                AnimatedVisibility(
                                    visible = isSearchState,
                                    exit = fadeOut(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = LinearEasing
                                        )
                                    )
                                ) {
                                    OutlinedTextField(
                                        value = searchText,
                                        onValueChange = { tempString ->
                                            searchText = tempString
                                            searchedNotes =
                                                noteList.filter { searchText.lowercase() in it.title.lowercase() }
                                        },
                                        maxLines = 1,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        placeholder = { Text(text = "Searching...") },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = {
                                                    isSearchState = false
                                                    searchText = ""
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Clear,
                                                    contentDescription = "Clear"
                                                )
                                            }
                                        }
                                    )
                                }
                                if (!isSearchState) {
                                    IconButton(onClick = { isSearchState = true }) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Search"
                                        )
                                    }
                                }
                            }
                            IconButton(
                                onClick = {
                                    val route = AppDestination.SettingsDestination.route
                                    targetDestination = route
                                    navController.navigateSingleTopTo(route)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    )
                }
                AnimatedVisibility(
                    visible = targetDestination == AppDestination.NoteEditDestination.route,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                ) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        title = {
                            Text(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Cursive,
                                text = "Edit Note"
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (isNewNote) {
                                        if (targetNote.title != "" || targetNote.content != "") {
                                            mainViewModel.insertOrUpdate(targetNote)
                                        }
                                        isNewNote = false
                                    } else {
                                        mainViewModel.insertOrUpdate(targetNote)
                                    }
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
                            var showDialog by remember { mutableStateOf(false) }
                            IconButton(
                                onClick = {
                                    mainViewModel.insertOrUpdate(targetNote)
                                    if (isNewNote) {
                                        isNewNote = false
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Done,
                                    contentDescription = "Save"
                                )
                            }
                            IconButton(
                                onClick = { showDialog = true }
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete")
                            }
                            DeleteWaringAlertDialog(
                                showDialog = showDialog,
                                onConfirm = {
                                    showDialog = false
                                    mainViewModel.deleteNote(targetNote)
                                    val route = AppDestination.NotesColumnDestination.route
                                    targetDestination = route
                                    navController.navigateBack()
                                },
                                onDismiss = { showDialog = false }
                            )
                        }
                    )
                }
                AnimatedVisibility(
                    visible = targetDestination == AppDestination.SettingsDestination.route,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                ) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        title = {
                            Text(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Cursive,
                                text = "Settings"
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    val route = AppDestination.NotesColumnDestination.route
                                    targetDestination = route
                                    navController.navigateBack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { settingsViewModel.resetSettings() }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = "Reset"
                                )
                            }
                        }
                    )
                }
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
                        noteList = if (isSearchState) searchedNotes else noteList,
                        onClick = { note ->
                            val route = AppDestination.NoteEditDestination.route
                            targetDestination = route
                            targetNote = note
                            navController.navigateSingleTopTo(route)
                        }
                    )
                }
                composable(route = AppDestination.NoteEditDestination.route) {
                    AppDestination.NoteEditDestination.Content(
                        note = targetNote,
                        onDone = { targetNote = it },
                        onBack = {
                            if (isNewNote) {
                                if (targetNote.title != "" || targetNote.content != "") {
                                    mainViewModel.insertOrUpdate(targetNote)
                                }
                                isNewNote = false
                            } else {
                                mainViewModel.insertOrUpdate(targetNote)
                            }
                            val route = AppDestination.NotesColumnDestination.route
                            targetDestination = route
                            navController.navigateBack()
                        }
                    )
                }
                composable(route = AppDestination.SettingsDestination.route) {
                    AppDestination.SettingsDestination.Content(
                        viewModel = settingsViewModel,
                        onBack = {
                            val route = AppDestination.NotesColumnDestination.route
                            targetDestination = route
                            navController.navigateBack()
                        }
                    )
                }
            }
        }
    }
}