package org.eviyttehsarch.note.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 如果主键冲突，则替换
    suspend fun insertOrUpdate(note: NoteEntity)

    @Query("SELECT * FROM NoteEntity ORDER BY modifiedDate DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}