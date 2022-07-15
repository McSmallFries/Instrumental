package com.example.instrumental

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.example.instrumental.databinding.ActivityTunerBinding


class TunerActivity : AppCompatActivity() {

    // use the same tuner processor that the view model uses.
    // private var tuner : TunerProcessor = TunerProcessor()
    private var note : Note = Note()

    // initialise binding
    private lateinit var binding : ActivityTunerBinding

    // initialise the view model
    private lateinit var tunerViewModel : TunerViewModel

    // initialise UI
    private lateinit var tvFrequency : TextView
    private lateinit var tvNote : TextView
    private lateinit var ivUpArrow : ImageView
    private lateinit var ivDownArrow : ImageView
    private lateinit var instrumentSpinner : Spinner
    private lateinit var tvSelectedInstrument : TextView
    private lateinit var btnBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isPermissionGranted = ContextCompat.checkSelfPermission(this@TunerActivity,
            Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

        // create the data binding and the tuner's ViewModel
        binding = ActivityTunerBinding.inflate(layoutInflater)

        // set content view as binding root
        setContentView(binding.root)

        // set lifecycle owner (needs to know when data in viewmodel has changed)
        binding.lifecycleOwner = this
        tunerViewModel = ViewModelProvider(this).get(TunerViewModel::class.java)

        // store the UI components at class level.
        tvFrequency = findViewById(R.id.tvFrequency)
        tvNote = findViewById(R.id.tvNote)
        ivUpArrow = findViewById(R.id.ivUpArrow)
        ivDownArrow = findViewById(R.id.ivDownArrow)
        tvSelectedInstrument = findViewById(R.id.tvSelectedInstrument)
        instrumentSpinner = findViewById(R.id.spnInstrumentSpinner)
        btnBack = findViewById(R.id.btnBack)

        // create adapter for the spinner, mapping the names of each intr object to
        // a string adapter collection.
        var instruments = tunerViewModel.availableInstruments
        var adapter = ArrayAdapter<String>(this@TunerActivity,
            android.R.layout.simple_spinner_dropdown_item, (instruments.map { it.name }).toTypedArray())
        instrumentSpinner.adapter = adapter

        // override selected item listener
        instrumentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener  {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tvSelectedInstrument.text = instruments[position].name
                // change the current instrument in view model.
                var selected = instruments[position]
                tunerViewModel.currentInstrument = selected

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // change to default chromatic mode
                tvSelectedInstrument.text = "Chromatic"
                tunerViewModel.currentInstrument = tunerViewModel.DEFAULT_INSTRUMENT
            }

        }

        // observe the note data via the binding
        tunerViewModel.mutableNote.observe(this, {
            binding.tvNote.text = it.note

            // println("NoteIT: " + it.note)
        })

        // do the same for frequency...
        tunerViewModel.mutableFrequency.observe(this, {
            binding.tvFrequency.text = it.toString()
            // println("FrequencyIT: " + it.toString())
        })

        // observe the mutable booleans tuneUp and tuneDown..
        tunerViewModel.tuneUp.observe(this,  {
            if (it)  {
                binding.ivUpArrow.setImageResource(R.drawable.ic_tuning_direction_correct)
            }
            else {
                binding.ivUpArrow.setImageResource(R.drawable.ic_tuning_direction)
            }
        })

        // observe the mutable booleans tuneUp and tuneDown..
        tunerViewModel.tuneDown.observe(this,  {
            if (it)  {
                binding.ivDownArrow.setImageResource(R.drawable.ic_tuning_direction_correct)
            }
            else {
                binding.ivDownArrow.setImageResource(R.drawable.ic_tuning_direction)
            }
        })

        // observe when the correct pitch is achieved..
        tunerViewModel.isCorrectPitchAchieved.observe(this,  {
            if (it)  {
                // change the note text to green
                binding.tvNote.setTextColor(ContextCompat.getColor(this, R.color.green_main))
            }
            else  {
                binding.tvNote.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        })

        // initialise the tunerViewModel using its methods
        tunerViewModel.buildTunerComponents()

        // start the pitch detection...
        if (isPermissionGranted)  {
            tunerViewModel.startTuning()
        }
        else {
            tvNote.text = "-"
            tvFrequency.text = "Microphone permission needed."
        }
        // set back button listener
        btnBack.setOnClickListener { navigateToMainActivity() }


    }

    fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}