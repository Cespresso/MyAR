package cespresso.gmail.com.espressosar.util

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageUtli (val application: Application){
    suspend fun getBitmapByUrl(imagePath:String):Bitmap{
        return suspendCoroutine {continuation ->
            Glide.with(application.applicationContext)
                .asBitmap()
                .load(imagePath)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        continuation.resume(resource)
                    }
                })
        }
}

}