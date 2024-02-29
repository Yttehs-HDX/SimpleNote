package org.eviyttehsarch.note.core

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.eviyttehsarch.note.MainViewModel
import org.eviyttehsarch.note.SettingsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MainViewModel(
                mainApplication().container.database
            )
        }
        initializer {
            SettingsViewModel()
        }
    }
}

fun CreationExtras.mainApplication(): SimpleNoteApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SimpleNoteApplication)