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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.eviarch.simplenote.MainViewModel
import top.eviarch.simplenote.R
import top.eviarch.simplenote.core.SimpleNoteApplication

@Composable
fun EditNote(
    viewModel: MainViewModel,
    isReadOnly: Boolean,
    onBack: () -> Unit
) {
    val note by viewModel.targetNote.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextField(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth(),
            value = note.title,
            readOnly = isReadOnly,
            textStyle = MaterialTheme.typography.headlineSmall,
            maxLines = 2,
            onValueChange = { title ->
                viewModel.updateNote(note.copy(
                    title = title,
                    modifiedDate = System.currentTimeMillis()
                ))
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
                .fillMaxWidth()
                .weight(1f),
            value = note.content,
            readOnly = isReadOnly,
            onValueChange = { content ->
                viewModel.updateNote(note.copy(
                    content = content,
                    modifiedDate = System.currentTimeMillis()
                ))
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
            onBack()
        }
    )
}