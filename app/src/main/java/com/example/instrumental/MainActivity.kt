package com.example.instrumental

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isPermissionGranted = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

        if (!isPermissionGranted)   {
                ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1234
            )}

        // store UI
        val ivTuner = findViewById<ImageView>(R.id.ivTuner)
        val ivPlayer = findViewById<ImageView>(R.id.ivPlayer)
        val ivSettings = findViewById<ImageView>(R.id.ivSettings)
        val ivLearn = findViewById<ImageView>(R.id.ivLearn)

        //set UI listeners
        ivTuner.setOnClickListener  {
            navigateToTuner(isPermissionGranted)
        }

        ivSettings.setOnClickListener  {
            navigateToSettings()
        }

    }

    /**
     * Invoked when the user selects to open the tuner activity.
     */
    fun navigateToTuner(permissionGranted: Boolean)  {
        if (permissionGranted)  {
            val intent  = Intent(this, TunerActivity::class.java)
            startActivity(intent)
        }
        else  {
            ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            1235)
        }
    }


    fun navigateToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

}