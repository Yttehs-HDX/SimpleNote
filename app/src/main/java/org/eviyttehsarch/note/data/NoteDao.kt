package org.eviyttehsarch.note.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 如果主键冲突，则替换
    suspend fun insertOrUpdate(note: NoteEntity)

    @Query("SELECT * FROM NoteEntity WHERE id = :id LIMIT 1")
    fun getNoteById(id: Long): Flow<NoteEntity>

    @Query("SELECT * FROM NoteEntity ORDER BY modifiedDate DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("DELETE FROM NoteEntity WHERE id = :id")
    suspend fun deleteNoteById(id: Long)
}