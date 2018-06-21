package com.otiasj.androidthingstest1.rainbow

import android.graphics.Color
import android.os.Handler
import com.google.android.things.contrib.driver.apa102.Apa102
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio

class Led {

    enum class ID {
        ALL,
        RED,
        GREEN,
        BLUE,
        STRIP
    }

    private val blinkHandler: Handler = Handler()
    private var redLED: Gpio? = null
    private var greenLED: Gpio? = null
    private var blueLED: Gpio? = null
    private var stripLED: Apa102? = null
    private var durationMs: Long = 1000
    var stripColors: IntArray = intArrayOf(Color.RED, Color.CYAN, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.WHITE)
    private val stripLedOff: IntArray = intArrayOf(-1, -1, -1, -1, -1, -1, -1)

    fun blink(ID: ID = Led.ID.ALL, durationMs: Long = 1000) {
        this.durationMs = durationMs
        open(ID)
        blinkHandler.postDelayed(blinkRunnable, durationMs)
    }

    fun turn(ID: ID = Led.ID.ALL, on: Boolean) {
        open(ID)
        when (ID) {
            Led.ID.ALL -> {
                redLED?.value = on
                greenLED?.value = on
                blueLED?.value = on
                if (on) {
                    stripLED?.write(stripColors)
                } else {
                    stripLED?.write(stripLedOff)
                }
            }
            Led.ID.RED -> redLED?.value = on
            Led.ID.GREEN -> greenLED?.value = on
            Led.ID.BLUE -> blueLED?.value = on
        }
    }

    fun clean() {
        blinkHandler.removeCallbacks(blinkRunnable)
        turn(ID.ALL, false)
        redLED?.close()
        greenLED?.close()
        blueLED?.close()
        stripLED?.close()
    }

    private fun open(ID: ID = Led.ID.ALL) {
        when (ID) {
            Led.ID.ALL -> {
                if (redLED == null) {
                    redLED = RainbowHat.openLedRed()
                }
                if (greenLED == null) {
                    greenLED = RainbowHat.openLedGreen()
                }
                if (blueLED == null) {
                    blueLED = RainbowHat.openLedBlue()
                }
                if (stripLED == null) {
                    stripLED = RainbowHat.openLedStrip()
                }
            }

            Led.ID.RED -> {
                if (redLED == null) {
                    redLED = RainbowHat.openLedRed()
                }
            }

            Led.ID.GREEN -> {
                if (greenLED == null) {
                    greenLED = RainbowHat.openLedGreen()
                }
            }

            Led.ID.BLUE -> {
                if (blueLED == null) {
                    blueLED = RainbowHat.openLedBlue()
                }
            }

            Led.ID.STRIP -> {
                if (stripLED == null) {
                    stripLED = RainbowHat.openLedStrip()
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

            stripLED?.also {
                stripColors = intArrayOf(stripColors.last()) + stripColors.dropLast(1)
                it.write(stripColors)
            }

            // reschedule the update
            blinkHandler.postDelayed(this, durationMs)
        }
    }
}
