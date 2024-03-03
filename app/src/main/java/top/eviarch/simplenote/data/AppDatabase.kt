package top.eviarch.simplenote.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import top.eviarch.simplenote.core.SimpleNoteApplication

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private lateinit var Instance: AppDatabase

        fun getDatabase() = if (Companion::Instance.isInitialized) Instance
        else synchronized(this) {
            Room.databaseBuilder(
                SimpleNoteApplication.Context,
                AppDatabase::class.java,
                "writer_database"
            )
                .build()
                .also {
                    Instance = it
                }
        }
    }
}