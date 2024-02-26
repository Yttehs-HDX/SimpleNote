package org.eviyttehsarch.note.data

interface DataContainer {
    val database: AppDatabase
}

class AppDataContainer : DataContainer {
    override val database: AppDatabase by lazy {
        AppDatabase.getDatabase()
    }
}