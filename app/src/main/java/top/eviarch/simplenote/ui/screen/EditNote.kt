package top.eviarch.simplenote.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import top.eviarch.simplenote.R
import top.eviarch.simplenote.core.SimpleNoteApplication
import top.eviarch.simplenote.data.NoteEntity

@Composable
fun EditNote(
    note: NoteEntity,
    onDone: (NoteEntity) -> Unit,
    onBack: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note.title) }
    var content by rememberSaveable { mutableStateOf(note.content) }
    var modifiedDate by rememberSaveable { mutableLongStateOf(note.modifiedDate) }
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
            placeholder = {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = SimpleNoteApplication.Context.getString(R.string.title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
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
            placeholder = {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = SimpleNoteApplication.Context.getString(R.string.content),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone(NoteEntity(note.id, title, content, modifiedDate))
                },

                ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )
    }
    BackHandler(
        onBack = {
            onDone(note)
            onBack()
        }
    )
}