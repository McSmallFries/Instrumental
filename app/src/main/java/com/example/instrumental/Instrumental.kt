package com.example.instrumental

import android.app.Application
import android.content.Context


/** provides a static way to access db inside the ViewModel */
class Instrumental : Application() {

    init {
        Companion.instance = this
    }

    companion object  {
        fun getContext() : Context  {
            return instance
        }

        lateinit var instance: Instrumental
    }

}