package com.akdogan.swcharacters.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.akdogan.swcharacters.datasource.DomainGotCharacter
import com.akdogan.swcharacters.datasource.MyRepository
import com.akdogan.swcharacters.datasource.ServiceLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ServiceLocator.getDefaultRepository(app)

    val characterList : LiveData<List<DomainGotCharacter>> = repo.data

    private val _navigateWithUrl = MutableLiveData<String?>()
    val navigateWithUrl : LiveData<String?>
        get() = _navigateWithUrl

    init {
        Log.i("REPO_INSTANCE_TEST", "Main viewmodel ${repo.getTestValue()}")
    }


    fun getCharacters(){
        viewModelScope.launch {
            repo.fetchData()
        }
    }

    fun invokeItemClicked(url: String){
        Log.i("CLICK_LISTENER_TEST", "url: $url")
        _navigateWithUrl.value = url
    }

    fun onNavigateDone(){
        _navigateWithUrl.value = null
    }


}