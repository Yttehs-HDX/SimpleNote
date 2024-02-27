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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        repeat(3) { num ->
            SettingsUnit(
                key = "Option$num",
                onClick = {

                },
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
    }
}

@Composable
fun SettingsUnit(
    key: String,
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
                text = "TODO" //TODO()
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