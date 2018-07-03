package com.otiasj.att0


import android.app.Activity
import android.os.Bundle
import com.google.android.things.pio.PeripheralManager
import com.otiasj.att0.rainbow.*
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.IOException

class MainActivity : Activity() {

    val led = Led()
    val buttons = HatButton()
    val piezo = Piezo()
    val temperatureSensor = TemperatureSensor()
    val topDisplay = TopDisplay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())
        val manager = PeripheralManager.getInstance()
        Timber.d("Available GPIO: " + manager.gpioList)

        runSafeIO {

            buttons.setOnClickListener(HatButton.ID.A, { _, _, pressed ->
                Unit
                led.turn(Led.ID.RED, pressed)
            })

            buttons.setOnClickListener(HatButton.ID.B, { _, _, pressed ->
                Unit
                led.turn(Led.ID.GREEN, pressed)

            })

            buttons.setOnClickListener(HatButton.ID.C, { _, _, pressed ->
                Unit
                led.turn(Led.ID.BLUE, pressed)
            })

            led.blink(Led.ID.STRIP)

            piezo.play(Piezo.Note.DO, 5000)

            val temperature = temperatureSensor.getTemperature()

            topDisplay.displayMessage(temperature.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        led.clean()
        buttons.clean()
        piezo.clean()
        temperatureSensor.clean()
        topDisplay.clean()
    }

    /**
     * Run the passed function in try/catch protecting against [IOException] that are
     * thrown by the RainbowHAT driver methods
     */
    private fun runSafeIO(ioOperation: () -> Unit) {
        try {
            ioOperation()
        } catch (error: IOException) {
            Timber.e(error, "IO Error")
        }
    }


}
