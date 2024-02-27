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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
        SystemBar(
            statusBarColor = MaterialTheme.colorScheme.background,
            navigationColor = MaterialTheme.colorScheme.inverseOnSurface
        )

        val noteList by viewModel.noteListFlow.collectAsState(initial = emptyList())
        val navController = rememberNavController()
        var targetDestination by remember { mutableStateOf(AppDestination.NotesColumnDestination.route) }
        var targetNote by remember { mutableStateOf(NoteEntity()) }
        Scaffold(
            floatingActionButton = {
                AnimatedVisibility(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
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
                var isExpanded by remember { mutableStateOf(false) }
                var searchText by remember { mutableStateOf("") }
                AnimatedVisibility(
                    visible = targetDestination == AppDestination.NotesColumnDestination.route,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
                ) {
                    TopAppBar(
                        title = {
                            AnimatedVisibility(
                                visible = (!isExpanded),
                                enter = fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearEasing)),
                                exit = fadeOut(animationSpec = tween(durationMillis = 30, easing = LinearEasing))
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
                            Row {
                                AnimatedVisibility(
                                    visible = isExpanded,
                                    exit = fadeOut(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
                                ) {
                                    OutlinedTextField(
                                        value = searchText,
                                        onValueChange = { searchText = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        placeholder = { Text(text = "Search...") },
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color.Gray,
                                            unfocusedBorderColor = Color.LightGray
                                        ),
                                        trailingIcon = {
                                            IconButton(onClick = { isExpanded = false }) {
                                                Icon(Icons.Default.Search, contentDescription = "Search")
                                            }
                                        }
                                    )
                                }
                                if (!isExpanded) {
                                    IconButton(onClick = { isExpanded = true }) {
                                        Icon(
                                            Icons.Default.Search,
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
                    enter = fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
                ) {
                    TopAppBar(
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

@Suppress("DEPRECATION")
@Composable
fun SystemBar(
    statusBarColor: Color,
    navigationColor: Color
) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.apply {
            setStatusBarColor(statusBarColor)
            setNavigationBarColor(navigationColor)
        }
    }
}