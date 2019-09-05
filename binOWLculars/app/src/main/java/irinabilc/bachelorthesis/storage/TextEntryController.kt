package irinabilc.bachelorthesis.storage

import irinabilc.bachelorthesis.model.TextEntry

class TextEntryController(private val repository: TextEntryRepository) {

    suspend fun addEntry(name: String, language: String, text: String, file: String) =
        repository.addTextEntry(TextEntry(0, name, text, language, file))

    suspend fun listEntries() = repository.getAllTextEntries()
}