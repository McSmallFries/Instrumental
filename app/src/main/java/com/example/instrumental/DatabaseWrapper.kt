package com.example.instrumental

import android.content.Context
import android.database.sqlite.*
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.nio.file.Paths


/**
 * A database to store the notes of each instrument is preferred over using objects
 * stored in a Kotlin data structure since it's static data that needs to be queried
 * and this can be stored in a lightweight fashion in the mobile phone's local files
 * and thus makes it easier on the running program. There is no need to remake all this
 * data every single time the program is ran.
 */

const val DB_NAME = "DBInstrumental.db"
val VER = 1

/** CorrectNote table */
val NOTE_TABLE = "CorrectNote"
val NOTE_ID = "id"
val NOTE_NAME = "name"
val NOTE_OCTAVE = "octave"
val NOTE_FREQUENCY = "frequency"

/** Instrument Table */
val INSTR_TABLE = "Instrument"
val INSTR_ID = "id"
val INSTR_NAME = "name"
val INSTR_NUM_OF_STRINGS = "numOfStrings"

/** There is a many : many relationship between notes & instruments, this
 * will need decomposing with an INSTRUMENT_NOTES table. */

/** String table */
val STRING_TABLE = "String"
val STRING_ID = "id"
val STRING_INSTR_ID = "instrumentId"
val STRING_NOTE_ID = "noteId"

//val PATH = "/data/data/com.example.instrumental/databases" + DB_NAME
val PATH = Instrumental.getContext().filesDir.path + DB_NAME

class DatabaseWrapper(context : Context) : SQLiteOpenHelper(context, DB_NAME, null, VER) {

    /**
     * Open/Create if does not exist, a database using the path
     */
    var db = SQLiteDatabase.openOrCreateDatabase(PATH, null)
    lateinit var data : SQLiteDatabase

