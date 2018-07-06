package com.otiasj.att2


import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.button.ButtonInputDriver
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.IOException


class MainActivity : Activity() {

    private val BUTTON_PIN = "GPIO2_IO01" // pin 29
    private val LED_RED_PIN = "GPIO2_IO02" // pin 31

    private lateinit var ledRed: Gpio
    private lateinit var buttonInputDriver: ButtonInputDriver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())

        val manager = PeripheralManager.getInstance()
        try {
            //let's us control the LED
            ledRed = manager.openGpio(LED_RED_PIN)
            ledRed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

            //initialize the button
            buttonInputDriver = ButtonInputDriver(
                    BUTTON_PIN,
                    Button.LogicState.PRESSED_WHEN_HIGH,
                    KeyEvent.KEYCODE_SPACE)
            buttonInputDriver.register()

        } catch (e: IOException) {
            Timber.e(e, "Error on PeripheralIO API")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            turnLED(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            turnLED(false)
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun turnLED(on: Boolean) {
        ledRed.value = on
    }

    override fun onStop() {
        buttonInputDriver.unregister()
        buttonInputDriver.close()
        ledRed.close()
        super.onStop()
    }

}
