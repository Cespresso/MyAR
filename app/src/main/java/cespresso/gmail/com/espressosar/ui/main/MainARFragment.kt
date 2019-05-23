package cespresso.gmail.com.espressosar.ui.main

import com.google.ar.sceneform.ux.ArFragment
import com.google.a.b.a.a.a.e
import android.R.attr.configure
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.*
import com.google.ar.core.Config.UpdateMode
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.TransformableNode


class MainARFragment :ArFragment(){
    var shouldAddModel = true

    override fun getSessionConfiguration(session: Session): Config {

        planeDiscoveryController.setInstructionView(null)
            planeDiscoveryController.hide()
        val config = Config(session)
        config.updateMode = UpdateMode.LATEST_CAMERA_IMAGE
        session.configure(config)
        this.arSceneView.setupSession(session)

        if ((activity as MainActivity).setupAugmentedImageDb(config, session)) {
            Log.d("SetupAugImgDb", "Success")
        } else {
            Log.e("SetupAugImgDb", "Failed to setup Db")
        }
        arSceneView.scene.addOnUpdateListener {frameTime->
            onUpdateFrame(frameTime)
        }

        return config
    }
    fun onUpdateFrame(frameTime: FrameTime) {
        val frame = this.arSceneView.arFrame

        val augmentedImages = frame!!.getUpdatedTrackables(AugmentedImage::class.java)
        Log.e("^v^",augmentedImages.size.toString())
        for (augmentedImage in augmentedImages) {
            Log.e("^v^",augmentedImage.name)
            if (augmentedImage.trackingState == TrackingState.TRACKING) {
                Log.e("^v^",augmentedImage.name)

                if (augmentedImage.name == "airplane" && shouldAddModel) {
//                    placeObject(
//                        this,
//                        augmentedImage.createAnchor(augmentedImage.getCenterPose()),
//                        Uri.parse("Airplane.sfb")
//                    )
                    placeText(this,augmentedImage.createAnchor(augmentedImage.centerPose),augmentedImage.name)
//                    placeWebView(this,augmentedImage.createAnchor(augmentedImage.centerPose),"https://www.google.com/")
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
//                val builder = AlertDialog.Builder(activity!!.co)
//                builder.setMessage(throwable.message)
//                    .setTitle("Error!")
//                val dialog = builder.create()
//                dialog.show()
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
    private fun placeText(fragment: ArFragment, anchor: Anchor,text:String) {
        val textView = TextView(activity)
        textView.text = text
        ViewRenderable.builder()
            .setView(activity, textView)
            .build()
            .thenAccept { renderable -> addNodeToScene(fragment, anchor, renderable) }

    }
    private fun placeWebView(fragment: ArFragment, anchor: Anchor,url:String) {
        val b = WebView(activity)
        b.webViewClient = WebViewClient()
        b.loadUrl(url)//"https://www.google.com/"
        ViewRenderable.builder()
            .setView(activity, b)
            .build()
            .thenAccept { renderable -> addNodeToScene(fragment, anchor, renderable) }

    }
}