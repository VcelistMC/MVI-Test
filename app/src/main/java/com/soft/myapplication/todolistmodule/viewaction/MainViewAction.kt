package com.soft.myapplication.todolistmodule.viewaction

import com.soft.myapplication.common.ViewAction

sealed class MainViewAction: ViewAction {
    data class ShowSnackBar(val msg: String): MainViewAction()
    data class ShowToast(val msg: String): MainViewAction()
}