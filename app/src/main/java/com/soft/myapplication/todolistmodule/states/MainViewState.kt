package com.soft.myapplication.todolistmodule.states

import com.soft.myapplication.common.ViewState
import com.soft.myapplication.todolistmodule.model.TodoItem

sealed class MainViewState: ViewState {
    data class TodoItems(val items: List<TodoItem>): MainViewState()
    object Loading: MainViewState()
    data class Error(val exceptionMsg: String): MainViewState()
}