package com.otiasj.androidthingstest1


import android.app.Activity
import android.os.Bundle
import com.google.android.things.pio.PeripheralManager
import com.otiasj.androidthingstest1.rainbow.HatButton
import com.otiasj.androidthingstest1.rainbow.Led
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.IOException

class MainActivity : Activity() {

    val led = Led()
    val buttons = HatButton()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())
        val manager = PeripheralManager.getInstance()
        Timber.d("Available GPIO: " + manager.gpioList)

        runSafeIO {

            buttons.setOnClickListener(HatButton.ID.A, { _, _, pressed ->
                Unit
                led.turn(Led.Color.RED, pressed)
            })

            buttons.setOnClickListener(HatButton.ID.B, { _, _, pressed ->
                Unit
                led.turn(Led.Color.GREEN, pressed)

            })

            buttons.setOnClickListener(HatButton.ID.C, { _, _, pressed ->
                Unit
                led.turn(Led.Color.BLUE, pressed)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        led.clean()
        buttons.clean()
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
