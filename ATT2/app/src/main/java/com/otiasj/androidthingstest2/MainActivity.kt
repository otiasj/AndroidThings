package com.otiasj.androidthingstest2


import android.app.Activity
import android.os.Bundle
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())

    }

    override fun onStop() {
        super.onStop()

    }

}
