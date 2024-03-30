package top.eviarch.simplenote.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(folder: FolderEntity)

    @Query("SELECT * FROM FolderEntity ORDER BY name ASC")
    fun getAllFolders(): Flow<List<FolderEntity>>

    @Delete
    suspend fun deleteFolder(folder: FolderEntity)
}