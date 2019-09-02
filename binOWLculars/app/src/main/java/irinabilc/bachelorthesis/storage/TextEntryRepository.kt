package irinabilc.bachelorthesis.storage

import irinabilc.bachelorthesis.model.TextEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TextEntryRepository(val database: TextDatabase) {

    suspend fun addTextEntry(textEntry: TextEntry) =
        withContext(Dispatchers.IO) {
            database.textEntryDao().addTextEntry(textEntry)
        }

    suspend fun getAllTextEntries() =
        withContext(Dispatchers.IO) {
            database.textEntryDao().getAllTextEntries()
        }
}