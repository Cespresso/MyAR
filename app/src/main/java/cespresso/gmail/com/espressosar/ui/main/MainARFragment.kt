package cespresso.gmail.com.espressosar.ui.main

import com.google.ar.sceneform.ux.ArFragment
import com.google.a.b.a.a.a.e
import android.R.attr.configure
import android.util.Log
import com.google.ar.core.Config
import com.google.ar.core.Config.UpdateMode
import com.google.ar.core.Session


class MainARFragment :ArFragment(){

    override fun getSessionConfiguration(session: Session): Config {

        planeDiscoveryController.setInstructionView(null)

        val config = Config(session)
        config.updateMode = UpdateMode.LATEST_CAMERA_IMAGE
        session.configure(config)
        this.arSceneView.setupSession(session)

        if ((activity as MainActivity).setupAugmentedImageDb(config, session)) {
            Log.d("SetupAugImgDb", "Success")
        } else {
            Log.e("SetupAugImgDb", "Failed to setup Db")
        }

        return config
    }
}