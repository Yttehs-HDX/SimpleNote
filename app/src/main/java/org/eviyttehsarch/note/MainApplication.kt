package org.eviyttehsarch.note

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import org.eviyttehsarch.note.data.AppDataContainer
import org.eviyttehsarch.note.data.DataContainer

class MainApplication : Application() {
    lateinit var container: DataContainer

    companion object {
        val Context: Context
            get() = _Context

        @SuppressLint("StaticFieldLeak")
        private lateinit var _Context: Context
    }

    override fun onCreate() {
        super.onCreate()
        _Context = applicationContext
        container = AppDataContainer()
    }
}