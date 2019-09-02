package irinabilc.bachelorthesis

import androidx.room.Room
import com.google.android.gms.vision.text.TextRecognizer
import irinabilc.bachelorthesis.image.ProcessImageViewModel
import irinabilc.bachelorthesis.storage.TextDatabase
import irinabilc.bachelorthesis.storage.TextEntryController
import irinabilc.bachelorthesis.storage.TextEntryRepository
import irinabilc.bachelorthesis.storage.textentrylist.TextEntryListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { Room.databaseBuilder(
        androidApplication().applicationContext,
        TextDatabase::class.java,
        "textDatabase")
        .fallbackToDestructiveMigration()
        .build() }

    single { TextEntryRepository(get()) }
    factory { TextEntryController(get()) }
    viewModel { TextEntryListViewModel(get()) }
    viewModel { ProcessImageViewModel(get()) }
}