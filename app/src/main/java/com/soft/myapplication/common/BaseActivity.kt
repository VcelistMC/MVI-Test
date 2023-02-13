package com.soft.myapplication.common

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

open class BaseActivity<VM: BaseViewModel<ViewState, ViewAction, ViewIntent>>: AppCompatActivity() {
    lateinit var viewModel: VM

    @Suppress("UNCHECKED_CAST")
    private val viewModelType: Class<VM>
        get() {
            return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        viewModel = initializeViewModel()
    }

    private fun initializeViewModel(): VM {
        return ViewModelProvider(this)[viewModelType]
    }

    var container = -1

    fun setContainerView(view: Int){
        container = view
    }

    fun addFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .add(container, fragment, fragment.tag).addToBackStack("")
            .commit()
    }
}