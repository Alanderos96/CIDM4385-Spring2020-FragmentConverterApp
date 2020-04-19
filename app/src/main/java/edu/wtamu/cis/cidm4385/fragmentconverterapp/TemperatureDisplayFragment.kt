package edu.wtamu.cis.cidm4385.fragmentconverterapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.temperature_display_fragment.*

class TemperatureDisplayFragment:Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.temperature_display_fragment, container, false)
    }

    public fun setTemperatureText(temperature: String){
        textViewTemperatureDisplay.text = temperature
    }
}