package top.eviarch.simplenote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FolderEntity(
    @PrimaryKey
    val id: Long = 0L,

    val name: String = ""
)