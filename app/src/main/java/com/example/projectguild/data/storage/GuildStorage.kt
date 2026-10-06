package com.example.projectguild.data.storage

import java.io.File
import kotlinx.serialization.json.Json

interface GuildStorage {
    fun loadState(): GuildSaveState
    fun saveState(state: GuildSaveState)
}

class InMemoryGuildStorage(
    initialState: GuildSaveState = GuildSaveState()
) : GuildStorage {
    private var state: GuildSaveState = initialState

    override fun loadState(): GuildSaveState = state

    override fun saveState(state: GuildSaveState) {
        this.state = state
    }
}

class FileGuildStorage(
    private val file: File
) : GuildStorage {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    override fun loadState(): GuildSaveState {
        return try {
            if (file.exists()) {
                val content = file.readText()
                json.decodeFromString<GuildSaveState>(content)
            } else {
                val initial = GuildSaveState()
                saveState(initial)
                initial
            }
        } catch (e: Exception) {
            val initial = GuildSaveState()
            saveState(initial)
            initial
        }
    }

    override fun saveState(state: GuildSaveState) {
        try {
            if (!file.parentFile?.exists().let { it == true }) {
                file.parentFile?.mkdirs()
            }
            val content = json.encodeToString(GuildSaveState.serializer(), state)
            file.writeText(content)
        } catch (_: Exception) {
            // Local fallback handling
        }
    }
}
