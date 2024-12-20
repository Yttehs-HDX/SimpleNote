package top.eviarch.simplenote

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import top.eviarch.simplenote.data.FolderEntity
import top.eviarch.simplenote.data.NoteEntity
import top.eviarch.simplenote.ui.screen.EditNote
import top.eviarch.simplenote.ui.screen.FolderManager
import top.eviarch.simplenote.ui.screen.NotesColumn
import top.eviarch.simplenote.ui.screen.Settings
import top.eviarch.simplenote.ui.screen.WebViewContainer

sealed interface AppDestination {
    val route: String

    data object NotesColumnDestination : AppDestination {
        override val route = "main"

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun Content(
            scrollBehavior: TopAppBarScrollBehavior,
            folderViewModel: FolderViewModel,
            settingsViewModel: SettingsViewModel,
            noteList: List<NoteEntity>,
            searchState: Boolean,
            matchedString: String,
            onClick: (NoteEntity) -> Unit,
            onButtonClick: (NoteEntity) -> Unit,
            onDeleteNote: (NoteEntity) -> Unit,
            onManageFolders: () -> Unit,
            onSelectFolder: (FolderEntity) -> Unit,
            onUnselectFolder: (FolderEntity) -> Unit,
            onSelectAllFolders: () -> Unit,
            onUnselectAllFolders: () -> Unit,
            onSetFolder: (NoteEntity) -> Unit
        ) {
            val style by settingsViewModel.style.collectAsState()
            val dateFormat by settingsViewModel.dateFormat.collectAsState()
            val autoDeleteDate by settingsViewModel.autoDeleteDate.collectAsState()
            NotesColumn(
                scrollBehavior = scrollBehavior,
                folderViewModel = folderViewModel,
                noteList = noteList,
                style = style,
                dateFormat = dateFormat.toString(),
                dateLimit = autoDeleteDate.toTimeMillis(),
                searchState = searchState,
                matchedString = matchedString,
                onClick = onClick,
                onButtonClick = onButtonClick,
                onDeleteNote = onDeleteNote,
                onManageFolders = onManageFolders,
                onSelectFolder = onSelectFolder,
                onUnselectFolder = onUnselectFolder,
                onSelectAllFolders = onSelectAllFolders,
                onUnselectAllFolders = onUnselectAllFolders,
                onSetFolder = onSetFolder
            )
        }
    }

    data object FolderManagerDestination : AppDestination {
        override val route = "folder"

        @Composable
        fun Content(
            folderViewModel: FolderViewModel,
            mainViewModel: MainViewModel,
            onBack: () -> Unit
        ) {
            FolderManager(
                folderViewModel = folderViewModel,
                mainViewModel = mainViewModel,
                onBack = onBack
            )
        }
    }

    data object EditNoteDestination : AppDestination {
        override val route = "note"

        @Composable
        fun Content(
            viewModel: MainViewModel,
            isReadOnly: Boolean,
            onBack: () -> Unit
        ) {
            EditNote(
                viewModel = viewModel,
                isReadOnly = isReadOnly,
                onBack = onBack
            )
        }
    }

    data object SettingsDestination : AppDestination {
        override val route = "settings"

        @Composable
        fun Content(
            context: Context,
            mainViewModel: MainViewModel,
            settingsViewModel: SettingsViewModel,
            jumpUrl: (String) -> Unit,
            onBack: () -> Unit
        ) {
            Settings(
                context = context,
                mainViewModel = mainViewModel,
                settingsViewModel = settingsViewModel,
                jumpUrl = jumpUrl,
                onBack = onBack
            )
        }
    }

    data object WebViewDestination : AppDestination {
        override val route = "web_view"

        @Composable
        fun Content(
            url: String,
            onBack: () -> Unit
        ) {
            WebViewContainer(
                url = url,
                onBack = onBack
            )
        }
    }
}