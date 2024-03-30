package top.eviarch.simplenote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey
    val id: Long = 0L,

    val title: String = "",

    val content: String = "",

    val modifiedDate: Long = System.currentTimeMillis(),

    val lock: Boolean = false,

    val folder: String = ""
)