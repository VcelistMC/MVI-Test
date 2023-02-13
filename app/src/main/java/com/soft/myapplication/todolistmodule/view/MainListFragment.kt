package com.soft.myapplication.todolistmodule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.soft.myapplication.R
import com.soft.myapplication.common.AlertDialogHelper
import com.soft.myapplication.common.BaseFragment
import com.soft.myapplication.todolistmodule.adapter.ItemActions
import com.soft.myapplication.todolistmodule.adapter.MainListAdapter
import com.soft.myapplication.todolistmodule.intent.MainListIntent
import com.soft.myapplication.todolistmodule.model.TodoItem
import com.soft.myapplication.todolistmodule.states.MainViewState
import com.soft.myapplication.todolistmodule.viewaction.MainViewEffect
import com.soft.myapplication.todolistmodule.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main_list.*


class MainListFragment : BaseFragment<MainViewModel, MainViewState, MainViewEffect, MainListIntent>(),
    ItemActions {

    private lateinit var adapter: MainListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dispatchIntent(MainListIntent.FetchAllTasks)
    }
    override fun initRecycler() {
        listRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainListAdapter(emptyList())
        listRv.adapter = adapter
        adapter.deletionListener = this
    }


    override fun initListeners() {
        listenToAddTaskBtn()
        listenToSearchBar()
        listenToClearBtn()
    }

    private fun listenToClearBtn() {
        clearBtn.setOnClickListener {
            search_et.setText("")
            dispatchIntent(MainListIntent.ClearSearch)
        }
    }

    private fun listenToSearchBar() {
        searchBtn.setOnClickListener {
            dispatchIntent(MainListIntent.SearchTask(search_et.text.toString()))
        }
        search_et.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrBlank() || text.isEmpty()){
                dispatchIntent(MainListIntent.FetchAllTasks)
            }
        }
    }

    private fun listenToAddTaskBtn() {
        addTaskBtn.setOnClickListener {
            dispatchIntent(
                MainListIntent.AddTaskPressed
            )
        }
    }

    private fun toggleRecyclerVisibility(isLoading: Boolean){
        progress_circular.visibility = if(isLoading) View.VISIBLE else View.GONE
        error_layout.visibility = View.GONE
        listRv.visibility = if(!isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(msg: String){
        progress_circular.visibility = View.GONE
        listRv.visibility = View.GONE
        error_layout.visibility = View.VISIBLE
        errTv.text = msg
    }

    override fun renderViewState(state: MainViewState) {
        when(state){
            is MainViewState.Loading -> {
                toggleRecyclerVisibility(true)
            }

            is MainViewState.TodoItems -> {
                toggleRecyclerVisibility(false)
                adapter.updateList(state.items)
            }

            is MainViewState.Error -> {
                showError(state.exceptionMsg)
            }
        }
    }

    override fun renderViewEffect(effect: MainViewEffect) {
        when(effect){
            is MainViewEffect.ShowSnackBar -> {
                Snackbar.make(requireView(), effect.msg, Snackbar.LENGTH_LONG).show()
            }

            is MainViewEffect.ShowToast -> {
                Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_LONG).show()
            }

            is MainViewEffect.ShowAddDialog -> {
                AlertDialogHelper.showInputDialog(requireContext(), "Add task"){
                    dispatchIntent(
                        MainListIntent.NewTaskAdded(it)
                    )
                }
            }

            is MainViewEffect.ShowEditDialog -> {
                AlertDialogHelper.showInputDialog(requireContext(), "Edit task"){
                    val newTask = effect.itemToEdit.copy(description = it)
                    dispatchIntent(MainListIntent.TaskEdited(effect.itemToEdit, newTask))
                }
            }
        }
    }


    companion object {
        fun create() = MainListFragment()
    }

    override fun onItemDeleted(deletedItem: TodoItem) {
        dispatchIntent(MainListIntent.TaskDeleted(deletedItem))
    }

    override fun onItemEdited(itemToEdit: TodoItem) {
        dispatchIntent(MainListIntent.EditTaskPressed(itemToEdit))
    }

}