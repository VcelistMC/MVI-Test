package com.soft.myapplication.todolistmodule.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.soft.myapplication.common.BaseViewModel
import com.soft.myapplication.todolistmodule.intent.MainListIntent
import com.soft.myapplication.todolistmodule.model.TodoItem
import com.soft.myapplication.todolistmodule.repo.TodoItemRepo
import com.soft.myapplication.todolistmodule.states.MainViewState
import com.soft.myapplication.todolistmodule.viewaction.MainViewEffect
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel<MainViewState, MainViewEffect, MainListIntent>(application) {
    val repo = TodoItemRepo()
    var cachedItems = mutableListOf<TodoItem>()

    private fun getTodos(query: String? = null){
        viewModelScope.launch {
            repo.getItems(query)
                .onStart {
                    postState(MainViewState.Loading)
                }
                .catch {
                    postState(MainViewState.Error(it.message?: "Failed to get data"))
                }
                .collect{ list ->
                    cachedItems = list.map{it.copy()} as MutableList<TodoItem>
                    postState(MainViewState.TodoItems(list))
                }
        }
    }

    private fun addItem(item: TodoItem){
        viewModelScope.launch {
            repo.addItem(item)
                .catch {
                    postEffect(MainViewEffect.ShowSnackBar(it.message!!))
                }
                .collect{
                    if(it) {
                        cachedItems.add(item)
                        postEffect(MainViewEffect.ShowSnackBar("Task Added"))
                        postState(MainViewState.TodoItems(cachedItems))
                    }
                }
        }
    }

    private fun deleteItem(item: TodoItem){
        viewModelScope.launch {
            repo.deleteItem(item)
                .catch {
                    postEffect(MainViewEffect.ShowSnackBar(it.message!!))
                }
                .collect{
                    if(it) {
                        cachedItems.remove(item)
                        postEffect(MainViewEffect.ShowSnackBar("Task deleted"))
                        postState(MainViewState.TodoItems(cachedItems))
                    }
                }
        }
    }

    override fun handleIntent(intent: MainListIntent) {
        when (intent){
            is MainListIntent.SearchTask -> getTodos(intent.query)
            is MainListIntent.ClearSearch -> getTodos()

            is MainListIntent.AddTaskPressed -> postEffect(MainViewEffect.ShowAddDialog)
            is MainListIntent.NewTaskAdded -> {
                val newTask = TodoItem(intent.newTask)
                addItem(newTask)
            }

            is MainListIntent.FetchAllTasks -> getTodos()

            is MainListIntent.TaskDeleted -> deleteItem(intent.itemToDelete)

            is MainListIntent.EditTaskPressed -> postEffect(MainViewEffect.ShowEditDialog(intent.itemToEdit))
            is MainListIntent.TaskEdited -> editItem(intent.oldItem, intent.newItem)
        }
    }

    private fun editItem(oldItem: TodoItem, newItem: TodoItem) {
        viewModelScope.launch {
            repo.editItem(oldItem, newItem)
                .catch {
                    postEffect(MainViewEffect.ShowSnackBar(it.message!!))
                }
                .collect{
                    val oldInd = cachedItems.indexOf(oldItem)
                    cachedItems[oldInd] = newItem
                    postEffect(MainViewEffect.ShowSnackBar("Success"))
                    postState(MainViewState.TodoItems(cachedItems))
                }
        }
    }


}