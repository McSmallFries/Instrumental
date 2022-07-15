package com.example.instrumental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import com.example.instrumental.databinding.ActivitySettingsBinding
import androidx.lifecycle.ViewModelProvider

class SettingsActivity : AppCompatActivity() {

    // initialise the binding
    private lateinit var binding : ActivitySettingsBinding

    // initialise the view model
    private lateinit var  settingsViewModel: SettingsViewModel

    // initialise UI
    private lateinit var tvTitle : TextView
    private lateinit var swDark : Switch
    private lateinit var dwNight : Switch
    private lateinit var rdGroupSampleRate: RadioGroup
    private lateinit var btnCancel : Button
    private lateinit var btnSave : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_settings)

        // create the data binding
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        // set binding root as content view
        setContentView(binding.root)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        // this is broken.. dependency issue that wont resolve
//      binding.viewModel = settingsViewModel
//      binding.lifecycleOwner = this



    }
}