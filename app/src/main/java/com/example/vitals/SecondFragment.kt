package com.example.vitals

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vitals.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private var sensorInstance: Sensor? = null
    private var isAwaitingTap = false

    /**
     * onSensorChanged is called when the Motion Sensor value
     * is changed and then run the algorithm to detect your desired motion.
     *
     * @return void
     */
    private val sensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if(!isAwaitingTap)
            {
                return;
            }

            val acc = Math.sqrt(
                Math.pow(event.values[0].toDouble(), 2.0)
                        + Math.pow(event.values[1].toDouble(), 2.0)
                        + Math.pow(event.values[2].toDouble(), 2.0)
            )

            if (acc > 40) {
                isAwaitingTap = false;
                onTapTapDetected();
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            // Nothing to do
        }
    }

    private fun onTapTapDetected() {
        Log.i("MainActivity", "Tap Detected")
    }

    private fun initSensorObject() {
        val sensorMgr = this.activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        sensorInstance = sensorMgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorMgr.registerListener(sensorEventListener, sensorInstance, 5000)
    }

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        initSensorObject();
    }

    override fun onResume() {
        super.onResume()
        this.isAwaitingTap = true;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}