package com.example.instrumental

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import kotlin.math.round

/**
 * Following the MVVM architecture pattern, this is a ViewModel class that encapsulates mutable data
 * for the tuner part of Instrumental. It uses the pitch processing tools from TarsosDSP to get input
 * from the microphone and handles the logic to turn it into the data points that are required. Data
 * is stored in a MutableLiveData object.
 */
class TunerViewModel : ViewModel() {

    /** Database access & DB data... */
    var dbWrapper = DatabaseWrapper(Instrumental.getContext())

    /** data used by the View... */

    /** The MutableLiveData that is bound to the view. */
    var mutableNote = MutableLiveData<Note>()
    var mutableFrequency = MutableLiveData<Float>()
    var tuneUp = MutableLiveData<Boolean>()
    var tuneDown = MutableLiveData<Boolean>()
    var isCorrectPitchAchieved = MutableLiveData<Boolean>()

    val DEFAULT_INSTRUMENT = Instrument("CH1", "Chromatic")
    var currentInstrument = DEFAULT_INSTRUMENT
    val availableInstruments = getAvailableInstrumentsFromDB()

    /** data used by the ViewModel... */

    /** Keeps track of when pitch-processing should be done. */
    var isRunning : Boolean = false

    /** initialised default values */
    private var sampleRate : Int = 44100
    private var recordOverlaps : Int = 3072
    private var bufferSize : Int = 4096

    /** Pitch processing components... */
    private lateinit var pitchHandler : PitchDetectionHandler
    private lateinit var pitchProcessor : AudioProcessor
    private lateinit var dispatcher : AudioDispatcher
    private lateinit var audioThread : Thread

    /** All notes A - G# stored in order, inside an ArrayList for O(1) access using index. */
    private var chromaticNotesArray : ArrayList<Note> = createNotesArray()

    /**
     * Block of code called once the class has been constructed.
     */
    init {
        buildTunerComponents()
        getInstrumentStringsFromDB()
        setAvailableInstrumentStringNotes()
    }

    /** Public interface... */

    /**
     * This function allows the tuner's configuration settings to be changed. It is Optional since
     * the default values are already initialised but the user can change these settings which is when
     * this function will be necessary.
     *
     * @param sampRate Desired sample rate
     * @param rcdOverlaps Desired record overlaps
     * @param bfrSize Desired buffer size
     */
    fun setSoundConfigSettings(sampRate: Int, rcdOverlaps: Int, bfrSize : Int)  {
        // set fields as argument values
        this.sampleRate = sampleRate
        this.recordOverlaps = recordOverlaps
        this.bufferSize = bufferSize
    }

    /**
     * This shall be called from within the view when the tuner section is opened by user. It initialises
     * the variables stored at class level in the correct order, when it's appropriate. (after the data
     * bindings have been set).
     */
    fun buildTunerComponents()  {
        // create instance of pitch detection tools
        dispatcher = createAudioDispatcher()
        pitchHandler = createPitchDetectionHandler()
        pitchProcessor = createPitchProcessor(pitchHandler)
        dispatcher.addAudioProcessor(pitchProcessor)
        audioThread = createAudioThread(dispatcher)

        // start the thread. this may be taken out when I have a function to control tuner on/off
        audioThread.start()
    }


    /** start tuning */
    fun startTuning()  {

    }


    /**
     * Stop tuning
     */
    fun stopTuning()  {

    }

    fun getAllInstrumentNames() : ArrayList<String>  {
        var names = ArrayList<String>()
        availableInstruments.forEach {
            names.add(it.name)
        }
        return names
    }


    /** Private functions */

    /**
     * This function is invoked once this view model has retrieved and stored the
     * instruments from the DB. It sets its correspondingstringnotes field for each
     * item.
     */
    private fun setAvailableInstrumentStringNotes() {
        for (i in availableInstruments)  {
            i.correspondingStringNotes = getInstrumentStringsFromDB()
        }
    }

    /**
     * Take a given frequency and return the note that it most closely matches. More specifically,
     * it uses an algorithm to find the closest note to the frequency being played.
     *
     * @return Note
     */
    private fun mapFrequencyToCloseNote(freq : Float) : CloseNote {

        try {
            // work out note from frequency
            var noteNumber : Float = 12 * kotlin.math.log2(freq / 440) + 49
            noteNumber = round(noteNumber)

            var noteIndex : Int = ((noteNumber - 1) % 12).toInt() // because 12 notes from A - G#

            // note string
            var note = chromaticNotesArray[noteIndex].note

            // octave of note
            var octave : Int = kotlin.math.floor((noteNumber + 8) / 12).toInt()

            return CloseNote(note, octave, freq)
        }
        // handle any anomolous notes
        catch (obe: ArrayIndexOutOfBoundsException)  {
            println(obe.stackTrace)
            var note = CloseNote("-", 0, -1f)
            println(note)
            return note
        }
    }


