package com.soft.myapplication.todolistmodule.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.soft.myapplication.common.BaseViewModel
import com.soft.myapplication.todolistmodule.intent.MainListIntent
import com.soft.myapplication.todolistmodule.model.TodoItem
import com.soft.myapplication.todolistmodule.states.MainViewState
import com.soft.myapplication.todolistmodule.viewaction.MainViewAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(application: Application) : BaseViewModel<MainViewState, MainViewAction, MainListIntent>(application) {
    private val todos = mutableListOf(TodoItem.mock1, TodoItem.mock2, TodoItem.mock3)

    fun addItem(todoItem: TodoItem){
        todos.add(todoItem)
    }

    private fun shouldSimulateError(): Boolean{
        return Random(System.currentTimeMillis()).nextFloat() < 0.3
    }

    fun getTodos(query: String? = null){
        viewModelScope.launch {
            postState(MainViewState.Loading)

            delay(1500)

            if(shouldSimulateError()){
                postState(
                    MainViewState.Error("Failed to get data")
                )
            }
            else {
                if(query == null){
                    postState(
                        MainViewState.TodoItems(todos)
                    )
                }
                else{
                    val res = todos.filter {
                        it.description.contains(query, ignoreCase = true)
                    }
                    postState(MainViewState.TodoItems(res))
                }
            }
        }
    }

    override fun handleIntent(intent: MainListIntent) {
        when (intent){
            is MainListIntent.SearchTask -> {
                getTodos(intent.query)
            }

            is MainListIntent.ClearSearch -> {
                getTodos()
            }

            is MainListIntent.AddTaskPressed -> {
                val newTask = TodoItem(intent.newTask)
                addItem(newTask)
                getTodos()
            }
        }
    }

    override fun initState() {
        postState(MainViewState.TodoItems(todos))
    }


}