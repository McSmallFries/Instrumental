package com.example.instrumental

/**
 * Represents an instrument that can be tuned.
 */
class Instrument  {

    val id : String
    val name : String

    private var numOfStrings : Int

    /**
     * Notes from the String table in database are retrieved and stored here based
     * selected instrument. Search operation used by tuner repeatedly so O(1) time complexity
     * is necessary hence I have opted for a hashmap.
     */
    var correspondingStringNotes : HashMap<String, CorrectNote>

    /**
     * initialise with name only, use methods to gather rest of data from database.
     */
    constructor(id : String, name : String)
    {
        this.id = id
        this.name = name

        // set later on with data from database
        numOfStrings = 0
        correspondingStringNotes = HashMap()
    }

    fun NumOfStringsDefault() : Int {
        return 6
    }

    fun getCorrespondingStringNotesDefault() : HashMap<String, CorrectNote>  {
        // chromatic scale to start with
        var map = HashMap<String, CorrectNote>(numOfStrings)
        map.put("A", CorrectNote("A", -1, 110f))
        map.put("A#", CorrectNote("A#", -1, 116.54f))
        map.put("B", CorrectNote("B", -1, 123.47f))
        map.put("C", CorrectNote("C", -1, 130.81f))
        map.put("C#", CorrectNote("C#", -1, 138.59f))
        map.put("D", CorrectNote("D", -1, 146.83f))
        map.put("D#", CorrectNote("D#", -1, 155.56f))
        map.put("E", CorrectNote("E", -1, 164.81f))
        map.put("F", CorrectNote("F", -1, 174.61f))
        map.put("F#", CorrectNote("F#", -1, 185f))
        map.put("G", CorrectNote("G", -1, 196f))
        map.put("G#", CorrectNote("G#", -1, 207.65f))

        return map
    }
}
