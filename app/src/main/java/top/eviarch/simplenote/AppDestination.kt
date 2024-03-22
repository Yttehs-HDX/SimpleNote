package top.eviarch.simplenote

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import top.eviarch.simplenote.data.NoteEntity
import top.eviarch.simplenote.ui.screen.EditNote
import top.eviarch.simplenote.ui.screen.NotesColumn
import top.eviarch.simplenote.ui.screen.Settings

interface AppDestination {
    val route: String

    data object NotesColumnDestination : AppDestination {
        override val route = "main"

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun Content(
            scrollBehavior: TopAppBarScrollBehavior,
            viewModel: SettingsViewModel,
            noteList: List<NoteEntity>,
            searchState: Boolean,
            matchedString: String,
            onClick: (NoteEntity) -> Unit,
            onDeleteNote: (NoteEntity) -> Unit
        ) {
            val style by viewModel.style.collectAsState()
            val dateFormat by viewModel.dateFormat.collectAsState()
            NotesColumn(
                scrollBehavior = scrollBehavior,
                noteList = noteList,
                style = style,
                dateFormat = dateFormat.toString(),
                searchState = searchState,
                matchedString = matchedString,
                onClick = onClick,
                onDeleteNote = onDeleteNote
            )
        }
    }

    data object EditNoteDestination : AppDestination {
        override val route = "note"

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun Content(
            note: NoteEntity,
            isReadOnly: Boolean,
            onDone: (NoteEntity) -> Unit,
            onBack: () -> Unit
        ) {
            EditNote(
                note = note,
                isReadOnly = isReadOnly,
                onDone = onDone,
                onBack = onBack
            )
        }
    }

    data object SettingsDestination : AppDestination {
        override val route = "Settings"

        @Composable
        fun Content(
            mainViewModel: MainViewModel,
            settingsViewModel: SettingsViewModel,
            onBack: () -> Unit
        ) {
            Settings(
                mainViewModel = mainViewModel,
                settingsViewModel = settingsViewModel,
                onBack = onBack
            )
        }
    }
}