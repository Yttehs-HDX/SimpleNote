package top.eviarch.simplenote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import top.eviarch.simplenote.data.AppDatabase
import top.eviarch.simplenote.data.NoteEntity
import top.eviarch.simplenote.extra.UUIDUtil

class MainViewModel(database: AppDatabase) : ViewModel() {
    private val noteDao = database.noteDao()

    val noteListFlow = getAllNotes()

    private val _targetDestination = MutableStateFlow(AppDestination.NotesColumnDestination.route)
    val targetDestination: StateFlow<String>
        get() = _targetDestination

    private val _targetNote = MutableStateFlow(NoteEntity(id = UUIDUtil.generateUniqueId()))
    val targetNote: StateFlow<NoteEntity>
        get() = _targetNote

    private val _showAutoDeleteDialog = MutableStateFlow(true)

    val showAutoDeleteDialog: StateFlow<Boolean>
        get() = _showAutoDeleteDialog

    private val _url = MutableStateFlow("")

    val url: StateFlow<String>
        get() = _url

    fun updateDestination(destination: String) {
        _targetDestination.value = destination
    }

    fun updateNote(note: NoteEntity) {
        updateTargetNote(note)
        viewModelScope.launch {
            noteDao.insertOrUpdate(note)
        }
    }

    fun updateTargetNote(note: NoteEntity) {
        _targetNote.value = note
    }

    fun clearTargetNote() {
        _targetNote.value = NoteEntity(id = UUIDUtil.generateUniqueId())
    }

    fun updateAutoDeleteDialogVisibility(visible: Boolean) {
        _showAutoDeleteDialog.value = visible
    }

    fun updateUrl(url: String) {
        _url.value = url
    }

    fun deleteNote(note: NoteEntity) {
        clearTargetNote()
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    private fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }
}