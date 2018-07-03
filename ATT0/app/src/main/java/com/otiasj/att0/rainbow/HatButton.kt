package com.otiasj.att0.rainbow

import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

class HatButton {

    enum class ID {
        ALL,
        A,
        B,
        C
    }

    private var buttonA: Button? = null
    private var buttonB: Button? = null
    private var buttonC: Button? = null

    fun setOnClickListener(button: ID = ID.ALL, onClick: (id: ID, button: Button, pressed: Boolean) -> Unit) {
        when (button) {
            ID.ALL -> {
                val buttonA = RainbowHat.openButtonA()
                this.buttonA = buttonA
                buttonA.setOnButtonEventListener { clickedButton, pressed -> onClick(ID.A, clickedButton, pressed) }
                val buttonB = RainbowHat.openButtonB()
                this.buttonB = buttonB
                buttonB.setOnButtonEventListener { clickedButton, pressed -> onClick(ID.B, clickedButton, pressed) }
                val buttonC = RainbowHat.openButtonC()
                this.buttonC = buttonC
                buttonC.setOnButtonEventListener { clickedButton, pressed -> onClick(ID.C, clickedButton, pressed) }
            }
            ID.A -> {
                val buttonA = RainbowHat.openButtonA()
                this.buttonA = buttonA
                buttonA.setOnButtonEventListener { clickedButton, pressed -> onClick(ID.A, clickedButton, pressed) }
            }
            ID.B -> {
                val buttonB = RainbowHat.openButtonB()
                this.buttonB = buttonB
                buttonB.setOnButtonEventListener { clickedButton, pressed -> onClick(ID.B, clickedButton, pressed) }
            }
            ID.C -> {
                val buttonC = RainbowHat.openButtonC()
                this.buttonC = buttonC
                buttonC.setOnButtonEventListener { clickedButton, pressed -> onClick(ID.C, clickedButton, pressed) }
            }
        }
    }

    fun clean() {
        buttonA?.close()
        buttonB?.close()
        buttonC?.close()
    }
}
