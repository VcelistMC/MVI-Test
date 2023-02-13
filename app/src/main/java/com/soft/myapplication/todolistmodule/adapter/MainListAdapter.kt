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

    var deletionListener: ItemActions? = null
    class TodoItemViewHolder(itemView: View, val listener: ItemActions?) : RecyclerView.ViewHolder(itemView) {
        fun bindItemToView(item: TodoItem){
            itemView.isCompletedCB.isChecked = item.isCompleted
            itemView.todoText.text = item.description
        }

        fun listenToCheckBox(item: TodoItem){
            itemView.isCompletedCB.setOnCheckedChangeListener { _, isChecked ->
                item.isCompleted = isChecked
            }
        }

        fun listenToDeletion(item: TodoItem){
            itemView.delete.setOnClickListener {
                listener?.onItemDeleted(item)
            }
        }

        fun listenToEdit(currentItem: TodoItem) {
            itemView.editBtn.setOnClickListener {
                listener?.onItemEdited(currentItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item, parent, false)

        return TodoItemViewHolder(view, deletionListener)
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
        holder.listenToDeletion(currentItem)
        holder.listenToEdit(currentItem)
    }
}

interface ItemActions{
    fun onItemDeleted(deletedItem: TodoItem)
    fun onItemEdited(itemToEdit: TodoItem)
}