package top.eviarch.simplenote.ui.screen

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.eviarch.simplenote.DateFormatValue
import top.eviarch.simplenote.R
import top.eviarch.simplenote.StyleValue
import top.eviarch.simplenote.core.SimpleNoteApplication
import top.eviarch.simplenote.data.NoteEntity
import top.eviarch.simplenote.extra.ToastUtil
import top.eviarch.simplenote.extra.limitContent
import top.eviarch.simplenote.ui.BasicCompose
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun NotesColumn(
    noteList: List<NoteEntity>,
    style: StyleValue,
    dateFormat: String,
    searchState: Boolean,
    matchedString: String,
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
                            searchState = searchState,
                            matchedString = matchedString,
                            dateFormat = dateFormat,
                            onClick = {
                                onClick(note)
                            },
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
                    columns = StaggeredGridCells.Fixed((LocalConfiguration.current.screenWidthDp.toFloat() / 200f).roundToInt()),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(noteList) { note ->
                        var showDialog by remember { mutableStateOf(false) }
                        NoteCard(
                            note = note,
                            searchState = searchState,
                            matchedString = matchedString,
                            dateFormat = dateFormat,
                            onClick = {
                                onClick(note)
                            },
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
    searchState: Boolean,
    matchedString: String,
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
            val annotatedTitle = buildAnnotatedString {
                val fullStringList = note.title.limitContent(20, ellipsis = "").split(matchedString)
                for (index in fullStringList.indices) {
                    if (index != 0) {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append(matchedString)
                        }
                    }
                    append(fullStringList[index])
                }
                if (note.content.length > 20) {
                    append("...")
                }
            }
            val title = AnnotatedString(note.title.limitContent(20))
            Text(
                text = if (searchState) annotatedTitle else title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val annotatedContent = buildAnnotatedString {
                val fullStringList = note.content.limitContent(100, ellipsis = "").split(matchedString)
                for (index in fullStringList.indices) {
                    if (index != 0) {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append(matchedString)
                        }
                    }
                    append(fullStringList[index])
                }
                if (note.content.length > 100) {
                    append("...")
                }
            }
            val content = AnnotatedString(note.content.limitContent(100))
            Text(
                text = if (searchState) annotatedContent else content,
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
        note = NoteEntity(0, "This is title", "This is content", 0),
        dateFormat = DateFormatValue.Ordinary.toString(),
        searchState = true,
        matchedString = "is",
        onClick = { },
        onLongPress = { }
    )
}