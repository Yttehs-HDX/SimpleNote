package org.eviyttehsarch.note.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import org.eviyttehsarch.note.PositionValue
import kotlin.math.roundToInt

@Composable
fun AddNoteFloatingButton(
    visible: Boolean,
    landscape: Boolean,
    verticalStartPosition: PositionValue,
    horizontalStartPosition: PositionValue,
    onClick: () -> Unit,
    onStop: (PositionValue) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 100,
                easing = LinearEasing
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 100,
                easing = LinearEasing
            )
        )
    ) {
        var offset by remember {
            mutableStateOf(
                if (landscape) horizontalStartPosition.toOffset()
                else verticalStartPosition.toOffset()
            )
        }
        val screenState = LocalConfiguration.current
        FloatingActionButton(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = offset.x.roundToInt(),
                        y = offset.y.roundToInt()
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offset += dragAmount
                            offset = Offset(
                                x = offset.x.coerceIn(-2 * screenState.screenWidthDp.toFloat(), 0f),
                                y = offset.y.coerceIn(-2 * screenState.screenHeightDp.toFloat(), 0f),
                            )
                        },
                        onDragEnd = {
                            onStop(offset.toPosition())
                        }
                    )
                },
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                tint = MaterialTheme.colorScheme.contentColorFor(FloatingActionButtonDefaults.containerColor),
                contentDescription = "Edit"
            )
        }
    }
}

fun PositionValue.toOffset() = Offset(first, second)

fun Offset.toPosition() = PositionValue(x, y)