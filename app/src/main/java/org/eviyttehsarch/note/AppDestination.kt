package org.eviyttehsarch.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.ui.screen.EditNote
import org.eviyttehsarch.note.ui.screen.NotesColumn
import org.eviyttehsarch.note.ui.screen.Settings

interface AppDestination {
    val route: String

    data object NotesColumnDestination : AppDestination {
        override val route = "main"

        @Composable
        fun Content(
            viewModel: SettingsViewModel,
            noteList: List<NoteEntity>,
            onClick: (NoteEntity) -> Unit
        ) {
            val style by viewModel.style.collectAsState()
            val dateFormat by viewModel.timeFormat.collectAsState()
            NotesColumn(
                noteList = noteList,
                style = style,
                dateFormat = dateFormat.toString(),
                onClick = onClick
            )
        }
    }

    data object EditNoteDestination : AppDestination {
        override val route = "note"

        @Composable
        fun Content(
            note: NoteEntity,
            onDone: (NoteEntity) -> Unit,
            onBack: () -> Unit
        ) {
            EditNote(
                note = note,
                onDone = onDone,
                onBack = onBack
            )
        }
    }

    data object SettingsDestination : AppDestination {
        override val route = "Settings"

        @Composable
        fun Content(
            viewModel: SettingsViewModel,
            onBack: () -> Unit
        ) {
            Settings(
                viewModel = viewModel,
                onBack = onBack
            )
        }
    }
}