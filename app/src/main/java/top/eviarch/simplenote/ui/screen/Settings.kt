package top.eviarch.simplenote.ui.screen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import top.eviarch.simplenote.DateFormatValue
import top.eviarch.simplenote.MainViewModel
import top.eviarch.simplenote.R
import top.eviarch.simplenote.SettingsItem
import top.eviarch.simplenote.SettingsViewModel
import top.eviarch.simplenote.StyleValue
import top.eviarch.simplenote.core.SimpleNoteApplication
import top.eviarch.simplenote.data.NoteEntity
import top.eviarch.simplenote.extra.ToastUtil
import top.eviarch.simplenote.extra.isJson
import java.util.Date
import java.util.Locale

@Composable
fun Settings(
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel,
    jumpUrl: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.main_page))
        StyleMode(viewModel = settingsViewModel)

        HorizontalDivider()
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.note_card))
        DateFormatMode(viewModel = settingsViewModel)

        HorizontalDivider()
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.storage_manager))
        StorageManagerMode(
            mainViewModel = mainViewModel,
            settingsViewModel = settingsViewModel
        )
        ExportDataMode(viewModel = mainViewModel)
        ImportDataMode(viewModel = mainViewModel)

        HorizontalDivider()
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.floating_button))
        VerticalPositionMode(viewModel = settingsViewModel)
        HorizontalPositionMode(viewModel = settingsViewModel)

        HorizontalDivider()
        SubSettingsTitle(title = SimpleNoteApplication.Context.getString(R.string.about))
        AboutUnit(
            key = SimpleNoteApplication.Context.getString(R.string.version),
            value = SimpleNoteApplication.Context.getString(R.string.version_number),
            icon = Icons.TwoTone.Info,
            onClick = {
                jumpUrl("https://github.com/Yttehs-HDX/Simple-Note")
            }
        )
        AboutUnit(
            key = SimpleNoteApplication.Context.getString(R.string.collaborator),
            value = SimpleNoteApplication.Context.getString(R.string.collaborator_one),
            icon = Icons.TwoTone.Person,
            onClick = {
                jumpUrl("https://github.com/eviarch666")
            }
        )
        AboutUnit(
            key = SimpleNoteApplication.Context.getString(R.string.collaborator),
            value = SimpleNoteApplication.Context.getString(R.string.collaborator_two),
            icon = Icons.TwoTone.Person,
            onClick = {
                jumpUrl("https://github.com/Yttehs-HDX")
            }
        )
        AboutUnit(
            key = SimpleNoteApplication.Context.getString(R.string.develop_tool),
            value = SimpleNoteApplication.Context.getString(R.string.develop_tool_name),
            icon = Icons.TwoTone.Build
        )
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
fun StorageManagerMode(
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel
) {
    val value by settingsViewModel.autoDeleteDate.collectAsState()
    SettingsUnit(
        key = SimpleNoteApplication.Context.getString(R.string.auto_delete_date),
        value = value.toUiState(),
        menuItemList = { onClose ->
            for (optionValue in SettingsItem.StorageManager.StorageManagerValue.entries) {
                MenuItem(
                    text = optionValue.toUiState(),
                    onClick = {
                        settingsViewModel.saveAutoDeleteDateData(optionValue)
                        mainViewModel.updateAutoDeleteDialogVisibility(true)
                        onClose()
                    }
                )
            }
        }
    )
}

@Composable
fun ExportDataMode(
    viewModel: MainViewModel
) {
    var value by remember { mutableStateOf(SimpleNoteApplication.Context.getString(R.string.to_external_storage)) }
    val allNote by viewModel.noteListFlow.collectAsState(emptyList())
    val exportJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            val gson = Gson()
            val jsonString = gson.toJson(allNote)
            val context = SimpleNoteApplication.Context
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            }
        }
    }
    val clipboardManager = LocalClipboardManager.current
    DataTransportUnit(
        key = SimpleNoteApplication.Context.getString(R.string.export_data),
        value = value,
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        menuItemList = { onClose ->
            MenuItem(
                text = SimpleNoteApplication.Context.getString(R.string.to_external_storage),
                onClick = {
                    value = SimpleNoteApplication.Context.getString(R.string.to_external_storage)
                    onClose()
                }
            )
            MenuItem(
                text = SimpleNoteApplication.Context.getString(R.string.to_clipboard),
                onClick = {
                    value = SimpleNoteApplication.Context.getString(R.string.to_clipboard)
                    onClose()
                }
            )
        },
        onClick = {
            if (value == SimpleNoteApplication.Context.getString(R.string.to_external_storage)){
                val formattedDate = SimpleDateFormat(DateFormatValue.Complex.toString(), Locale.ENGLISH).format(Date(System.currentTimeMillis()))
                val fileName = "Simple-Note-${formattedDate}.json"
                exportJsonLauncher.launch(fileName)
            } else if (value == SimpleNoteApplication.Context.getString(R.string.to_clipboard)) {
                val gson = Gson()
                val jsonString = gson.toJson(allNote)
                ToastUtil.showToast(SimpleNoteApplication.Context.getString(R.string.export_to_clipboard_mes))
                clipboardManager.setText(AnnotatedString(jsonString))
            }
        }
    )
}

