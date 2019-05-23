package cespresso.gmail.com.espressosar.ui.main

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cespresso.gmail.com.espressosar.data.entity.Sign
import cespresso.gmail.com.espressosar.data.source.IApiService
import cespresso.gmail.com.espressosar.util.ImageUtli
import com.google.ar.core.AugmentedImageDatabase
import kotlinx.coroutines.launch

class MainViewModel(val service: IApiService, val imageUtil: ImageUtli) : ViewModel() {
    val update = MutableLiveData<Boolean>()
    val imageMap = MutableLiveData<MutableMap<Sign,Bitmap>>().apply {
        value = mutableMapOf()
    }

    fun getData() {
        viewModelScope.launch {
//            val augmentedImageDatabase = augmentedImageDatabase.value
            val result = service.getAllSigns().await()
            result.body()?.let {
                Log.e("^v^", result.body().toString())
                it.map{ sign ->
                    val image = imageUtil.getBitmapByUrl(sign.imageUrl)
                    Log.e("^v^",image.byteCount.toString())
                    Log.e("^v^",image.height.toString())
                    Log.e("^v^",image.width.toString())
                    imageMap.value!![sign]=image

                }

                update.value = true
            }

        }
    }
}