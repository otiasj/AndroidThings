package com.otiasj.att5


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
import com.leinardi.android.things.pio.SoftPwm
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.IOException


class MainActivity : Activity() {

    companion object {
        private const val PWM1_PORT = "PWM1"
        private const val PWM2_PORT = "PWM2"
        private const val PWM3_SOFT_PORT = "GPIO2_IO02"

        // Parameters of the LED Pulse Width Modulation (PWM)
        private const val MIN_DUTY_CYCLE = 0.0
        private const val MAX_DUTY_CYCLE = 100.0
        private const val STEP = 5
        private const val INTERVAL_BETWEEN_STEPS_MS: Long = 50
    }

    private val handler = Handler()
    private lateinit var pwmRed: Pwm
    private lateinit var pwmGreen: Pwm
    private lateinit var pwmBlue: Pwm
    private var pulseIsIncreasing = true
    private var activePulseDuration: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(DebugTree())
        Timber.d("OnCreate")

        val manager = PeripheralManager.getInstance()
        val portList = manager.pwmList
        if (portList.isEmpty()) {
            Timber.i("No PWM port available on this device.")
        } else {
            Timber.i("List of available ports: $portList")
        }

        activePulseDuration = MIN_DUTY_CYCLE

        try {
            pwmRed = manager.openPwm(PWM1_PORT)

            // Always set frequency and initial duty cycle before enabling PWM
            pwmRed.setPwmFrequencyHz(2000.0)
            pwmRed.setPwmDutyCycle(0.0)
            pwmRed.setEnabled(true)

        } catch (e: IOException) {
            Timber.e(e,"Error on PeripheralIO API")
        }

        try {
            pwmGreen = manager.openPwm(PWM2_PORT)

            // Always set frequency and initial duty cycle before enabling PWM
            pwmGreen.setPwmFrequencyHz(2000.0)
            pwmGreen.setPwmDutyCycle(0.0)
            pwmGreen.setEnabled(true)

        } catch (e: IOException) {
            Timber.e(e,"Error on PeripheralIO API")
        }

        try {
            pwmBlue = SoftPwm.openSoftPwm(PWM3_SOFT_PORT);

            // Always set frequency and initial duty cycle before enabling PWM
            pwmBlue.setPwmFrequencyHz(300.0)
            pwmBlue.setPwmDutyCycle(0.0)
            pwmBlue.setEnabled(true)

        } catch (e: IOException) {
            Timber.e(e,"Error on PeripheralIO API")
        }

        handler.post(changePWMRunnable)
    }

    override fun onStop() {
        super.onStop()
        Timber.d("OnStop")
        handler.removeCallbacks(changePWMRunnable)
        pwmRed?.close()
        pwmGreen?.close()
        pwmBlue?.close()
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
                pwmRed.setPwmDutyCycle(activePulseDuration)
                pwmGreen.setPwmDutyCycle(activePulseDuration)
                pwmBlue.setPwmDutyCycle(activePulseDuration)

                handler.postDelayed(this, INTERVAL_BETWEEN_STEPS_MS)
            } catch (e: IOException) {
                Timber.e(e, "Error on PeripheralIO API")
            }

        }
    }

}
