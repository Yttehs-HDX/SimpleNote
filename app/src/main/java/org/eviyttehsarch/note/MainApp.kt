package org.eviyttehsarch.note

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        val navController = rememberNavController()
        var targetDestination by remember { mutableStateOf(AppDestination.NotesColumnDestination.route) }
        val noteList by viewModel.noteListFlow.collectAsState(initial = emptyList())
        var targetNote by remember { mutableStateOf(NoteEntity()) }
        var isSearchState by remember { mutableStateOf(false) }
        var searchedNotes by remember { mutableStateOf<List<NoteEntity>>(emptyList()) }
        Scaffold(
            floatingActionButton = {
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
                                    text = "Writer"
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
                                    viewModel.insertOrUpdate(targetNote)
                                    val route = AppDestination.NotesColumnDestination.route
                                    targetDestination = route
                                    navController.navigateSingleTopTo(route)
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
                                    viewModel.insertOrUpdate(targetNote)
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
                                    viewModel.deleteNote(targetNote)
                                    val route = AppDestination.NotesColumnDestination.route
                                    targetDestination = route
                                    navController.navigateBack()
                                },
                                onDismiss = { showDialog = false }
                            )
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
                composable(
                    route = AppDestination.NotesColumnDestination.route
                ) {
                    AppDestination.NotesColumnDestination.Content(
                        noteList = if (isSearchState) searchedNotes else noteList,
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
                        onDone = {
                            targetNote = it
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteWaringAlertDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Delete this note!")
            },
            text = {
                Text(text = "It will lose forever!")
            },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Sure")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}