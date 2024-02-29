package org.eviyttehsarch.note

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.eviyttehsarch.note.core.SimpleNoteApplication

class SettingsViewModel : ViewModel() {
    private val sharedPreferences = SimpleNoteApplication.Context.getSharedPreferences("settings_shared_pref", Context.MODE_PRIVATE)

    private val _style = MutableStateFlow(SettingsItem.Style.defaultValue)
    val style: StateFlow<SettingsItem.Style.Value>
        get() = _style

    init {
        loadStyleData(SettingsItem.Style.key, SettingsItem.Style.defaultValue)
    }

    private fun loadStyleData(key: String, defaultValue: SettingsItem.Style.Value) {
        viewModelScope.launch {
            val stringValue = sharedPreferences.getString(key, null)
            _style.value = stringValue?.toStyleOrDefault() ?: defaultValue
        }
    }

    fun saveStyleData(value: SettingsItem.Style.Value) {
        val key = SettingsItem.Style.key
        _style.value = value
        saveData(key, value.toString())
    }

    private fun saveData(key: String, value: String) {
        viewModelScope.launch {
            sharedPreferences.edit {
                putString(key, value)
            }
        }
    }
}