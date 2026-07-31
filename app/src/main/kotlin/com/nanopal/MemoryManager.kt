package com.nanopal

import android.content.Context
import java.io.File

class MemoryManager(private val context: Context) {
    private val memoryFile = File(context.filesDir, "nanopal_memory.txt")

    fun readMemory(): String {
        return if (memoryFile.exists()) {
            memoryFile.readText()
        } else {
            ""
        }
    }

    fun saveMemory(content: String) {
        memoryFile.writeText(content)
    }

    fun appendFact(fact: String) {
        val current = readMemory()
        saveMemory("$current\n$fact")
    }
}
