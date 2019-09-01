package irinabilc.bachelorthesis.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "text_entries")
data class TextEntry(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "language") var language: String
)