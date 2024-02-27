package org.eviyttehsarch.note.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String = "",

    val content: String = "",

    val modifiedDate: Long = System.currentTimeMillis()
)