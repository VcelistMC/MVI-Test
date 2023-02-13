package com.soft.myapplication.todolistmodule.intent

import com.soft.myapplication.common.ViewIntent

sealed class MainListIntent: ViewIntent{
    data class AddTaskPressed(val newTask: String): MainListIntent()
    data class SearchTask(val query: String): MainListIntent()
    object ClearSearch: MainListIntent()
}