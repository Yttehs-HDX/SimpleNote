package top.eviarch.simplenote.ui.screen

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.eviarch.simplenote.DateFormatValue
import top.eviarch.simplenote.FolderViewModel
import top.eviarch.simplenote.R
import top.eviarch.simplenote.StyleValue
import top.eviarch.simplenote.core.SimpleNoteApplication
import top.eviarch.simplenote.data.FolderEntity
import top.eviarch.simplenote.data.NoteEntity
import top.eviarch.simplenote.extra.ToastUtil
import top.eviarch.simplenote.extra.limitContent
import top.eviarch.simplenote.ui.BasicCompose
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesColumn(
    scrollBehavior: TopAppBarScrollBehavior,
    folderViewModel: FolderViewModel,
    noteList: List<NoteEntity>,
    style: StyleValue,
    dateFormat: String,
    dateLimit: Long,
    searchState: Boolean,
    matchedString: String,
    onClick: (NoteEntity) -> Unit,
    onButtonClick: (NoteEntity) -> Unit,
    onDeleteNote: (NoteEntity) -> Unit,
    onSelectFolder: (FolderEntity) -> Unit,
    onUnselectFolder: (FolderEntity) -> Unit,
    onSelectAllFolders: () -> Unit,
    onUnselectAllFolders: () -> Unit,
    onSetFolder: (NoteEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            val allFolders by folderViewModel.folderListFlow.collectAsState(initial = emptyList())
            val selectAllFolders by folderViewModel.selectAllFolders.collectAsState()
            var forceUnselect by remember { mutableStateOf(false) }
            if (!searchState) {
                ElevatedFilterChip(
                    modifier = Modifier.padding(end = 8.dp),
                    selected = selectAllFolders,
                    onClick = {
                        if (!selectAllFolders) {
                            onSelectAllFolders()
                            folderViewModel.setSelectAllFolders(true)
                        } else {
                            forceUnselect = true
                            onUnselectAllFolders()
                            folderViewModel.setSelectAllFolders(false)
                        }
                    },
                    label = {
                        Row {
                            if (selectAllFolders) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Select"
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(text = SimpleNoteApplication.Context.getString(R.string.all))
                        }
                    }
                )
                allFolders.forEach { folder ->
                    var selected by remember { mutableStateOf(false) }
                    if (selectAllFolders) { selected = true }
                    else if (forceUnselect) { selected = false }
                    ElevatedFilterChip(
                        modifier = Modifier.padding(end = 8.dp),
                        selected = selected,
                        onClick = {
                            if (selected) {
                                selected = false
                                onUnselectFolder(folder)
                                folderViewModel.setSelectAllFolders(false)
                            } else {
                                forceUnselect = false
                                selected = true
                                onSelectFolder(folder)
                            }
                        },
                        label = {
                            Row {
                                if (selected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Select"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(folder.name)
                            }
                        }
                    )
                }
            }
        }
        if (noteList.isEmpty()) {
            EmptyNoteList()
        } else {
            when (style) {
                StyleValue.Vertical -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(noteList) { note ->
                            var showDialog by remember { mutableStateOf(false) }
                            NoteCard(
                                note = note,
                                searchState = searchState,
                                matchedString = matchedString,
                                dateFormat = dateFormat,
                                dateLimit = dateLimit,
                                onClick = {
                                    onClick(note)
                                },
                                onButtonClick = {
                                    onButtonClick(note)
                                },
                                onLongPress = { showDialog = true },
                                onSetFolder = onSetFolder
                            )
                            DeleteWaringAlertDialog(
                                showDialog = showDialog,
                                onConfirm = {
                                    onDeleteNote(note)
                                    ToastUtil.forceShowToast(
                                        SimpleNoteApplication.Context.getString(
                                            R.string.delete_succeed
                                        )
                                    )
                                    showDialog = false
                                },
                                onDismiss = { showDialog = false }
                            )
                        }
                    }
                }
                StyleValue.StaggeredGrid -> {
                    LazyVerticalStaggeredGrid(
                        modifier = Modifier.fillMaxSize(),
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
                                dateLimit = dateLimit,
                                onClick = {
                                    onClick(note)
                                },
                                onButtonClick = {
                                    onButtonClick(note)
                                },
                                onLongPress = { showDialog = true },
                                onSetFolder = onSetFolder
                            )
                            DeleteWaringAlertDialog(
                                showDialog = showDialog,
                                onConfirm = {
                                    onDeleteNote(note)
                                    ToastUtil.forceShowToast(
                                        SimpleNoteApplication.Context.getString(
                                            R.string.delete_succeed
                                        )
                                    )
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
}

@Composable
fun NoteCard(
    note: NoteEntity,
    searchState: Boolean,
    matchedString: String,
    dateFormat: String,
    dateLimit: Long,
    onClick: () -> Unit,
    onButtonClick: () -> Unit,
    onLongPress: () -> Unit,
    onSetFolder: (NoteEntity) -> Unit
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
            val hidedTitle = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )) {
                    append(note.title.limitContent(5))
                }
            }
            Text(
                text = if (searchState) annotatedTitle else if (note.lock) hidedTitle else title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val annotatedContent = buildAnnotatedString {
                val fullStringList = note.content
                    .limitContent(100, ellipsis = "")
                    .split(matchedString)
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
            val hidedContent = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )) {
                    append(SimpleNoteApplication.Context.getString(R.string.locked_note_hint))
                }
            }
            Text(
                text = if (searchState) annotatedContent else if (note.lock) hidedContent else content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .heightIn(30.dp)
            )

            Row {
                if (note.modifiedDate < System.currentTimeMillis() - dateLimit) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        onLongPress()
                                    },
                                    onLongPress = {
                                        onSetFolder(note)
                                    }
                                )
                            },
                        imageVector = Icons.Default.Warning,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "Old",
                    )
                } else if (note.lock) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        onButtonClick()
                                    }
                                )
                            },
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_lock_person_24),
                        contentDescription = "Locked",
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        onClick()
                                    },
                                    onLongPress = {
                                        onSetFolder(note)
                                    }
                                )
                            },
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                val formattedDate = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date(note.modifiedDate))
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
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
        dateLimit = 0,
        searchState = true,
        matchedString = "is",
        onClick = { },
        onButtonClick = { },
        onLongPress = { },
        onSetFolder = { }
    )
}