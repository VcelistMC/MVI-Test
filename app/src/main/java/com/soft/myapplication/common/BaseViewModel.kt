package com.soft.myapplication.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseViewModel<VS: ViewState, A: ViewEffect, I: ViewIntent>(application: Application) : AndroidViewModel(application) {
    protected var _state = MutableLiveData<VS>()
    val stateLiveData: LiveData<VS>
        get() = _state

    protected var _action = MutableLiveData<A>()
    val actionLiveData: LiveData<A>
        get() = _action


    // This method is responsible for handling intents that are sent from the BaseFragment.dispatchIntent method
    abstract fun handleIntent(intent: I)

    fun postState(state: VS){
        _state.postValue(state)
    }

    fun postAction(action: A){
        _action.postValue(action)
    }

}