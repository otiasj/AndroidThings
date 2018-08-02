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

    private val LEDS: Array<String> =  arrayOf("GPIO2_IO01", "GPIO2_IO02")//, "GPIO2_IO01", "GPIO2_IO02", "GPIO2_IO01", "GPIO2_IO02", "GPIO2_IO01", "GPIO2_IO02")
    private val INTERVAL_BETWEEN_BLINKS_MS = 50L

    private var ledGpio: Array<Gpio?> = arrayOfNulls<Gpio>(2)
    private var ledIndex:Int = 0
    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())
        Timber.d("OnCreate")

        val manager = PeripheralManager.getInstance()
        try {
            for ((index, pin) in LEDS.withIndex()) {
                ledGpio[index] = manager.openGpio(pin)
                ledGpio[index]?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            }
            handler.post(blinkRunnable)
        } catch (e: IOException) {
            Timber.e(e,"Error on PeripheralIO API")
        }

    }

    override fun onStop() {
        super.onStop()
        Timber.d("OnStop")
        handler.removeCallbacks(blinkRunnable)
        for (gpio in ledGpio) {
            gpio?.close()
        }
    }

    private val blinkRunnable = object : Runnable {
        override fun run() {
            try {
                ledGpio[ledIndex]?.let {led ->
                    if (led.value) {
                        ledIndex++
                        if (ledIndex >= LEDS.size) {
                            ledIndex = 0
                        }
                    }
                    led.value = !led.value // turn the led on/off
                    handler.postDelayed(this, INTERVAL_BETWEEN_BLINKS_MS)
                }
            } catch (e: IOException) {
                Timber.e(e, "Error on PeripheralIO API")
            }
        }
    }

}
