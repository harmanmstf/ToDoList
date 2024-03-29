package com.example.todolist.ui.list

import androidx.lifecycle.*
import com.example.todolist.data.Item
import com.example.todolist.data.ItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class ToDoListViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()
    val completedItems : LiveData<List<Item>> = itemDao.getCompletedItems().asLiveData()


    fun updateItem(
        itemId: Int,
        itemTask: String,
        isCompleted: Boolean

        ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemTask, isCompleted)
        updateItem(updatedItem)
    }


     fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }


    fun addNewItem(itemTask: String) {
        val newItem = getNewItemEntry(itemTask)
        insertItem(newItem)
    }


    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }


    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }


    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }


    fun isEntryValid(itemName: String): Boolean {
        if (itemName.isBlank()) {
            return false
        }
        return true
    }


    private fun getNewItemEntry(itemTask: String): Item {
        return Item(
            itemTask = itemTask,

            )
    }


    private fun getUpdatedItemEntry(
        itemId: Int,
        itemTask: String,
        isCompleted: Boolean

        ): Item {
        return Item(
            id = itemId,
            itemTask = itemTask,
            isCompleted = isCompleted

            )
    }

       fun getCompletedItems(): Flow<List<Item>> {
        return itemDao.getCompletedItems()
    }
}


class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoListViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
