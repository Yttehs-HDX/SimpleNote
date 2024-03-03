package top.eviarch.simplenote.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import top.eviarch.simplenote.data.AppDataContainer
import top.eviarch.simplenote.data.DataContainer

class SimpleNoteApplication : Application() {
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