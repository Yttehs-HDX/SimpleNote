package org.eviyttehsarch.note.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.eviyttehsarch.note.R
import org.eviyttehsarch.note.core.SimpleNoteApplication

object BasicCompose {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBarColors(
        containerColors: Color = MaterialTheme.colorScheme.primaryContainer
    ) = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColors,
        scrolledContainerColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primaryContainer),
        navigationIconContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primaryContainer),
        titleContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primaryContainer),
        actionIconContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primaryContainer),
    )

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