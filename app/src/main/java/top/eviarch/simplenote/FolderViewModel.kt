package top.eviarch.simplenote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import top.eviarch.simplenote.data.AppDatabase
import top.eviarch.simplenote.data.FolderEntity

class FolderViewModel(database: AppDatabase) : ViewModel() {
    private val folderDao = database.folderDao()

    val folderListFlow = getAllFolders()

    private val _selectAllFolders = MutableStateFlow(true)
    val selectAllFolders: StateFlow<Boolean>
        get() = _selectAllFolders

    fun setSelectAllFolders(value: Boolean) {
        _selectAllFolders.value = value
    }

    fun updateFolder(folder: FolderEntity) {
        viewModelScope.launch {
            folderDao.insertOrUpdate(folder)
        }
    }

    fun deleteFolder(folder: FolderEntity) {
        viewModelScope.launch {
            folderDao.deleteFolder(folder)
        }
    }

    private fun getAllFolders(): Flow<List<FolderEntity>> {
        return folderDao.getAllFolders()
    }
}