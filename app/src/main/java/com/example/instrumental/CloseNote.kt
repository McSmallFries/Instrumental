package com.example.instrumental

/**
 * Represents the closest note to the frequency that is detected by the tuner.
 *
 * @param note
 * @param actualFrequency The frequency that is detected by tuner
 */
class CloseNote(
    override val note: String,
    val octave : Int,
    val actualFrequency : Float
) : Note(note, octave)  {

    /**
     * Compare the frequency of the CloseNote (Approximate) to that of the CorrectNote.
     *
     * @return 0 if it is within the acceptable margin of error.
     * @return -1 if the user should tune downwards.
     * @return 1 if the user should tune upwards.
     */
    fun compareToCorrectNote(note : CorrectNote) : Int {
        // allow for error margin of 1 Hz up or down (1 Hz equals 4 cents)
        val errorMargin = 1f
        val correctFrequency = note.correctFrequency
        val upperMargin = correctFrequency + errorMargin
        val lowerMargin = correctFrequency - errorMargin
        return when (actualFrequency in lowerMargin..upperMargin)  {
            true -> 0 // within the acceptable range
            false -> when (actualFrequency < (correctFrequency - errorMargin)) {
                true -> -1 // less than = tune down -> (-1)
                false -> 1 // greater than = tune up -> (1)
            }
        }
    }
}