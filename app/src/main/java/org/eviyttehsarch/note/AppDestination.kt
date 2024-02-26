package org.eviyttehsarch.note

import androidx.compose.runtime.Composable
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.ui.NoteEditScreen
import org.eviyttehsarch.note.ui.NotesColumn

interface AppDestination {
    val route: String

    data object NotesColumnDestination : AppDestination {
        override val route = "main"

        @Composable
        fun Content(
            viewModel: AppViewModel,
            onClick: (Long) -> Unit
        ) {
            NotesColumn(
                viewModel = viewModel,
                onClick = onClick
            )
        }
    }

    data object NoteEditDestination : AppDestination {
        override val route = "note"

        @Composable
        fun Content(
            note: NoteEntity,
            onSaveNote: (NoteEntity) -> Unit,
            onBack: () -> Unit
        ) {
            NoteEditScreen(
                note = note,
                onSaveNote = onSaveNote,
                onBack = onBack
            )
        }
    }
}