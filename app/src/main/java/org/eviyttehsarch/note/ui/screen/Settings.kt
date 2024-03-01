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
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.KeyboardArrowDown
import androidx.compose.material.icons.twotone.KeyboardArrowUp
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eviyttehsarch.note.DateFormatValue
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
        SubSettingsTitle(title = "Main Page")
        StyleMode(viewModel = viewModel)
        HorizontalDivider()
        SubSettingsTitle(title = "Note Card")
        DateFormatMode(viewModel = viewModel)
        HorizontalDivider()
        SubSettingsTitle(title = "Floating Button")
        LocationMode(viewModel = viewModel)
        HorizontalDivider()
        SubSettingsTitle(title = "About")
        AboutUnit(key = "Version", value = "v1.0", icon = Icons.TwoTone.Info)
        AboutUnit(key = "Author 1", value = "Yttehs", icon = Icons.TwoTone.Person)
        AboutUnit(key = "Author 2", value = "Eviarch", icon = Icons.TwoTone.Person)
        AboutUnit(key = "Dev Tool", value = "Android Studio", icon = Icons.TwoTone.Build)
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
            for (optionValue in StyleValue.entries) {
                MenuItem(
                    text = optionValue.toString(),
                    onClick = {
                        viewModel.saveStyleData(optionValue)
                        onClose()
                    }
                )
            }
        }
    )
}

@Composable
fun DateFormatMode(viewModel: SettingsViewModel) {
    val key = SettingsItem.DateFormat.key
    val value by viewModel.dateFormat.collectAsState()
    SettingsUnit(
        key = key,
        value = value.toUiState(),
        menuItemList = { onClose ->
            for (optionValue in DateFormatValue.entries) {
                MenuItem(
                    text = optionValue.toUiState(),
                    onClick = {
                        viewModel.saveDateFormatData(optionValue)
                        onClose()
                    }
                )
            }
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
fun SubSettingsTitle(title: String) {
    Row(
        // Click nothing, just for animation here
        modifier = Modifier.clickable { /* Click nothing */ }
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .align(Alignment.CenterVertically),
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.weight(1f))
    }
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
            text = key,
            fontWeight = FontWeight.Bold
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
                if (isExpand) Icons.TwoTone.KeyboardArrowUp
                else Icons.TwoTone.KeyboardArrowDown,
                contentDescription = "Expand"
            )
        }
    }
}

@Composable
fun AboutUnit(
    key: String,
    value: String,
    icon: ImageVector
) {
    Row(
        // Click nothing, just for animation here
        modifier = Modifier.clickable { /* Click nothing */ }
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically),
            text = key,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically),
            text = value,
        )
        Box(
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center),
                imageVector = icon,
                contentDescription = key
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