package org.eviyttehsarch.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.eviyttehsarch.note.data.AppDatabase
import org.eviyttehsarch.note.data.NoteEntity
import java.util.UUID

class MainViewModel(database: AppDatabase) : ViewModel() {
    private val noteDao = database.noteDao()

    val noteListFlow = getAllNotes()

    private val _targetDestination = MutableStateFlow(AppDestination.NotesColumnDestination.route)
    val targetDestination: StateFlow<String>
        get() = _targetDestination

    private val _targetNote = MutableStateFlow(NoteEntity())
    val targetNote: StateFlow<NoteEntity>
        get() = _targetNote

    fun updateDestination(destination: String) {
        _targetDestination.value = destination
    }

    fun updateNote(note: NoteEntity) {
        _targetNote.value = note
    }

    fun insertOrUpdate(note: NoteEntity) {
        viewModelScope.launch {
            noteDao.insertOrUpdate(note)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    private fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    fun generateUniqueId(): Long {
        return UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
    }
}