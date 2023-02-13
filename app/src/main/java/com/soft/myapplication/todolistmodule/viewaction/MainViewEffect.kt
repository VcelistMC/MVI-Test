package com.soft.myapplication.todolistmodule.viewaction

import com.soft.myapplication.common.ViewEffect
import com.soft.myapplication.todolistmodule.model.TodoItem

sealed class MainViewEffect: ViewEffect {
    data class ShowSnackBar(val msg: String): MainViewEffect()
    data class ShowToast(val msg: String): MainViewEffect()
    object ShowAddDialog: MainViewEffect()
    data class ShowEditDialog(val itemToEdit: TodoItem): MainViewEffect()
}