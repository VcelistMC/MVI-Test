package com.soft.myapplication.todolistmodule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soft.myapplication.R
import com.soft.myapplication.todolistmodule.model.TodoItem
import kotlinx.android.synthetic.main.todo_list_item.view.*
import java.util.zip.Inflater

class MainListAdapter(
    private var todos: List<TodoItem>
): RecyclerView.Adapter<MainListAdapter.TodoItemViewHolder>(){

    class TodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItemToView(item: TodoItem){
            itemView.isCompletedCB.isChecked = item.isCompleted
            itemView.todoText.text = item.description
        }

        fun listenToCheckBox(item: TodoItem){
            itemView.isCompletedCB.setOnCheckedChangeListener { _, isChecked ->
                item.isCompleted = isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item, parent, false)

        return TodoItemViewHolder(view)
    }

    override fun getItemCount() = todos.size

    fun updateList(list: List<TodoItem>){
        todos = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val currentItem = todos[position]

        holder.bindItemToView(currentItem)
        holder.listenToCheckBox(currentItem)
    }
}