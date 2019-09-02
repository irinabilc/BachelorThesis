package irinabilc.bachelorthesis.storage.textentrylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import irinabilc.bachelorthesis.model.TextEntry
import irinabilc.bachelorthesis.storage.TextEntryController
import kotlinx.coroutines.launch

class TextEntryListViewModel(private val controller: TextEntryController): ViewModel() {

    private val _entries=MutableLiveData<List<TextEntry>>()
    val entries:LiveData<List<TextEntry>> get() = _entries

    init {
        viewModelScope.launch {
            _entries.value=controller.listEntries()
        }
    }
}