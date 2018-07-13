package com.otiasj.att4


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.IOException


class MainActivity : Activity() {

    companion object {
        private const val PWM_PORT = "PWM1"

        // Parameters of the LED Pulse Width Modulation (PWM)
        private const val MIN_DUTY_CYCLE = 0.0
        private const val MAX_DUTY_CYCLE = 100.0
        private const val STEP = 5
        private const val INTERVAL_BETWEEN_STEPS_MS: Long = 50
    }

    private val handler = Handler()
    private lateinit var pwm: Pwm
    private var pulseIsIncreasing = true
    private var activePulseDuration: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())
        Timber.d("OnCreate")

        val manager = PeripheralManager.getInstance()
        try {
            activePulseDuration = MIN_DUTY_CYCLE

            pwm = manager.openPwm(PWM_PORT)

            // Always set frequency and initial duty cycle before enabling PWM
            pwm.setPwmFrequencyHz(1000.0)
            pwm.setPwmDutyCycle(0.0)
            pwm.setEnabled(true)

           handler.post(changePWMRunnable)
        } catch (e: IOException) {
            Timber.e(e,"Error on PeripheralIO API")
        }

    }

    override fun onStop() {
        super.onStop()
        Timber.d("OnStop")
        handler.removeCallbacks(changePWMRunnable)
        pwm?.close()
    }

    private val changePWMRunnable = object : Runnable {
        override fun run() {
            if (pulseIsIncreasing) {
                activePulseDuration += STEP
            } else {
                activePulseDuration -= STEP
            }

            // Bounce activePulseDuration back from the limits
            if (activePulseDuration > MAX_DUTY_CYCLE) {
                activePulseDuration = MAX_DUTY_CYCLE
                pulseIsIncreasing = !pulseIsIncreasing
            } else if (activePulseDuration < MIN_DUTY_CYCLE) {
                activePulseDuration = MIN_DUTY_CYCLE
                pulseIsIncreasing = !pulseIsIncreasing
            }

            try {
                //the duty cycle is a percentage
                pwm.setPwmDutyCycle(activePulseDuration)

                handler.postDelayed(this, INTERVAL_BETWEEN_STEPS_MS)
            } catch (e: IOException) {
                Timber.e(e, "Error on PeripheralIO API")
            }

        }
    }

}
