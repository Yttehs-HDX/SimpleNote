package org.eviyttehsarch.note.ui

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.eviyttehsarch.note.AppViewModel
import org.eviyttehsarch.note.data.NoteEntity
import java.util.Date
import java.util.Locale


@Composable
fun NotesColumn(
    viewModel: AppViewModel,
    onClick: (Long) -> Unit
) {
    val noteList by viewModel.getAllNotes().collectAsState(initial = emptyList())

    val refreshState = remember { mutableStateOf(false) }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshState.value),
        onRefresh = { /*TODO*/ }
    ) {
        LazyColumn {
            items(noteList) { note ->
                NoteCard(
                    note = note,
                    onClick = {
                        onClick(note.id)
                    }
                )
            }
        }
    }
}



@Composable
fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(note.modifiedDate))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formattedDate.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNoteCard() {
    NoteCard(
        note = NoteEntity(0, "Simple title", "This is content", 0),
        onClick = { }
    )
}