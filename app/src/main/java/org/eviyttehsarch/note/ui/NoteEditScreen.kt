package org.eviyttehsarch.note.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.eviyttehsarch.note.data.NoteEntity

@Composable
fun NoteEditScreen(
    note: NoteEntity,
    onDone: (NoteEntity) -> Unit,
    onBack: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note.title) }
    var content by rememberSaveable { mutableStateOf(note.content) }
    val modifiedDate by rememberSaveable { mutableLongStateOf(note.modifiedDate) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = title,
                textStyle = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                onValueChange = {
                    title = it
                    onDone(NoteEntity(note.id, title, content, modifiedDate))
                },
                placeholder = { Text(text = "Title") },
                keyboardActions = KeyboardActions(
                    onDone = {
                        onDone(NoteEntity(note.id, title, content, modifiedDate))
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = content,
                maxLines = 2,
                onValueChange = {
                    content = it
                    onDone(NoteEntity(note.id, title, content, modifiedDate))
                },
                placeholder = { Text(text = "Title") },
                keyboardActions = KeyboardActions(
                    onDone = {
                        onDone(NoteEntity(note.id, title, content, modifiedDate))
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )
        }
    }
    BackHandler(onBack = onBack)
}

@Composable
fun DeleteWaringAlertDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Delete this note!")
            },
            text = {
                Text(text = "It will lose forever!")
            },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}