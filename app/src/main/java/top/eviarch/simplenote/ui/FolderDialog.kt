package top.eviarch.simplenote.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import top.eviarch.simplenote.R
import top.eviarch.simplenote.core.SimpleNoteApplication
import top.eviarch.simplenote.data.FolderEntity
import top.eviarch.simplenote.data.NoteEntity

object FolderDialog {
    @Composable
    fun SetFolderDialog(
        visible: Boolean,
        note: NoteEntity,
        folders: List<FolderEntity>,
        onConfirm: (FolderEntity) -> Unit,
        onDismiss: () -> Unit
    ) {
        if (visible) {
            var selectedFolder by remember {
                mutableStateOf(folders.find { it.name == note.folder } ?: FolderEntity(name = note.folder))
            }
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = SimpleNoteApplication.Context.getString(R.string.select_folder))
                },
                text = {
                    Column {
                        folders.forEach { folder ->
                            Row {
                                RadioButton(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    selected = selectedFolder.name == folder.name,
                                    onClick = {
                                        selectedFolder = folder
                                    }
                                )
                                Text(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    text = folder.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Text(
                        modifier = Modifier.clickable {
                            onConfirm(selectedFolder)
                        },
                        text = SimpleNoteApplication.Context.getString(R.string.confirm),
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                dismissButton = {
                    Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = SimpleNoteApplication.Context.getString(R.string.dismiss),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            )
        }
    }

    @Composable
    fun DeleteFolderAlert(
        visible: Boolean,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        BasicCompose.WaringAlertDialog(
            visible = visible,
            title = "Delete Folder",
            text = "The notes will be stand alone.",
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}