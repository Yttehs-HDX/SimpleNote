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
            noteList: List<NoteEntity>,
            onClick: (NoteEntity) -> Unit
        ) {
            NotesColumn(
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
            onDone: (NoteEntity) -> Unit
        ) {
            NoteEditScreen(
                note = note,
                onDone = onDone
            )
        }
    }
}