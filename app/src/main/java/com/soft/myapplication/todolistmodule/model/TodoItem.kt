package com.soft.myapplication.todolistmodule.model

data class TodoItem (
    var description: String = "",
    var isCompleted: Boolean = false
){
    companion object {
        val mock1 = TodoItem(
            "Buy Milk",
            false
        )
        val mock2 = TodoItem(
            "Buy Eggs",
            false
        )
        val mock3 = TodoItem(
            "Watch Movie",
            true
        )

        val mockList = listOf(mock1, mock2, mock3)
    }
}