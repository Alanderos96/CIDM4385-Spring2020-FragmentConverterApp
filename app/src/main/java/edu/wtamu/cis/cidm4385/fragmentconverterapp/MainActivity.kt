package edu.wtamu.cis.cidm4385.fragmentconverterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class MainActivity : FragmentActivity(), TemperatureFragment.TemperatureListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onChipClick(temperatureValue: String){

        //temperature_display_fragment

        //find the temperature display fragment
        val temperatureDisplayFragment: TemperatureDisplayFragment? =
            supportFragmentManager.findFragmentById(R.id.temperature_display_fragment)
            as? TemperatureDisplayFragment
        Log.i("DUDE", "I'm in Activity Main onChipClick")
        if(temperatureDisplayFragment == null){
            Log.i("DUDE", "Couldn't find fragment_temperature_display")
        }else{
            Log.i("DUDE", "FOUND! temperature_display_fragment")
        }

        temperatureDisplayFragment?.setTemperatureText(temperatureValue)

    }
}
