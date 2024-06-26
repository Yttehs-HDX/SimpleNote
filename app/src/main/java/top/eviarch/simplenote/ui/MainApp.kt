package top.eviarch.simplenote.ui

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import top.eviarch.simplenote.FolderViewModel
import top.eviarch.simplenote.MainViewModel
import top.eviarch.simplenote.R
import top.eviarch.simplenote.SettingsViewModel
import top.eviarch.simplenote.StorageManagerValue
import top.eviarch.simplenote.core.SimpleNoteApplication
import top.eviarch.simplenote.data.FolderEntity
import top.eviarch.simplenote.data.NoteEntity
import top.eviarch.simplenote.extra.ToastUtil
import top.eviarch.simplenote.extra.UUIDUtil
import top.eviarch.simplenote.extra.bioAuthentication
import top.eviarch.simplenote.extra.navigateBack
import top.eviarch.simplenote.extra.navigateSingleTopTo
import top.eviarch.simplenote.ui.topbar.EditNoteTopBar
import top.eviarch.simplenote.ui.topbar.FolderManagerTopBar
import top.eviarch.simplenote.ui.topbar.NoteColumnTopBar
import top.eviarch.simplenote.ui.topbar.SettingsTopBar
import top.eviarch.simplenote.ui.topbar.WebViewContainerTopBar

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    context: Context,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    folderViewModel: FolderViewModel,
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

        val allFolders by folderViewModel.folderListFlow.collectAsState(initial = emptyList())
        var showSetFolderDialog by remember { mutableStateOf(false) }
        var selectedNotes by remember { mutableStateOf<List<NoteEntity>>(emptyList()) }
        selectedNotes = allNotes

        var searchState by rememberSaveable { mutableStateOf(false) }
        var matchedString by rememberSaveable { mutableStateOf("") }
        var searchedNotes by rememberSaveable { mutableStateOf<List<NoteEntity>>(emptyList()) }

        val noteColumnTopBarState = rememberTopAppBarState()
        val noteColumnTopBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(noteColumnTopBarState)

        var isReadOnly by remember { mutableStateOf(false) }

        val url by mainViewModel.url.collectAsState()
        Scaffold(
            Modifier.fillMaxSize(),
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
                        mainViewModel.updateNote(targetNote)
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
                    scrollBehavior = noteColumnTopBarScrollBehavior,
                    visible = targetDestination == AppDestination.NotesColumnDestination.route,
                    searchState = searchState,
                    onSearchStart = {
                        searchState = true
                        searchedNotes = allNotes.filter { eachNote ->
                            !eachNote.lock
                        }
                    },
                    onSearchStop = {
                        matchedString = ""
                        searchedNotes = emptyList()
                        searchState = false
                    },
                    onSearch = { input ->
                        matchedString = input
                        searchedNotes = allNotes.filter { eachNote ->
                            if (eachNote.lock) false
                            else input in eachNote.title || input in eachNote.content
                        }
                    },
                    onClickSettingsButton = {
                        val route = AppDestination.SettingsDestination.route
                        mainViewModel.updateDestination(route)
                        navController.navigateSingleTopTo(route)
                    },
                    onBack = {
                        matchedString = ""
                        searchedNotes = emptyList()
                        searchState = false
                    }
                )
                FolderManagerTopBar(
                    visible = targetDestination == AppDestination.FolderManagerDestination.route,
                    onAddFolder = {
                        var name = SimpleNoteApplication.Context.getString(R.string.default_folder_name)
                        while (allFolders.any { it.name == name }) {
                            name = "$name-${SimpleNoteApplication.Context.getString(R.string.repeated_suffix)}"
                        }
                        folderViewModel.updateFolder(FolderEntity(UUIDUtil.generateUniqueId(), name))
                    },
                    onBack = {
                        mainViewModel.updateDestination(homeRoute)
                        navController.navigateBack()
                    }
                )
                EditNoteTopBar(
                    visible = targetDestination == AppDestination.EditNoteDestination.route,
                    note = targetNote,
                    isReadOnly = isReadOnly,
                    onSaveNote = { note ->
                        mainViewModel.updateNote(note)
                    },
                    onDeleteNote = { note ->
                        mainViewModel.deleteNote(note)
                    },
                    onBack = {
                        mainViewModel.clearTargetNote()
                        mainViewModel.updateDestination(homeRoute)
                        navController.navigateBack()
                    },
                    enableReadOnly = {
                        isReadOnly = !isReadOnly
                    }
                )
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
                WebViewContainerTopBar(
                    visible = targetDestination == AppDestination.WebViewDestination.route,
                    url = url,
                    onBack = {
                        mainViewModel.updateDestination(AppDestination.SettingsDestination.route)
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
                        scrollBehavior = noteColumnTopBarScrollBehavior,
                        folderViewModel = folderViewModel,
                        settingsViewModel = settingsViewModel,
                        noteList = if (searchState) searchedNotes else selectedNotes,
                        searchState = searchState,
                        matchedString = matchedString,
                        onClick = { note ->
                            if (note.lock) {
                                ToastUtil.showToast(SimpleNoteApplication.Context.getString(R.string.note_unlock_hint), Toast.LENGTH_LONG)
                                mainViewModel.updateTargetNote(targetNote)
                            } else {
                                mainViewModel.updateNote(note)
                                val route = AppDestination.EditNoteDestination.route
                                mainViewModel.updateDestination(route)
                                navController.navigateSingleTopTo(route)
                                searchState = false
                            }
                        },
                        onButtonClick = { note ->
                            if (note.lock) {
                                bioAuthentication(
                                    context = context,
                                    subTitle = SimpleNoteApplication.Context.getString(R.string.unlock_subtitle),
                                    onSuccess = {
                                        val route = AppDestination.EditNoteDestination.route
                                        mainViewModel.updateDestination(route)
                                        mainViewModel.updateNote(note)
                                        navController.navigateSingleTopTo(route)
                                        searchState = false
                                    }
                                )
                            } else {
                                mainViewModel.updateNote(note)
                                val route = AppDestination.EditNoteDestination.route
                                mainViewModel.updateDestination(route)
                                navController.navigateSingleTopTo(route)
                                searchState = false
                            }
                        },
                        onDeleteNote = { note ->
                            mainViewModel.deleteNote(note)
                            searchedNotes = searchedNotes.filter { it != note }
                        },
                        onManageFolders = {
                            val route = AppDestination.FolderManagerDestination.route
                            mainViewModel.updateDestination(route)
                            navController.navigateSingleTopTo(route)
                        },
                        onSelectFolder = { folder ->
                            selectedNotes = selectedNotes + allNotes.filter { it.folder == folder.name }
                        },
                        onUnselectFolder = { folder ->
                            selectedNotes = selectedNotes.filter { it.folder != folder.name }
                        },
                        onSelectAllFolders = {
                            selectedNotes = allNotes
                        },
                        onUnselectAllFolders = {
                            selectedNotes = emptyList()
                        },
                        onSetFolder = { note ->
                            mainViewModel.updateTargetNote(note)
                            showSetFolderDialog = true
                        }
                    )
                }
                composable(route = AppDestination.FolderManagerDestination.route) {
                    AppDestination.FolderManagerDestination.Content(
                        mainViewModel = mainViewModel,
                        folderViewModel = folderViewModel,
                        onBack = {
                            mainViewModel.updateDestination(homeRoute)
                            navController.navigateBack()
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
                        viewModel = mainViewModel,
                        isReadOnly = isReadOnly,
                        onBack = {
                            mainViewModel.clearTargetNote()
                            mainViewModel.updateDestination(homeRoute)
                            navController.navigateBack()
                        }
                    )
                }
                composable(route = AppDestination.SettingsDestination.route) {
                    AppDestination.SettingsDestination.Content(
                        context = context,
                        mainViewModel = mainViewModel,
                        settingsViewModel = settingsViewModel,
                        jumpUrl = { url ->
                            mainViewModel.updateUrl(url)
                            val route = AppDestination.WebViewDestination.route
                            mainViewModel.updateDestination(route)
                            navController.navigateSingleTopTo(route)
                        },
                        onBack = {
                            mainViewModel.updateDestination(homeRoute)
                            navController.navigateBack()
                        }
                    )
                }
                composable(route = AppDestination.WebViewDestination.route) {
                    AppDestination.WebViewDestination.Content(
                        url = url,
                        onBack = {
                            val route = AppDestination.SettingsDestination.route
                            mainViewModel.updateDestination(route)
                            navController.navigateBack()
                        }
                    )
                }
            }
        }
        val autoDelete by mainViewModel.showAutoDeleteDialog.collectAsState()
        val deleteDate by settingsViewModel.autoDeleteDate.collectAsState()
        val deletingNotes = if (deleteDate == StorageManagerValue.Never) emptyList() else {
            val timeLimit = System.currentTimeMillis() - deleteDate.toTimeMillis()
            allNotes.filter { it.modifiedDate < timeLimit }
        }
        AutoDeleteNoteAlert(
            showDialog = autoDelete && deletingNotes.isNotEmpty(),
            deletingNotes = deletingNotes,
            onConfirm = {
                mainViewModel.clearTargetNote()
                deletingNotes.forEach {
                    mainViewModel.deleteNote(it)
                }
            },
            onDismiss = {
                mainViewModel.updateAutoDeleteDialogVisibility(false)
            }
        )
        FolderDialog.SetFolderDialog(
            visible = showSetFolderDialog,
            note = targetNote,
            folders = allFolders,
            onConfirm = { folder ->
                mainViewModel.updateNote(targetNote.copy(folder = folder.name))
                showSetFolderDialog = false
            },
            onDismiss = {
                showSetFolderDialog = false
            }
        )
    }
}