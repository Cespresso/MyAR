package cespresso.gmail.com.espressosar.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cespresso.gmail.com.espressosar.data.source.IApiService
import com.google.ar.core.AugmentedImageDatabase

class MainViewModel(service:IApiService) :ViewModel(){
    val augmentedImageDatabase = MutableLiveData<AugmentedImageDatabase>()

    fun getData(){

    }
}