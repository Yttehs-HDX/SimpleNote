package org.eviyttehsarch.note.ui.screen

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eviyttehsarch.note.DateFormatValue
import org.eviyttehsarch.note.R
import org.eviyttehsarch.note.StyleValue
import org.eviyttehsarch.note.core.SimpleNoteApplication
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.extra.ToastUtil
import org.eviyttehsarch.note.extra.limitContent
import org.eviyttehsarch.note.ui.BasicCompose
import java.util.Date
import java.util.Locale

@Composable
fun NotesColumn(
    noteList: List<NoteEntity>,
    style: StyleValue,
    dateFormat: String,
    onClick: (NoteEntity) -> Unit,
    onDeleteNote: (NoteEntity) -> Unit
) {
    if (noteList.isEmpty()) {
        EmptyNoteList()
    } else {
        when (style) {
            StyleValue.Vertical -> {
                LazyColumn {
                    items(noteList) { note ->
                        var showDialog by remember { mutableStateOf(false) }
                        NoteCard(
                            note = note,
                            dateFormat = dateFormat,
                            onClick = { onClick(note) },
                            onLongPress = { showDialog = true }
                        )
                        DeleteWaringAlertDialog(
                            showDialog = showDialog,
                            onConfirm = {
                                onDeleteNote(note)
                                ToastUtil.forceShowToast(SimpleNoteApplication.Context.getString(R.string.delete_succeed))
                                showDialog = false
                            },
                            onDismiss = { showDialog = false }
                        )
                    }
                }
            }
            StyleValue.StaggeredGrid -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(noteList) { note ->
                        var showDialog by remember { mutableStateOf(false) }
                        NoteCard(
                            note = note,
                            dateFormat = dateFormat,
                            onClick = { onClick(note) },
                            onLongPress = { showDialog = true }
                        )
                        DeleteWaringAlertDialog(
                            showDialog = showDialog,
                            onConfirm = {
                                onDeleteNote(note)
                                ToastUtil.forceShowToast(SimpleNoteApplication.Context.getString(R.string.delete_succeed))
                                showDialog = false
                            },
                            onDismiss = { showDialog = false }
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
    dateFormat: String,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClick()
                    },
                    onLongPress = {
                        onLongPress()
                    }
                )
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
            val formattedDate = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date(note.modifiedDate))
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
fun DeleteWaringAlertDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicCompose.WaringAlertDialog(
        visible = showDialog,
        title = SimpleNoteApplication.Context.getString(R.string.delete_this_note),
        text = SimpleNoteApplication.Context.getString(R.string.it_will_lose_forever),
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
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
        dateFormat = DateFormatValue.Ordinary.toString(),
        onClick = { },
        onLongPress = { }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyNoteList() {
    EmptyNoteList()
}