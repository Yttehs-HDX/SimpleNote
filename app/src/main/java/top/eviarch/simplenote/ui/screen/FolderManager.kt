package top.eviarch.simplenote.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import top.eviarch.simplenote.FolderViewModel
import top.eviarch.simplenote.MainViewModel
import top.eviarch.simplenote.R
import top.eviarch.simplenote.core.SimpleNoteApplication
import top.eviarch.simplenote.data.FolderEntity
import top.eviarch.simplenote.extra.ToastUtil
import top.eviarch.simplenote.ui.FolderDialog

@Composable
fun FolderManager(
    mainViewModel: MainViewModel,
    folderViewModel: FolderViewModel,
    onBack: () -> Unit
) {
    val allFolders by folderViewModel.folderListFlow.collectAsState(initial = emptyList())
    val allNotes by mainViewModel.noteListFlow.collectAsState(initial = emptyList())
    LazyColumn {
        allFolders.forEach {  folder ->
            item {
                FolderItem(
                    folder = folder,
                    onRename = { newName ->
                        if (allFolders.none { it.name == newName }) {
                            folderViewModel.updateFolder(folder.copy(name = newName))
                        } else {
                            ToastUtil.showToast(SimpleNoteApplication.Context.getString(R.string.folder_already_exists))
                        }
                    },
                    onDelete = {
                        allNotes.forEach { note ->
                            if (note.folder == folder.name) {
                                mainViewModel.updateNote(note.copy(folder = ""))
                            }
                        }
                        folderViewModel.deleteFolder(folder)
                    }
                )
            }
        }
    }
    BackHandler {
        onBack()
    }
}

@Composable
fun FolderItem(
    folder: FolderEntity,
    onRename: (String) -> Unit,
    onDelete: () -> Unit
) {
    var deleteState by remember { mutableStateOf(false) }
    Row(
        // Click nothing, just for animation here
        modifier = Modifier
            .clickable { /* Click nothing */ }
            .padding(top = 8.dp)
    ) {
        var editState by remember { mutableStateOf(false) }
        Spacer(modifier = Modifier.width(16.dp))
        if (editState) {
            var newName by remember { mutableStateOf(folder.name) }
            TextField(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .weight(1f),
                value = newName,
                maxLines = 1,
                onValueChange = { tempString ->
                    newName = tempString
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                onClick = {
                    onRename(newName)
                    editState = false
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Done"
                )
            }
        } else {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically),
                text = folder.name,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { editState = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Rename"
                )
            }
        }
        IconButton(
            onClick = { deleteState = true }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }
    FolderDialog.DeleteFolderAlert(
        visible = deleteState,
        onConfirm = {
            onDelete()
            deleteState = false
        },
        onDismiss = {
            deleteState = false
        }
    )
}