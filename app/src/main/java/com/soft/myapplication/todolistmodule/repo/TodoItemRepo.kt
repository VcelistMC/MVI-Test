package com.soft.myapplication.todolistmodule.repo

import com.soft.myapplication.todolistmodule.model.TodoItem
import com.soft.myapplication.todolistmodule.states.MainViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

class TodoItemRepo {
    val items = mutableListOf(TodoItem.mock1,TodoItem.mock2,TodoItem.mock3)

    private fun shouldSimulateError(): Boolean{
        return Random(System.currentTimeMillis()).nextFloat() < 0.1
    }

    fun addItem(item: TodoItem): Flow<Boolean> {
        return flow{
            delay(1000)
            val isError = shouldSimulateError()
            if(isError){
                error("Failed to add :(")
            }
            else {
                items.add(item)
                emit(true)
            }
        }
    }

    fun getItems(query: String? = null): Flow<List<TodoItem>>{
        return flow {
            delay(500)
            if (query == null){
                emit(items)
            }
            else {
                val filtered = items.filter {
                    it.description.contains(query, ignoreCase = true)
                }
                emit(filtered)
            }
        }
    }

    fun deleteItem(item: TodoItem): Flow<Boolean> {
        return flow {
            delay(100)
            val isError = shouldSimulateError()
            if(isError){
                error("Failed to delete")
            }
            else {
                items.remove(item)
                emit(true)
            }
        }
    }

    fun editItem(old: TodoItem, new: TodoItem): Flow<Boolean>{
        return flow {
            delay(250)
            val isError = shouldSimulateError()
            if(isError){
                error("Failed to edit")
            }
            else{
                val oldInd = items.indexOf(old)
                items[oldInd] = new
                emit(true)
            }
        }
    }
}