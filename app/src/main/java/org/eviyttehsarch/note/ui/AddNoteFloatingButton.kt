package org.eviyttehsarch.note.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
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
import org.eviyttehsarch.note.LocationValue
import org.eviyttehsarch.note.SettingsItem
import org.eviyttehsarch.note.SettingsViewModel
import kotlin.math.roundToInt

@Composable
fun AddNoteFloatingButton(
    visible: Boolean,
    viewModel: SettingsViewModel,
    startLocation: LocationValue,
    onClick: () -> Unit,
    onStop: (LocationValue) -> Unit
) {
    ScreenOrientationDetection(viewModel = viewModel)
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
        var position by remember { mutableStateOf(startLocation.toOffset()) }
        Box(modifier = Modifier.wrapContentSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                position += dragAmount
                            },
                            onDragEnd = {
                                onStop(position.toLocation())
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
}

@Composable
fun ScreenOrientationDetection(
    viewModel: SettingsViewModel
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    if (orientation != Configuration.ORIENTATION_LANDSCAPE){
        viewModel.saveLocationData(SettingsItem.Location.defaultValue)
    }else{
        viewModel.saveLocationData(SettingsItem.Location.defaultValue)
    }
}

fun LocationValue.toOffset() = Offset(first, second)

fun Offset.toLocation() = LocationValue(x, y)