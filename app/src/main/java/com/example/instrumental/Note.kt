package com.example.instrumental

/**
 * A simple class that encapsulates a note and an octave.
 * These two data points represent the key qualities of what makes up a musical note.
 *
 *
 * @param note The note that this instance represents.
 * @param octave The octave of the note that this instance represents. This field is
 * optional as it isn't vital that all notes explicitly declare an octave.
 */
open class Note(note : String, octave : Int)  {

    open val note = note
    private val octave = octave

    /**
     * Since the octave property is an optional one (Not all notes need to have
     * an octave), the default value for a note without an octave is -1.
     */
    constructor(note: String) : this (note, -1)

    /**
     * Default constructor: for representing "not a note".
     */
    constructor() : this("-", -1)


    /**
     * A more suitable toString method for the needs of the app.
     */
    fun noteString() : String { return "$note" }

}