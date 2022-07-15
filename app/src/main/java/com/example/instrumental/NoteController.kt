package com.example.instrumental

import kotlin.math.round

/**
 * This is a NoteController
 *
 *
 */
class NoteController {

    /**
     * This instance will be variable and change whenever a new note is played.
     */
    var currentNote : Note

    /**
     * Primary constructor: A frequency is passed and the given note is calculated.
     * @param Frequency (Float) Frequency in Hz
     * -----------------------------------------------------
     * constructor(frequency: Float) {
     *   currentNote = mapFrequencyToNote(frequency)
     * }
     */


    /**
     * Explicitly set the note rather than calculate the note from frequency.
     */
    constructor(currNote: Note) {
        currentNote = currNote
    }





    private fun createNotesMap() : HashMap<Note, String> {
        // Place all notes in chromatic scale into the hashmap and assign a corresponding String
        // value...
        val noteMap : HashMap<Note, String> = HashMap<Note, String>()
        noteMap.put(Note("A"), "A")
        noteMap.put(Note("A#"), "A#")
        noteMap.put(Note("B"), "B")
        noteMap.put(Note("C"), "C")
        noteMap.put(Note("C#"), "C#")
        noteMap.put(Note("D"), "D")
        noteMap.put(Note("D#"), "D#")
        noteMap.put(Note("E"), "E")
        noteMap.put(Note("F"), "F")
        noteMap.put(Note("F#"), "F#")
        noteMap.put(Note("G"), "G")
        noteMap.put(Note("G#"), "G#")
        noteMap.put(Note("_None"), "-")
        return (noteMap)
    }

    public fun noteString() : String  {
        return currentNote.toString()
    }

}