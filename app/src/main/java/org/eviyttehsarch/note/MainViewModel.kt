package org.eviyttehsarch.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.eviyttehsarch.note.data.AppDatabase
import org.eviyttehsarch.note.data.NoteEntity
import java.util.UUID

class MainViewModel(database: AppDatabase) : ViewModel() {
    private val noteDao = database.noteDao()

    val noteListFlow = getAllNotes()

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