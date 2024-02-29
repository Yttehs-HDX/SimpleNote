package org.eviyttehsarch.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.eviyttehsarch.note.data.AppDatabase
import org.eviyttehsarch.note.data.NoteEntity

class MainViewModel(database: AppDatabase) : ViewModel() {
    private val noteDao = database.noteDao()

    val noteListFlow = getAllNotes()

    var targetId by mutableLongStateOf(0)

    // 获取所有笔记数据的 Flow
    private fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    // 插入或更新笔记数据
    fun insertOrUpdate(note: NoteEntity) {
        viewModelScope.launch {
            targetId = noteDao.insertOrUpdate(note)
        }
    }

    // 删除笔记数据
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }
}