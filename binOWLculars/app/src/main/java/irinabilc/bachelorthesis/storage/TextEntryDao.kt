package irinabilc.bachelorthesis.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import irinabilc.bachelorthesis.model.TextEntry

@Dao
interface TextEntryDao {

    @Insert
    fun addTextEntry(textEntry: TextEntry)

    @Delete
    fun deleteEntry(textEntry: TextEntry)

    @Query("SELECT * FROM text_entries")
    fun getAllTextEntries(): List<TextEntry>

}