package org.eviyttehsarch.note.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        About()
    }
}

@Composable
fun ArrangeMode(){
    var isChange by remember { mutableStateOf(false/*Change to sharedPreference*/) }
    var expand by remember { mutableStateOf(false) }
    val choice = if (isChange){
        "瀑布流"
    }else{
        "竖栏"
    }
    SettingsUnit(
        key = "排列模式",
        onClick = {
            expand = true
        },
        boxContent = {
            Row (
                modifier = Modifier
                    .align(Alignment.Center),
            ){
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
                Text(
                    text = choice
                )
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = null)
            }
            DropdownMenu(
                modifier = Modifier
                    .clickable {
                        expand = false
                    }
                    .align(Alignment.Center),
                expanded = expand,
                onDismissRequest = { expand = false },
            ){
                MenuItem(
                    text = "竖栏",
                    onClick = {
                        expand = false
                        isChange = false
                    }
                )
                MenuItem(
                    text = "瀑布流",
                    onClick = {
                        expand = false
                        isChange = true
                    }
                )
            }
        }
    )
}

@Composable
fun FontSize(){
    var fontSize by remember { mutableIntStateOf(12) }
    var expand by remember { mutableStateOf(false) }
    SettingsUnit(
        key = "字体大小",
        onClick = {
            expand = true
        },
        boxContent = {
            Row (
                modifier = Modifier
                    .align(Alignment.Center),
            ){
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
                Text(
                    text = fontSize.toString()
                )
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = null)
            }
            DropdownMenu(
                modifier = Modifier
                    .clickable {
                        expand = false
                    }
                    .align(Alignment.Center),
                expanded = expand,
                onDismissRequest = { expand = false },
            ){
                repeat(8) {
                    val num = 10
                    MenuItem(
                        text = (num + it * 2).toString(),
                        onClick = {
                            expand = false
                            fontSize = num + it * 2
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun SortMode(){
    var sortMode by remember { mutableStateOf("按时间顺序") }
    var expand by remember { mutableStateOf(false) }
    SettingsUnit(
        key = "排序方式",
        onClick = {
            expand = true
        },
        boxContent = {
            Row (
                modifier = Modifier
                    .align(Alignment.Center),
            ){
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
                Text(
                    text = sortMode
                )
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = null)
            }
            DropdownMenu(
                modifier = Modifier
                    .clickable {
                        expand = false
                    }
                    .align(Alignment.Center),
                expanded = expand,
                onDismissRequest = { expand = false },
            ){
                MenuItem(
                    text = "按时间顺序",
                    onClick = {
                        expand = false
                        sortMode = "按时间顺序"
                    }
                )
                MenuItem(
                    text = "按标题顺序",
                    onClick = {
                        expand = false
                        sortMode = "按标题顺序"
                    }
                )
            }
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
    onClick: () -> Unit,
    boxContent: @Composable BoxScope.() -> Unit,
) {
    Row(
        modifier = Modifier
        .clickable {
            onClick()
        }
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically),
            fontWeight = FontWeight.Bold,
            text = key
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.padding(16.dp),
            content = boxContent
        )
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
