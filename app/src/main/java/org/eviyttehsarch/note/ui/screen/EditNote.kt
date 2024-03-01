package org.eviyttehsarch.note.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.eviyttehsarch.note.data.NoteEntity

@Composable
fun EditNote(
    note: NoteEntity,
    onDone: (NoteEntity) -> Unit,
    onBack: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note.title) }
    var content by rememberSaveable { mutableStateOf(note.content) }
    var modifiedDate by rememberSaveable { mutableLongStateOf(note.modifiedDate) }
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
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth(),
                value = title,
                textStyle = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                onValueChange = {
                    title = it
                    modifiedDate = System.currentTimeMillis()
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
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth(),
                value = content,
                onValueChange = {
                    content = it
                    modifiedDate = System.currentTimeMillis()
                    onDone(NoteEntity(note.id, title, content, modifiedDate))
                },
                placeholder = { Text(text = "Content") },
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