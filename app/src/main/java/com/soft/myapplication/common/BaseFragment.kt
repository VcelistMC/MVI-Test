package com.soft.myapplication.common

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

open class BaseFragment<VM: BaseViewModel<VS, VA, VI>, VS: ViewState, VA: ViewAction, VI: ViewIntent>: Fragment(), OnFragmentCreated {
    lateinit var viewModel: VM

    @Suppress("UNCHECKED_CAST")
    private val viewModelType: Class<VM>
        get() {
            return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        }

    private fun createViewModel(): VM {
        val t = viewModelType
        Log.v("vmt", t.name)
        return ViewModelProvider(this).get(viewModelType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        initListeners()
        initObservers()
        initRecycler()

        viewModel.stateLiveData.observe(viewLifecycleOwner){
            renderViewState(it)
        }

        viewModel.actionLiveData.observe(viewLifecycleOwner){
            renderViewAction(it)
        }
    }



    @Suppress("UNCHECKED_CAST")
    val baseActivity: BaseActivity<BaseViewModel<ViewState, ViewAction, ViewIntent>>
    get(){
        return activity as BaseActivity<BaseViewModel<ViewState, ViewAction, ViewIntent>>
    }

    val container: Int
    get() {
        return baseActivity.container
    }


    fun addFragment(fragment: Fragment){
        val backStackName: String = fragment::javaClass.toString()
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(container, fragment, tag)?.addToBackStack(backStackName)
            ?.commit()
    }

    // We call this method when we want to send an intent for the viewmodel to handle
    fun dispatchIntent(intent: VI){
        viewModel.handleIntent(intent)
    }

    override fun initObservers() {}
    override fun initRecycler() {}
    override fun initListeners() {}

    open fun renderViewState(state: VS){}
    open fun renderViewAction(action: VA){}
}