package org.eviyttehsarch.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.eviyttehsarch.note.data.AppDatabase
import org.eviyttehsarch.note.data.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppViewModel(private val database: AppDatabase) : ViewModel() {

    private val noteDao = database.noteDao()

    // 获取所有笔记数据的 Flow
    fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    // 根据笔记 ID 获取笔记数据
    fun getNoteById(id: Long): Flow<NoteEntity> {
        return noteDao.getNoteById(id)
    }

    // 插入或更新笔记数据
    fun insertOrUpdate(note: NoteEntity) {
        viewModelScope.launch {
            noteDao.insertOrUpdate(note)
        }
    }

    // 删除笔记数据
    suspend fun deleteNoteById(id: Long) {
        noteDao.deleteNoteById(id)
    }
}