    fun createDatabase()  {
        data = writableDatabase
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createCorrectNoteTable = "CREATE TABLE $NOTE_TABLE (" +
                "$NOTE_ID VARCHAR(5) PRIMARY KEY," +
                "$NOTE_NAME VARCHAR(5)," +
                "$NOTE_OCTAVE INTEGER," +
                "$NOTE_FREQUENCY REAL);"

        val createInstrumentTable = "CREATE TABLE $INSTR_TABLE (" +
                "$INSTR_ID VARCHAR(5) PRIMARY KEY," +
                "$INSTR_NAME VARCHAR(50)," +
                "$INSTR_NUM_OF_STRINGS INTEGER);"

        val createStringTable = "CREATE TABLE $STRING_TABLE (" +
                "$STRING_ID VARCHAR(5) PRIMARY KEY," +
                "$STRING_INSTR_ID VARCHAR(5)," +
                "$STRING_NOTE_ID VARCHAR(5));"

        // create tables
        println("Creating note table...")
        db?.execSQL(createCorrectNoteTable)
        println("Creating instrument table...")
        db?.execSQL(createInstrumentTable)
        println("Creating string table...")
        db?.execSQL(createStringTable)

        println("Created tables")
        // add data to tables..
        println("Adding data to note")
        db?.execSQL(addDataToNote())
        println("Adding data to instrument")
        db?.execSQL(addDataToInstrument())
        println("Adding data to string")
        db?.execSQL(addDataToString())
        println("successfully added data.")
        println("Database stored: " + db?.path)
        println("Successfulyy added data to tables.")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    private fun addDataToNote() : String {
        val sql = "INSERT INTO $NOTE_TABLE (" +
                "$NOTE_ID, $NOTE_NAME, $NOTE_OCTAVE, $NOTE_FREQUENCY) " +
                "VALUES ('C1', 'C', 1, 32.70), ('C#1', 'C#', 1, 34.65)," +
                "('D1', 'D', 1, 36.71), ('D#1', 'D#', 1, 38.89), " +
                "('E1', 'E', 1, 41.20), ('F1', 'F', 1, 43.65)," +
                "('F#1', 'F#', 1, 46.25), ('G1', 'G', 1, 49.0)," +
                "('G#1', 'G', 1, 51.91), ('A1', 'A', 1, 55.0)," +
                "('A#1', 'A#', 1, 58.27), ('B1', 'B', 1, 61.74)," +
                "('C2', 'C', 2, 65.41), ('C#2', 'C', 2, 69.30)," +
                "('D2', 'D', 2, 73.42), ('D#2', 'D#', 2, 77.78)," +
                "('E2', 'E', 2, 82.41), ('F2', 'F', 2, 87.31)," +
                "('F#2', 'F#', 2, 92.50), ('G2', 'G', 2, 98.00)," +
                "('G#2', 'G#', 2, 103.83), ('A2', 'A', 2, 110.00)," +
                "('A#2', 'A#', 2, 116.54), ('B2', 'B', 2, 123.47)," +
                "('C3', 'C', 3, 130.81), ('C#3', 'C#', 3, 138.59)," +
                "('D3', 'D', 3, 146.83), ('D#3', 'D#', 3, 155.56)," +
                "('E3', 'E', 3, 164.81), ('F3', 'F', 3, 174.61)," +
                "('F#3', 'F#', 3, 185.00), ('G3', 'G', 3, 196.00)," +
                "('G#3', 'G#', 3, 207.65), ('A3', 'A', 3, 220.00)," +
                "('A#3', 'A#', 3, 233.08), ('B3', 'B', 3, 246.94)," +
                "('C4', 'C', 4, 261.63), ('C#4', 'C#', 4, 277.18)," +
                "('D4', 'D', 4, 293.66), ('D#4', 'D#', 4, 311.13)," +
                "('E4', 'E', 4, 329.63), ('F4', 'F', 4, 349.23)," +
                "('F#4', 'F#', 4, 369.99), ('G4', 'G', 4, 392.00)," +
                "('G#4', 'G#', 4, 415.30), ('A4', 'A', 4, 440.00)," +
                "('A#4', 'A#', 4, 466.16), ('B4', 'B', 4, 493.88)," +
                "('C5', 'C', 5, 523.25), ('C#5', 'C#', 5, 554.37)," +
                "('D5', 'D', 5, 587.33), ('D#5', 'D#', 5, 622.25)," +
                "('E5', 'E', 5, 659.25), ('F5', 'F', 5, 698.46)," +
                "('F#5', 'F#', 5, 739.99), ('G5', 'G', 5, 783.25)," +
                "('G#5', 'G#', 5, 830.61), ('A5', 'A', 5, 880.00)," +
                "('A#5', 'A#', 5, 932.33), ('B5', 'B', 5, 987.77)," +
                "('C6', 'C', 6, 1046.50), ('C#6', 'C#', 6, 1108.73)," +
                "('D6', 'D', 6, 1174.66);"
        return sql
    }

    private fun addDataToInstrument() : String {
        val sql = "INSERT INTO $INSTR_TABLE (" +
                "$INSTR_ID, $INSTR_NAME, $INSTR_NUM_OF_STRINGS) " +
                "VALUES ('I1', 'Guitar Standard', 6)," +
                "('I2', 'Guitar Eb Tuning', 6)," +
                "('I3', 'Guitar Drop D', 6)," +
                "('I4', 'Guitar Full-step down', 6)," +
                "('I5', 'Ukulele', 4)," +
                "('I6', 'Ukulele Baritone', 4)," +
                "('I7', 'Banjo standard', 5)," +
                "('I8', 'Violin', 4)," +
                "('I9', 'Bass Standard', 4);"
        return sql
    }

    private fun addDataToString() : String  {
        val sql = "INSERT INTO $STRING_TABLE (" +
                "$STRING_ID, $STRING_INSTR_ID, $STRING_NOTE_ID) " +
                // GTR STD
                "VALUES ('GS1', 'I1', 'E2'), ('GS2', 'I1', 'A2')," +
                "('GS3', 'I1', 'D3'), ('GS4', 'I1', 'G3')," +
                "('GS5', 'I1', 'B3'), ('GS6', 'I1', 'E4')," +
                // HENDRIX
                "('HS1', 'I2', 'D#2'), ('HS2', 'I2', 'G#2')," +
                "('HS3', 'I2', 'C#3'), ('HS4', 'I2', 'F#3')," +
                "('HS5', 'I2', 'A#3'), ('HS6', 'I2', 'D#4')," +
                // DROP D
                "('GDS1', 'I3', 'D2'), ('GDS2', 'I3', 'A2')," +
                "('GDS3', 'I3', 'D3'), ('GDS4', 'I3', 'G3')," +
                "('GDS5', 'I3', 'B3'), ('GDS6', 'I3', 'E4')," +
                // FULL STEP DOWN
                "('FS1', 'I4', 'D2'), ('FS2', 'I4', 'G2')," +
                "('FS3', 'I4', 'C3'), ('FS4', 'I4', 'F3')," +
                "('FS5', 'I4', 'A3'), ('FS6', 'I4', 'D4')," +
                // UKULELE
                "('US1', 'I5', 'G4'), ('US2', 'I5', 'C4')," +
                "('US3', 'I5', 'E4'), ('US4', 'I5', 'A4')," +
                // UKULELE BARITONE
                "('UBS1', 'I6', 'D3'), ('UBS2', 'I6', 'G3')," +
                "('UBS3', 'I6', 'B3'), ('UBS4', 'I6', 'E4')," +
                // BANJO
                "('BJS1', 'I7', 'D4'), ('BJS2', 'I7', 'B3')," +
                "('BJS3', 'I7', 'G3'), ('BJS4', 'I7', 'D3')," +
                "('BJS5', 'I7', 'G4')," +
                // VIOLIN
                "('VS1', 'I8', 'G3'), ('VS2', 'I8', 'D4')," +
                "('VS3', 'I8', 'A4'), ('VS4', 'I8', 'E5')," +
                // BASS STANDARD
                "('BAS1', 'I9', 'E1'), ('BAS2', 'I9', 'A1')," +
                "('BAS3', 'I9', 'D2'), ('BAS4', 'I9', 'G2');"

        return sql
    }

    /**
     * Take an instrument id to query the database and find the corresponding strings
     */
    fun getInstrumentStringsFromDB(instrumentId : String) : HashMap<String, CorrectNote>  {
        // get all strings from table that belong to desired instrument
        val sqlStrings = "SELECT * FROM $STRING_TABLE WHERE $STRING_INSTR_ID = '$instrumentId'"
        var correctNoteMap : HashMap<String, CorrectNote> = HashMap()
        val data = this.readableDatabase
        val strings = data.rawQuery(sqlStrings, null)
        if (strings.moveToFirst())  {
            do  {
                // get noteId from the string table to use as id
                var noteId = strings.getString(strings.getColumnIndexOrThrow(STRING_NOTE_ID))
                // select rest of notes data
                val noteDataSql = "SELECT $NOTE_NAME, $NOTE_OCTAVE, $NOTE_FREQUENCY FROM " +
                        "$NOTE_TABLE WHERE $NOTE_ID = '$noteId'"
                var noteData = data.rawQuery(noteDataSql, null)
                noteData.moveToNext()
                // get the data from the result and make a new correct note
                var note = noteData.getString(noteData.getColumnIndexOrThrow(NOTE_NAME))
                var octave = noteData.getInt(noteData.getColumnIndexOrThrow(NOTE_OCTAVE))
                var freq = noteData.getFloat(noteData.getColumnIndexOrThrow(NOTE_FREQUENCY)).toFloat()
                var corrNote = CorrectNote(note, octave, freq)
                // add correct note to map
                correctNoteMap.put(note, corrNote)
                println(corrNote)
                noteData.close()
            } while (strings.moveToNext())
            data.close()
        }
        return correctNoteMap
    }

    /**
     * Query the database to return all stored instruments in the database.
     */
    fun getAvailableInstrumentsFromDB() : ArrayList<Instrument>  {
        var instrumentsArray = ArrayList<Instrument>()
        val sqlGetInstrument = "SELECT $INSTR_ID, $INSTR_NAME FROM $INSTR_TABLE"
        val data = readableDatabase
        val instruments = data.rawQuery(sqlGetInstrument, null)
        if (instruments.moveToFirst())  {
            do {
                var id = instruments.getString(instruments.getColumnIndexOrThrow(INSTR_ID))
                var name = instruments.getString(instruments.getColumnIndexOrThrow(INSTR_NAME))
                var instrument = Instrument(id, name)
                instrumentsArray.add(instrument)
            } while (instruments.moveToNext())
            instruments.close()
        }
        return instrumentsArray
    }

    /** Add custom tuning to database ... CT1, CT2, CT3 ETC */

}