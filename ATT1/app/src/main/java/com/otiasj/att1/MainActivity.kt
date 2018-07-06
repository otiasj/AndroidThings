package com.otiasj.att1


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.IOException


class MainActivity : Activity() {

    private val LED_RED_PIN = "GPIO2_IO02"
    private val INTERVAL_BETWEEN_BLINKS_MS = 50L

    private var ledRed: Gpio? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())
        Timber.d("OnCreate")

        val manager = PeripheralManager.getInstance()
        try {
            val ledRed = manager.openGpio(LED_RED_PIN)
            this.ledRed = ledRed
            ledRed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            handler.post(blinkRunnable)
        } catch (e: IOException) {
            Timber.e(e,"Error on PeripheralIO API")
        }

    }

    override fun onStop() {
        super.onStop()
        Timber.d("OnStop")
        handler.removeCallbacks(blinkRunnable)
        ledRed?.close()
    }

    private val blinkRunnable = object : Runnable {
        override fun run() {
            try {
                ledRed?.let {ledRed ->
                    ledRed.value = !ledRed.value // turn the led on/off
                    handler.postDelayed(this, INTERVAL_BETWEEN_BLINKS_MS)
                }
            } catch (e: IOException) {
                Timber.e(e, "Error on PeripheralIO API")
            }

        }
    }

}
