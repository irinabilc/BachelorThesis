package irinabilc.bachelorthesis.storage

import irinabilc.bachelorthesis.model.TextEntry
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TextEntryRepository(val database: TextDatabase) {
    private val job = Job()
    private val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    val scope = CoroutineScope(coroutineContext)

    fun addTextEntry(textEntry: TextEntry) {
        scope.launch {
            withContext(Dispatchers.IO) {
                database.textEntryDao().addTextEntry(textEntry)
            }
        }
    }

    fun deleteTextEntry(textEntry: TextEntry){
        scope.launch {
            withContext(Dispatchers.IO){
                database.textEntryDao().deleteEntry(textEntry)
            }
        }
    }

    suspend fun getAllTextEntries(): List<TextEntry>{
        var entries: List<TextEntry> = emptyList()
        withContext(Dispatchers.IO){
            entries = database.textEntryDao().getAllTextEntries()
        }
        return entries
    }
}