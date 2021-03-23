package com.akdogan.swcharacters.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.akdogan.swcharacters.datasource.DomainGotCharacter
import com.akdogan.swcharacters.datasource.ServiceLocator
import kotlinx.coroutines.launch

class DetailViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ServiceLocator.getDefaultRepository(app)

    private val _character = MutableLiveData<DomainGotCharacter>()
    val character : LiveData<DomainGotCharacter>
        get() = _character

    init {
        Log.i("REPO_INSTANCE_TEST", "Detail viewmodel ${repo.getTestValue()}")
    }

    fun getItem(url: String){
        viewModelScope.launch {
            repo.getCharacter(url)?.let {
                _character.postValue(it)
                Log.i("DATABASE_GETCHARACTER", "Result: $it")
            }


        }
    }
}