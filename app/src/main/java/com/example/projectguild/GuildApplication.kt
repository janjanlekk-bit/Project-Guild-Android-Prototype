package com.example.projectguild

import android.app.Application
import com.example.projectguild.data.DefaultGuildRepository
import com.example.projectguild.data.GuildRepository
import com.example.projectguild.data.storage.FileGuildStorage
import java.io.File

class GuildApplication : Application() {

    lateinit var repository: GuildRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val storageFile = File(filesDir, "guild_state.json")
        val storage = FileGuildStorage(storageFile)
        repository = DefaultGuildRepository(storage)
        instance = this
    }

    companion object {
        lateinit var instance: GuildApplication
            private set
    }
}