@Composable
fun ImportDataMode(
    viewModel: MainViewModel
) {
    var clipboardText by remember { mutableStateOf("") }
    val selectJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        val data = result.data ?: return@rememberLauncherForActivityResult
        val uri = data.data ?: return@rememberLauncherForActivityResult
        val context = SimpleNoteApplication.Context
        val inputStream = context.contentResolver.openInputStream(uri)
        val jsonString = inputStream?.bufferedReader().use { it?.readText() }
        jsonString?.let {
            val gson = Gson()
            val noteList: List<NoteEntity> =
                gson.fromJson(jsonString, object : TypeToken<List<NoteEntity>>() {}.type)
            noteList.forEach { note ->
                viewModel.updateNote(note)
            }
            viewModel.clearTargetNote()
            ToastUtil.showToast(
                "${SimpleNoteApplication.Context.getString(R.string.import_message_head)} ${noteList.size} ${
                    SimpleNoteApplication.Context.getString(
                        R.string.import_message_tail
                    )
                }"
            )
        }
    }
    val clipboardManager = LocalClipboardManager.current
    var value by remember { mutableStateOf(SimpleNoteApplication.Context.getString(R.string.from_external_storage)) }
    DataTransportUnit(
        key = SimpleNoteApplication.Context.getString(R.string.import_data),
        value = value,
        icon = Icons.AutoMirrored.Filled.ArrowBack,
        menuItemList = { onClose ->
            MenuItem(
                text = SimpleNoteApplication.Context.getString(R.string.from_external_storage),
                onClick = {
                    value = SimpleNoteApplication.Context.getString(R.string.from_external_storage)
                    onClose()
                }
            )
            MenuItem(
                text = SimpleNoteApplication.Context.getString(R.string.from_clipboard),
                onClick = {
                    value = SimpleNoteApplication.Context.getString(R.string.from_clipboard)
                    onClose()
                }
            )
        },
        onClick = {
            if (value == SimpleNoteApplication.Context.getString(R.string.from_external_storage)){
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/json"
                selectJsonLauncher.launch(intent)
            } else if (value == SimpleNoteApplication.Context.getString(R.string.from_clipboard)) {
                clipboardManager.getText()?.let {
                    clipboardText = it.text
                }
                if (clipboardText.isJson()) {
                    val gson = Gson()
                    val noteList: List<NoteEntity> = gson.fromJson(
                        clipboardText,
                        object : TypeToken<List<NoteEntity>>() {}.type
                    )
                    noteList.forEach { note ->
                        viewModel.updateNote(note)
                    }
                    viewModel.clearTargetNote()
                    ToastUtil.showToast(
                        "${SimpleNoteApplication.Context.getString(R.string.import_message_head)} ${noteList.size} ${
                            SimpleNoteApplication.Context.getString(
                                R.string.import_message_tail
                            )
                        }"
                    )
                } else {
                    ToastUtil.showToast(SimpleNoteApplication.Context.getString(R.string.invalid_json_string))
                }
            }
        }
    )
}

@Composable
fun DataTransportUnit(
    modifier: Modifier = Modifier,
    key: String,
    value: String,
    icon: ImageVector,
    expandIcon: ImageVector = Icons.TwoTone.KeyboardArrowUp,
    inExpandIcon: ImageVector = Icons.TwoTone.KeyboardArrowDown,
    menuItemList: @Composable (onClose: () -> Unit) -> Unit,
    onClick: () -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }
    Row (
      modifier = modifier.clickable { /* Click nothing, just for animation here*/ }
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
                        color = Color.Transparent,
                        shape = RoundedCornerShape(90f)
                    ),
                imageVector =
                if (isExpand) expandIcon
                else inExpandIcon,
                contentDescription = "Expand"
            )
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        shape = RoundedCornerShape(90f)
                    ),
                imageVector = icon,
                contentDescription = "TransportIcon"
            )
        }
    }
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
    modifier: Modifier = Modifier,
    key: String,
    value: String,
    expandIcon: ImageVector = Icons.TwoTone.KeyboardArrowUp,
    inExpandIcon: ImageVector = Icons.TwoTone.KeyboardArrowDown,
    menuItemList: @Composable (onClose: () -> Unit) -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }
    Row(
        // Click nothing, just for animation here
        modifier = modifier.clickable { /* Click nothing */ }
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
                if (isExpand) expandIcon
                else inExpandIcon,
                contentDescription = "Expand"
            )
        }
    }
}

@Composable
fun AboutUnit(
    key: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit = { }
) {
    Row(
        modifier = Modifier.clickable { onClick() }
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