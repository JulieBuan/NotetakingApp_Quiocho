package ph.edu.comteq.roomdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import androidx.room.util.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao: NoteDao = AppDatabase.getDatabase(application).noteDao()

    private val _searchQuery = MutableStateFlow(value = "")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val allNotes: Flow<List<Note>> = searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            noteDao.getAllNotes()  // Show everything
        } else {
            noteDao.searchNotes(query)  // Show only matches
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun insert(note: Note) = viewModelScope.launch{
        noteDao.insertNote(note)
    }

    fun update(note: Note) = viewModelScope.launch{
        noteDao.updateNote(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        noteDao.deleteNote(note)
    }

    val allNotesWithTags: Flow<List<NoteWithTags>> = noteDao.getAllNotesWithTags()


    fun getNoteWithTags(noteId: Int): NoteWithTags? {
        return noteDao.getNoteWithTags(noteId)
    }


}