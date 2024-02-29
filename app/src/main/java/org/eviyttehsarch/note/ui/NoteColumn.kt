package org.eviyttehsarch.note.ui

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eviyttehsarch.note.SettingsItem
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.extra.limitContent
import java.util.Date
import java.util.Locale

@Composable
fun NotesColumn(
    style: SettingsItem.Style.Value,
    noteList: List<NoteEntity>,
    onClick: (NoteEntity) -> Unit
) {
    if (noteList.isEmpty()) {
        EmptyNoteList()
    } else {
        when (style) {
            SettingsItem.Style.Value.Vertical -> {
                LazyColumn {
                    items(noteList) { note ->
                        NoteCard(
                            note = note,
                            onClick = { onClick(note) }
                        )
                    }
                }
            }
            SettingsItem.Style.Value.StaggeredGrid -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(200.dp),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(noteList) { note ->
                        NoteCard(
                            note = note,
                            onClick = { onClick(note) }
                        )
                    }
                }
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
                text = note.title.limitContent(20),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = note.content.limitContent(100),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .heightIn(30.dp)
            )
            val formattedDate = SimpleDateFormat("yy-MM-DD HH:MM", Locale.getDefault()).format(Date(note.modifiedDate))
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

@Composable
fun EmptyNoteList() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(64.dp),
            imageVector = Icons.AutoMirrored.Filled.List,
            tint = MaterialTheme.colorScheme.inverseOnSurface,
            contentDescription = "Empty list"
        )
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

@Preview(showBackground = true)
@Composable
fun PreviewEmptyNoteList() {
    EmptyNoteList()
}