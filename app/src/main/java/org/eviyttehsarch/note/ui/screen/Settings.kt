package org.eviyttehsarch.note.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eviyttehsarch.note.SettingsItem
import org.eviyttehsarch.note.SettingsViewModel
import org.eviyttehsarch.note.StyleValue

@Composable
fun Settings(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        StyleMode(viewModel = viewModel)
        LocationMode(viewModel = viewModel)
    }
    BackHandler(onBack = onBack)
}

@Composable
fun StyleMode(viewModel: SettingsViewModel) {
    val key = SettingsItem.Style.key
    val value by viewModel.style.collectAsState()
    SettingsUnit(
        key = key,
        value = value.toString(),
        menuItemList = { onClose ->
            MenuItem(
                text = StyleValue.Vertical.toString(),
                onClick = {
                    viewModel.saveStyleData(StyleValue.Vertical)
                    onClose()
                }
            )
            MenuItem(
                text = StyleValue.StaggeredGrid.toString(),
                onClick = {
                    viewModel.saveStyleData(StyleValue.StaggeredGrid)
                    onClose()
                }
            )
        }
    )
}

@Composable
fun LocationMode(viewModel: SettingsViewModel) {
    val key = SettingsItem.Location.key
    val value by viewModel.location.collectAsState()
    SettingsUnit(
        key = key,
        value = if (value == SettingsItem.Location.defaultValue) "Default" else "Custom",
        menuItemList = { onClose ->
            MenuItem(
                text = "Default",
                onClick = {
                    viewModel.saveLocationData(SettingsItem.Location.defaultValue)
                    onClose()
                }
            )
        }
    )
}

@Composable
fun SettingsUnit(
    key: String,
    value: String,
    menuItemList: @Composable (onClose: () -> Unit) -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }
    Row(
        // Click nothing, just for animation here
        modifier = Modifier.clickable { /* Click nothing */ }
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically),
            fontWeight = FontWeight.Bold,
            text = key
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                text = value
            )
            DropdownMenu(
                expanded = isExpand,
                onDismissRequest = {
                    isExpand = false
                }
            ) {
                menuItemList { isExpand = false }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = { isExpand = !isExpand }
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        shape = RoundedCornerShape(90f)
                    ),
                imageVector =
                if (isExpand) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand"
            )
        }
    }
}

@Composable
fun MenuItem(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp),
            text = text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsUnit() {
    SettingsUnit(
        key = "Key",
        value = "Value",
        menuItemList = { onClose ->
            repeat(3) {
                MenuItem(
                    text = "item$it",
                    onClick = { onClose() }
                )
            }
        }
    )
}