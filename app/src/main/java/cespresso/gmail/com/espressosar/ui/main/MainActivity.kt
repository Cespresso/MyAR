package cespresso.gmail.com.espressosar.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cespresso.gmail.com.espressosar.R
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private var arFragment: MainARFragment? = null
    private var shouldAddModel = true
    public lateinit var frameLayout:FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frameLayout = findViewById(R.id.framelayout)

        viewModel.update.observe(this, Observer {
            if(it){
                val arFragment =  MainARFragment()
                supportFragmentManager.beginTransaction().replace(R.id.framelayout,arFragment).commit()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

    fun setupAugmentedImageDb(config: Config, session: Session): Boolean {
        val augmentedImageDatabase = AugmentedImageDatabase(session)
        viewModel.imageMap.value?.forEach {
            Log.e("^v^",it.key.name+"を追加したよ")
            augmentedImageDatabase.addImage(it.key.name,it.value)
        }
        config.augmentedImageDatabase = augmentedImageDatabase
        return true
    }

    private fun loadAugmentedImage(): Bitmap? {
        try {
            assets.open("airplane.jpg").use { `is` -> return BitmapFactory.decodeStream(`is`) }
        } catch (e: IOException) {
            Log.e("ImageLoad", "IO Exception while loading", e)
        }

        return null
    }

    fun onUpdateFrame(frameTime: FrameTime) {
        val frame = arFragment!!.arSceneView.arFrame

        val augmentedImages = frame!!.getUpdatedTrackables(AugmentedImage::class.java)
        Log.e("^v^",augmentedImages.size.toString())
        for (augmentedImage in augmentedImages) {
            Log.e("^v^",augmentedImage.name)
            if (augmentedImage.trackingState == TrackingState.TRACKING) {
                Log.e("^v^",augmentedImage.name)

                if (augmentedImage.name == "airplane" && shouldAddModel) {
                    placeObject(
                        arFragment!!,
                        augmentedImage.createAnchor(augmentedImage.getCenterPose()),
                        Uri.parse("Airplane.sfb")
                    )
                    shouldAddModel = false
                }
            }
        }

    }


    private fun placeObject(fragment: ArFragment, anchor: Anchor, model: Uri) {
        ModelRenderable.builder()
            .setSource(fragment.context, model)
            .build()
            .thenAccept{ renderable -> addNodeToScene(fragment, anchor, renderable) }
            .exceptionally{ throwable ->
                val builder = AlertDialog.Builder(this)
                builder.setMessage(throwable.message)
                    .setTitle("Error!")
                val dialog = builder.create()
                dialog.show()
                null
            }

    }

    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(fragment.transformationSystem)
        node.renderable = renderable
        node.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        node.select()
    }

}
