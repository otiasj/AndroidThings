package com.otiasj.att0.rainbow

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

class TopDisplay {

    private var alphanumericDisplay: AlphanumericDisplay? = null

    fun displayMessage(message: String) {
        if (alphanumericDisplay == null) {
            alphanumericDisplay = RainbowHat.openDisplay();
        }

        alphanumericDisplay?.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX);
        alphanumericDisplay?.display(message);
        alphanumericDisplay?.setEnabled(true);
    }

    fun clean() {
        alphanumericDisplay?.close()
    }
}