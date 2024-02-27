package org.eviyttehsarch.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
            val isRefreshing by remember { mutableStateOf(false) }
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
            SwipeRefresh(state = swipeRefreshState, onRefresh = { /*TODO:refresh function*/ }) {
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    NotesColumn(
                        noteList = noteList,
                        onClick = onClick
                    )
                }
            }
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