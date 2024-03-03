package top.eviarch.simplenote

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import top.eviarch.simplenote.core.SimpleNoteApplication

class SettingsViewModel : ViewModel() {
    private val sharedPreferences = SimpleNoteApplication.Context.getSharedPreferences("settings_shared_pref", Context.MODE_PRIVATE)

    private val _style = MutableStateFlow(SettingsItem.Style.defaultValue)
    val style: StateFlow<StyleValue>
        get() = _style

    private val _verticalPosition = MutableStateFlow(SettingsItem.VerticalPosition.defaultValue)
    val verticalPosition: StateFlow<PositionValue>
        get() = _verticalPosition

    private val _horizontalPosition = MutableStateFlow(SettingsItem.HorizontalPosition.defaultValue)
    val horizontalPosition: StateFlow<PositionValue>
        get() = _horizontalPosition

    private val _dateFormat = MutableStateFlow(SettingsItem.DateFormat.defaultValue)
    val dateFormat: StateFlow<DateFormatValue>
        get() = _dateFormat

    init {
        loadStyleData(SettingsItem.Style.defaultValue)
        loadDateFormatData(SettingsItem.DateFormat.defaultValue)
        loadVerticalPositionData(SettingsItem.VerticalPosition.defaultValue)
        loadHorizontalPositionData(SettingsItem.HorizontalPosition.defaultValue)
    }

    private fun loadStyleData(defaultValue: StyleValue) {
        val key = SettingsItem.Style.key
        viewModelScope.launch {
            val stringValue = sharedPreferences.getString(key, null)
            _style.value = stringValue?.toStyleOrDefault() ?: defaultValue
        }
    }

    fun saveStyleData(value: StyleValue) {
        val key = SettingsItem.Style.key
        _style.value = value
        saveData(key, value.toString())
    }

    private fun loadDateFormatData(defaultValue: DateFormatValue) {
        val key = SettingsItem.DateFormat.key
        viewModelScope.launch {
            val stringValue = sharedPreferences.getString(key, null)
            _dateFormat.value = stringValue?.toDateFormatOrDefault() ?: defaultValue
        }
    }

    fun saveDateFormatData(value: DateFormatValue) {
        val key = SettingsItem.DateFormat.key
        _dateFormat.value = value
        saveData(key, value.toString())
    }

    private fun loadVerticalPositionData(defaultValue: PositionValue) {
        val key = SettingsItem.VerticalPosition.key
        viewModelScope.launch {
            val stringValue = sharedPreferences.getString(key, null)
            _verticalPosition.value = stringValue?.toPositionOrDefault() ?: defaultValue
        }
    }

    fun saveVerticalPositionData(value: PositionValue) {
        val key = SettingsItem.VerticalPosition.key
        _verticalPosition.value = value
        saveData(key, value.toString())
    }

    private fun loadHorizontalPositionData(defaultValue: PositionValue) {
        val key = SettingsItem.HorizontalPosition.key
        viewModelScope.launch {
            val stringValue = sharedPreferences.getString(key, null)
            _horizontalPosition.value = stringValue?.toPositionOrDefault() ?: defaultValue
        }
    }

    fun saveHorizontalPositionData(value: PositionValue) {
        val key = SettingsItem.HorizontalPosition.key
        _horizontalPosition.value = value
        saveData(key, value.toString())
    }

    private fun saveData(key: String, value: String) {
        viewModelScope.launch {
            sharedPreferences.edit {
                putString(key, value)
            }
        }
    }

    fun resetSettings() {
        saveStyleData(SettingsItem.Style.defaultValue)
        saveDateFormatData(SettingsItem.DateFormat.defaultValue)
        saveVerticalPositionData(SettingsItem.VerticalPosition.defaultValue)
        saveHorizontalPositionData(SettingsItem.HorizontalPosition.defaultValue)
    }
}