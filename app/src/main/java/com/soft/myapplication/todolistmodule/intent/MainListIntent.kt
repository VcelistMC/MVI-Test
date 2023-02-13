package com.soft.myapplication.todolistmodule.intent

import com.soft.myapplication.common.ViewIntent
import com.soft.myapplication.todolistmodule.model.TodoItem

sealed class MainListIntent: ViewIntent{
    object AddTaskPressed: MainListIntent()
    data class EditTaskPressed(val itemToEdit: TodoItem): MainListIntent()
    data class NewTaskAdded(val newTask: String): MainListIntent()
    data class SearchTask(val query: String): MainListIntent()
    object ClearSearch: MainListIntent()
    data class TaskDeleted(val itemToDelete: TodoItem): MainListIntent()
    data class TaskEdited(val oldItem: TodoItem, val newItem: TodoItem): MainListIntent()
    object FetchAllTasks: MainListIntent()
}