    /**
     * Function delegates to the dbWrapper object and returns all available instruments in the
     * database.
     */
    private fun getAvailableInstrumentsFromDB() : ArrayList<Instrument>  {
        return dbWrapper.getAvailableInstrumentsFromDB()
    }

    /**
     * Using the ID of each available instrument, use it to query the database and return a
     * hashmap to set all the available instruments' correspondingstringnotes value.
     */
    private fun getInstrumentStringsFromDB() : HashMap<String, CorrectNote> {
        var stringNotes = HashMap<String, CorrectNote>()
        // get all string notes for each instrument in database
        for (i in availableInstruments)  {
            var iStrings = dbWrapper.getInstrumentStringsFromDB(i.id)
            // put all in hashmap
            stringNotes.putAll(iStrings)
        }
        return stringNotes
    }

    /**
     * Put the CloseNote and Frequency played into the data objects that are bound to the
     * view.
     *
     * @param newCloseNote
     * @param newFreq
     */
    private fun updateCloseNoteAndFrequency(newCloseNote : Note, newFreq : Float)  {
        // postValue() vs setValue() ??
        mutableNote.postValue(newCloseNote)
        mutableFrequency.postValue(newFreq)
        println("MutableNote: " + mutableNote.value +
                "MutableFreq: " + mutableFrequency.value)
    }

    /**
     * Create the Array to store each note in the chromatic scale
     */
    private fun createNotesArray() : ArrayList<Note>  {
        // Place all notes in chromatic scale into the arraylist
        val noteArray : ArrayList<Note> = ArrayList()
        noteArray.add(Note("A"))
        noteArray.add(Note("A#"))
        noteArray.add(Note("B"))
        noteArray.add(Note("C"))
        noteArray.add(Note("C#"))
        noteArray.add(Note("D"))
        noteArray.add(Note("D#"))
        noteArray.add(Note("E"))
        noteArray.add(Note("F"))
        noteArray.add(Note("F#"))
        noteArray.add(Note("G"))
        noteArray.add(Note("G#"))
        noteArray.add(Note("-"))
        return noteArray
    }

    /**
     * This function handles the output from the comparison of correct notes vs notes played by
     * user and then updates the mutable booleans according to that result. These values can then
     * be used by the view.
     *
     * @param result The result of the note comparison represented by an Integer.
     */
    private fun processComparisonResult(result : Int)  {
        when (result)  {
            0 -> {
                tuneUp.postValue(true)
                tuneDown.postValue(true)
                isCorrectPitchAchieved.postValue(true)
            }
            1 -> {
                tuneUp.postValue(true)
                tuneDown.postValue(false)
                isCorrectPitchAchieved.postValue(false)
            }
            -1 -> {
                tuneDown.postValue(true)
                tuneUp.postValue(false)
                isCorrectPitchAchieved.postValue(false)
            }
        }
    }

    /**
     * Create PitchProcessor
     */
    private fun createPitchProcessor(pdh : PitchDetectionHandler) : AudioProcessor {
        // stores as parent interface AudioProcessor and uses YIN detection algorithm.
        val pitchProcessor : AudioProcessor = PitchProcessor(
            PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
            sampleRate.toFloat(), bufferSize, pdh
        )
        return pitchProcessor
    }

    /**
     * Create PitchDetectionHandler
     */
    private fun createPitchDetectionHandler(): PitchDetectionHandler {
        // detecting frequencies through microphone

        // return a lambda that defines how to handle the pitch detection
        return PitchDetectionHandler { res, event ->
            val freq: Float = res.pitch

            // redundant for now
            val probability: Float = res.probability

            if (freq > -1.0f) {
                // change the values of the mutable data
                var closeNote = mapFrequencyToCloseNote(freq)
                updateCloseNoteAndFrequency(closeNote, freq)

                // get the correct note that the CloseNote corresponds to.
                var correctNote = currentInstrument.correspondingStringNotes.get(closeNote.note)

                // compare with actual note
                if (correctNote != null)  {
                    println("compared: " + closeNote.compareToCorrectNote(correctNote).toString())
                    processComparisonResult(closeNote.compareToCorrectNote(correctNote))
                }

                // this call improves user experience as it gives time to respond
                // and also reduces the amount of background noise picked up by microphone.
                Thread.sleep(200)

//              println("Note: " + note)
//              println("Freq: " + freq)
            }
        }
    }

    /**
     *  Create the AudioDispatcher
     */
    private fun createAudioDispatcher() : AudioDispatcher {
        val dispatcher : AudioDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, recordOverlaps)
        return dispatcher
    }

    /**
     * Create the thread for tuner to run on.
     */
    private fun createAudioThread(dispatcher: AudioDispatcher) : Thread {
        return Thread(dispatcher, "Tuner Thread")
    }


}