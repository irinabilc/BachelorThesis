package irinabilc.bachelorthesis.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "text_entries")
data class TextEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "language") var language: String,
    @ColumnInfo(name="file") var file: String
) : Parcelable