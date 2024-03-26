package top.eviarch.simplenote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

const val EmptyId = 0L

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = EmptyId,

    val title: String = "",

    val content: String = "",

    val modifiedDate: Long = System.currentTimeMillis(),

    val lock: Boolean = false
)