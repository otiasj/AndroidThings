package com.otiasj.androidthingstest1.rainbow

import android.os.Handler
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio

class Led {

    enum class Color {
        ALL,
        RED,
        GREEN,
        BLUE
    }

    private val blinkHandler: Handler = Handler()
    private var redLED: Gpio? = null
    private var greenLED: Gpio? = null
    private var blueLED: Gpio? = null
    private var durationMs: Long = 1000

    fun blink(color: Color = Color.ALL, durationMs: Long = 1000) {
        this.durationMs = durationMs
        open(color)
        blinkHandler.postDelayed(blinkRunnable, durationMs)
    }

    fun turn(color: Color = Color.ALL, on: Boolean) {
        open(color)
        when (color) {
            Color.ALL -> {
                redLED?.value = on
                greenLED?.value = on
                blueLED?.value = on
            }
            Color.RED -> redLED?.value = on
            Color.GREEN -> greenLED?.value = on
            Color.BLUE -> blueLED?.value = on
        }
    }

    fun clean() {
        blinkHandler.removeCallbacks(blinkRunnable)
        redLED?.close()
        greenLED?.close()
        blueLED?.close()
    }

    private fun open(color: Color = Color.ALL) {
        when (color) {
            Color.ALL -> {
                if (redLED == null) {
                    redLED = RainbowHat.openLedRed()
                }
                if (greenLED == null) {
                    greenLED = RainbowHat.openLedGreen()
                }
                if (blueLED == null) {
                    blueLED = RainbowHat.openLedBlue()
                }
            }

            Color.RED -> {
                if (redLED == null) {
                    redLED = RainbowHat.openLedRed()
                }
            }

            Color.GREEN -> {
                if (greenLED == null) {
                    greenLED = RainbowHat.openLedGreen()
                }
            }

            Color.BLUE -> {
                if (blueLED == null) {
                    blueLED = RainbowHat.openLedBlue()
                }
            }
        }
    }

    private val blinkRunnable = object : Runnable {
        override fun run() {
            redLED?.also {
                it.value = !it.value
            }

            greenLED?.also {
                it.value = !it.value
            }

            blueLED?.also {
                it.value = !it.value
            }

            // reschedule the update
            blinkHandler.postDelayed(this, durationMs)
        }
    }
}
