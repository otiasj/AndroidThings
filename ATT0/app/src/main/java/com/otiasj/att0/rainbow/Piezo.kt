package com.otiasj.att0.rainbow

import android.os.Handler
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

class Piezo {

    enum class Note(val frequency: Double) {
        E5(659.255), DO(659.255),
        D5(587.33), RE(587.33),
        C5(523.251), MI(523.251),
        B4(493.883), FA(493.883),
        A4(440.0), SOL(440.0),
        G4(391.995), LA(391.995),
        F4(349.228), SI(349.228),
        E4(329.628), DO2(329.628)
    }

    private val noteHandler: Handler = Handler()
    private var durationMs: Long = 1000
    private var piezo: Speaker? = null

    //Play note until stop is called
    fun play(note: Note) {
        if (piezo == null) {
            piezo = RainbowHat.openPiezo()
        }

        piezo?.let {
            it.play(note.frequency)
        }
    }

    /**
     * Play for the given duration
     */
    fun play(note: Note, durationMs: Long) {
        this.durationMs = durationMs

        play(note)
        noteHandler.postDelayed(noteRunnable, durationMs)
    }

    fun stop() {
        piezo?.stop()
    }

    fun clean() {
        noteHandler.removeCallbacks(noteRunnable)
        piezo?.stop()
        piezo?.close()
    }

    private val noteRunnable = Runnable { stop() }
}