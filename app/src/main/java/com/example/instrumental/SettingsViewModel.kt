package com.example.instrumental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

/**
 * The main business logic for the settings part of the system.
 *
 */
class SettingsViewModel : ViewModel() {

    var dbWrapper = DatabaseWrapper(Instrumental.getContext())
    var background : String = "@drawable/bgdark" // default
    lateinit var mutable : MutableLiveData<String>



}