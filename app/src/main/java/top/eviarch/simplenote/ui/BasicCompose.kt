package top.eviarch.simplenote.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.eviarch.simplenote.R
import top.eviarch.simplenote.core.SimpleNoteApplication

object BasicCompose {
    @Composable
    fun WaringAlertDialog(
        visible: Boolean,
        title: String,
        text: String,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        if (visible) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = title)
                },
                text = {
                    Text(text = text)
                },
                confirmButton = {
                    Text(
                        modifier = Modifier.clickable {
                            onConfirm()
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
}