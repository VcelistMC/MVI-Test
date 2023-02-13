package com.soft.myapplication

import android.os.Bundle
import com.soft.myapplication.common.*
import com.soft.myapplication.todolistmodule.view.MainListFragment

class MainActivity : BaseActivity<BaseViewModel<ViewState, ViewAction, ViewIntent>>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContainerView(R.id.mainLayout)
        addFragment(MainListFragment.create())
    }
}