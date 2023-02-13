package com.soft.myapplication.todolistmodule.viewaction

import com.soft.myapplication.common.ViewEffect

sealed class MainViewEffect: ViewEffect {
    data class ShowSnackBar(val msg: String): MainViewEffect()
    data class ShowToast(val msg: String): MainViewEffect()
}