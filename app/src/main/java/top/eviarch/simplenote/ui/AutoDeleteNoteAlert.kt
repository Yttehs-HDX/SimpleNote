package top.eviarch.simplenote.ui

import androidx.compose.runtime.Composable
import top.eviarch.simplenote.R
import top.eviarch.simplenote.core.SimpleNoteApplication
import top.eviarch.simplenote.data.NoteEntity

@Composable
fun AutoDeleteNoteAlert(
    showDialog: Boolean,
    deletingNotes: List<NoteEntity>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val text = buildString {
        for (title in deletingNotes.map { it.title }) {
            append(title)
            append("\n")
        }
    }
    BasicCompose.WaringAlertDialog(
        visible = showDialog,
        title = SimpleNoteApplication.Context.getString(R.string.auto_delete_note_title),
        text = text,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}