package top.eviarch.simplenote.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import top.eviarch.simplenote.core.SimpleNoteApplication

private const val DB_NAME = "writer_database"
private const val TABLE_NAME = "NoteEntity"

@Database(entities = [NoteEntity::class], version = 2, exportSchema = false)
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
                DB_NAME
            )
                .addMigrations(MIGRATION_1_2)
                .build()
                .also {
                    Instance = it
                }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN lock INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}