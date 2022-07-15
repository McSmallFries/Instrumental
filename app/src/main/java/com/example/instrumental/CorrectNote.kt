package com.example.instrumental

class CorrectNote(
    override val note: String,
    val octave : Int,
    val correctFrequency : Float
) : Note(note)  {
    override fun toString() : String {
        return "CorrectNote: note=$note, octave=$octave, correctFrequency=$correctFrequency"
    }
}