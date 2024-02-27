package org.eviyttehsarch.note.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        ArrangeMode()
        FontSize()
        SortMode()
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        About()
    }
}

@Composable
fun ArrangeMode(){
    var isChange by remember { mutableStateOf(false/*Change to sharedPreference*/) }
    val choice = if (isChange){
        "瀑布流"
    }else{
        "竖栏"
    }
    SettingsUnit(
        key = "排列模式",
        choice = choice,
        onClick = {

        },
        menuItemColumn = {
            MenuItem(
                text = "竖栏",
                onClick = {
                    isChange = false
                }
            )
            MenuItem(
                text = "瀑布流",
                onClick = {
                    isChange = true
                }
            )
        }
    )
}

@Composable
fun FontSize(){
    var fontSize by remember { mutableIntStateOf(12) }
    SettingsUnit(
        key = "字体大小",
        choice = fontSize.toString(),
        onClick = {

        },
        menuItemColumn = {
            repeat(8){
                val num = 10
                MenuItem(
                    text = (num + it*2).toString(),
                    onClick = {
                        fontSize = num + it*2
                    }
                )
            }
        }
    )
}

@Composable
fun SortMode(){
    var sortMode by remember { mutableStateOf("按时间顺序") }
    SettingsUnit(
        key = "排序方式",
        choice = sortMode,
        onClick = {

        },
        menuItemColumn = {
            MenuItem(
                text = "按时间顺序",
                onClick = {
                    sortMode = "按时间顺序"
                }
            )
            MenuItem(
                text = "按标题顺序",
                onClick = {
                    sortMode = "按标题顺序"
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun About(){
    Column {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = "   About",
            fontSize = 25.sp
        )
        Column {
            Text(
                fontSize = 20.sp,
                text = "  Developers"
            )
            Text(
                fontSize = 15.sp,
                text = "developer 1"
            )
            Text(
                fontSize = 15.sp,
                text = "developer 2"
            )
        }

    }
}

@Composable
fun SettingsUnit(
    key: String,
    choice: String,
    onClick: () -> Unit,
    menuItemColumn: @Composable ColumnScope.() -> Unit
) {
    var expand by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
        .clickable {
            expand = true
            onClick()
        }
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically),
            text = key
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = choice
            )
            DropdownMenu(
                modifier = Modifier.align(Alignment.Center),
                expanded = expand,
                onDismissRequest = { expand = false },
                content = menuItemColumn
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
fun PreviewSettingsItem() {
    SettingsUnit(
        key = "Option1",
        choice = "Choice1",
        onClick = { },
        menuItemColumn = {
            repeat(3) {
                MenuItem(
                    text = "Item$it",
                    onClick = { }
                )
            }
        }
    )
}