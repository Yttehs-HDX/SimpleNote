package top.eviarch.simplenote.ui.screen

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
import top.eviarch.simplenote.DateFormatValue
import top.eviarch.simplenote.R
import top.eviarch.simplenote.SettingsItem
import top.eviarch.simplenote.SettingsViewModel
import top.eviarch.simplenote.StyleValue
import top.eviarch.simplenote.core.SimpleNoteApplication

@Composable
fun Settings(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.main_page))
        StyleMode(viewModel = viewModel)
        HorizontalDivider()
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.note_card))
        DateFormatMode(viewModel = viewModel)
        HorizontalDivider()
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.floating_button))
        VerticalPositionMode(viewModel = viewModel)
        HorizontalPositionMode(viewModel = viewModel)
        HorizontalDivider()
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.about))
        AboutUnit(key = SimpleNoteApplication.Context.getString(R.string.version), value = SimpleNoteApplication.Context.getString(R.string.version_number), icon = Icons.TwoTone.Info)
        AboutUnit(key = SimpleNoteApplication.Context.getString(R.string.collaborator), value = SimpleNoteApplication.Context.getString(R.string.collaborator_one), icon = Icons.TwoTone.Person)
        AboutUnit(key = SimpleNoteApplication.Context.getString(R.string.collaborator), value = SimpleNoteApplication.Context.getString(R.string.collaborator_two), icon = Icons.TwoTone.Person)
        AboutUnit(key = SimpleNoteApplication.Context.getString(R.string.develop_tool), value = SimpleNoteApplication.Context.getString(R.string.develop_tool_name), icon = Icons.TwoTone.Build)
    }
    BackHandler(onBack = onBack)
}

@Composable
fun StyleMode(viewModel: SettingsViewModel) {
    val value by viewModel.style.collectAsState()
    SettingsUnit(
        key = SimpleNoteApplication.Context.getString(R.string.style),
        value = value.toUiState(),
        menuItemList = { onClose ->
            for (optionValue in StyleValue.entries) {
                MenuItem(
                    text = optionValue.toUiState(),
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
    val value by viewModel.dateFormat.collectAsState()
    SettingsUnit(
        key = SimpleNoteApplication.Context.getString(R.string.date_format),
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
fun VerticalPositionMode(viewModel: SettingsViewModel) {
    val value by viewModel.verticalPosition.collectAsState()
    SettingsUnit(
        key = SimpleNoteApplication.Context.getString(R.string.vertical_position),
        value = if (value == SettingsItem.VerticalPosition.defaultValue)
            SimpleNoteApplication.Context.getString(R.string.default_text)
        else
            SimpleNoteApplication.Context.getString(R.string.custom),
        menuItemList = { onClose ->
            MenuItem(
                text = SimpleNoteApplication.Context.getString(R.string.default_text),
                onClick = {
                    viewModel.saveVerticalPositionData(SettingsItem.VerticalPosition.defaultValue)
                    onClose()
                }
            )
        }
    )
}

@Composable
fun HorizontalPositionMode(viewModel: SettingsViewModel) {
    val value by viewModel.horizontalPosition.collectAsState()
    SettingsUnit(
        key = SimpleNoteApplication.Context.getString(R.string.horizontal_position),
        value = if (value == SettingsItem.HorizontalPosition.defaultValue)
            SimpleNoteApplication.Context.getString(R.string.default_text)
        else
            SimpleNoteApplication.Context.getString(R.string.custom),
        menuItemList = { onClose ->
            MenuItem(
                text = SimpleNoteApplication.Context.getString(R.string.default_text),
                onClick = {
                    viewModel.saveHorizontalPositionData(SettingsItem.HorizontalPosition.defaultValue)
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