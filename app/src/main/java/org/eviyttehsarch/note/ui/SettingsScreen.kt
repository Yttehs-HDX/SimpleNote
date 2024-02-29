package org.eviyttehsarch.note.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        ArrangeMode()
        FontSize()
        SortMode()
    }
    BackHandler(onBack = onBack)
}

@Composable
fun ArrangeMode() {
    var isChange by remember { mutableStateOf(false) }
    val value = if (isChange){
        "瀑布流"
    }else{
        "竖栏"
    }
    SettingsUnit(
        key = "排列模式",
        value = value,
        menuItemList = { onClose ->
            MenuItem(
                text = "竖栏",
                onClick = {
                    onClose()
                    isChange = false
                }
            )
            MenuItem(
                text = "瀑布流",
                onClick = {
                    onClose()
                    isChange = true
                }
            )
        }
    )
}


@Composable
fun FontSize() {
    var fontSize by remember { mutableIntStateOf(10) }
    val num = 10
    SettingsUnit(
        key = "字体大小",
        value = fontSize.toString(),
        menuItemList = {onClose ->
            for (i in 0..20 step 2){
                MenuItem(
                    text = (num + i).toString(),
                    onClick = {
                        onClose()
                        fontSize = num + i
                    }
                )
            }
        }
    )
}

@Composable
fun SortMode() {
    var sortMode by remember { mutableStateOf("按时间顺序") }
    SettingsUnit(
        key = "排序方式",
        value = sortMode,
        menuItemList = {onClose ->
            MenuItem(
                text = "按时间顺序",
                onClick = {
                    onClose()
                    sortMode = "按时间顺序"
                }
            )
            MenuItem(
                text = "按标题顺序",
                onClick = {
                    onClose()
                    sortMode = "按标题顺序"
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
            .clickable {
                onClick()
            },
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
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