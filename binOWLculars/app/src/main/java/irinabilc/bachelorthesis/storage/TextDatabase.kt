package irinabilc.bachelorthesis.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import irinabilc.bachelorthesis.model.TextEntry


@Database(entities = [TextEntry::class], version = 2)
abstract class TextDatabase : RoomDatabase() {
    abstract fun textEntryDao(): TextEntryDao

    companion object {
        private var instance: TextDatabase? = null
        fun getDatabase(context: Context): TextDatabase {
            if (instance == null) instance =
                Room.databaseBuilder(context, irinabilc.bachelorthesis.storage.TextDatabase::class.java, "db")
                    .fallbackToDestructiveMigration()
                    .build()
            return instance!!
        }
    }
}

