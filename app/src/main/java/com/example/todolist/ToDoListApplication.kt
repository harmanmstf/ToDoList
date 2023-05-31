package com.example.todolist

import android.app.Application
import com.example.todolist.data.ItemRoomDatabase


class ToDoListApplication : Application() {

    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}