package org.eviyttehsarch.note.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.eviyttehsarch.note.data.NoteEntity

@Composable
fun NoteEditScreen(
    note: NoteEntity,
    onDone: (NoteEntity) -> Unit
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
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    onDone(NoteEntity(note.id, title, content, modifiedDate))
                },
                label = {
                    Text(
                        text = "Title"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    onDone(NoteEntity(note.id, title, content, modifiedDate))
                },
                label = {
                    Text(
                        text = "Content"
                    )
                },
                modifier = Modifier.fillMaxSize(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onDone(NoteEntity(note.id, title, content, modifiedDate))
                    }
                )
            )
        }
    }
}