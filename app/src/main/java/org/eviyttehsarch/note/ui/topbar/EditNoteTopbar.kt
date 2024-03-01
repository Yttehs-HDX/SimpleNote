package org.eviyttehsarch.note.ui.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.eviyttehsarch.note.data.NoteEntity
import org.eviyttehsarch.note.extra.ToastUtil
import org.eviyttehsarch.note.ui.BasicCompose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteTopBar(
    visible: Boolean,
    note: NoteEntity,
    onSaveNote: (NoteEntity) -> Unit,
    onDeleteNote: (NoteEntity) -> Unit,
    onBack: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            )
        )
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        TopAppBar(
            colors = BasicCompose.topAppBarColors(),
            title = {
                Text(
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        ToastUtil.showToast("Try to write something ^_^")
                    },
                    text = "Edit Note",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (note.title != "" || note.content != "") {
                            onSaveNote(note)
                        }
                        onBack()
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                var showDialog by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { onSaveNote(note) }
                ) {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Save"
                    )
                }
                IconButton(
                    onClick = { showDialog = true }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
                DeleteWaringAlertDialog(
                    showDialog = showDialog,
                    onConfirm = {
                        showDialog = false
                        onDeleteNote(note)
                        ToastUtil.forceShowToast("Delete succeed")
                        onBack()
                    },
                    onDismiss = { showDialog = false }
                )
            }
        )
    }
}

@Composable
fun DeleteWaringAlertDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicCompose.WaringAlertDialog(
        visible = showDialog,
        title = "Delete this note",
        text = "It will lose forever",
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}