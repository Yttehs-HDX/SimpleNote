package org.eviyttehsarch.note

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.ui.NoteEditScreen
import org.eviyttehsarch.note.ui.NotesColumn
import org.eviyttehsarch.note.ui.SettingsScreen

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
            NotesColumn(
                style = style,
                noteList = noteList,
                onClick = onClick
            )
        }
    }

    data object NoteEditDestination : AppDestination {
        override val route = "note"

        @Composable
        fun Content(
            note: NoteEntity,
            onDone: (NoteEntity) -> Unit,
            onBack: () -> Unit
        ) {
            NoteEditScreen(
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
            SettingsScreen(
                viewModel = viewModel,
                onBack = onBack
            )
        }
    }
}