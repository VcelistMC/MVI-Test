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
import com.soft.myapplication.todolistmodule.adapter.MainListAdapter
import com.soft.myapplication.todolistmodule.intent.MainListIntent
import com.soft.myapplication.todolistmodule.states.MainViewState
import com.soft.myapplication.todolistmodule.viewaction.MainViewAction
import com.soft.myapplication.todolistmodule.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main_list.*


class MainListFragment : BaseFragment<MainViewModel, MainViewState, MainViewAction, MainListIntent>() {

    lateinit var adapter: MainListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTodos()
    }
    override fun initRecycler() {
        listRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainListAdapter(emptyList())
        listRv.adapter = adapter
    }


    override fun initListeners() {
        listenToAddTaskBtn()
        listenToSearchBar()
        listenToClearBtn()
    }

    private fun listenToClearBtn() {
        clearBtn.setOnClickListener {
            dispatchIntent(MainListIntent.ClearSearch)
        }
    }

    private fun listenToSearchBar() {
        searchBtn.setOnClickListener {
            dispatchIntent(MainListIntent.SearchTask(search_et.text.toString()))
        }
    }

    private fun listenToAddTaskBtn() {
        addTaskBtn.setOnClickListener {
            AlertDialogHelper.showInputDialog(requireContext(), "Add Task"){
                dispatchIntent(
                    MainListIntent.AddTaskPressed(it)
                )
            }
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

    override fun renderViewAction(action: MainViewAction) {
        when(action){
            is MainViewAction.ShowSnackBar -> {
                Snackbar.make(requireView(), action.msg, Snackbar.LENGTH_LONG).show()
            }

            is MainViewAction.ShowToast -> {
                Toast.makeText(requireContext(), action.msg, Toast.LENGTH_LONG).show()
            }
        }
    }


    companion object {
        fun create() = MainListFragment()
    }

}