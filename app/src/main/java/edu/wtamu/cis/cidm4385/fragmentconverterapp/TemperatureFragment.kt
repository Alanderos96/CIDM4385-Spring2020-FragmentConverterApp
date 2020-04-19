package edu.wtamu.cis.cidm4385.fragmentconverterapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import android.widget.Toast
import android.content.Context
import android.util.Log
import android.widget.ToggleButton
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import kotlin.text.*
import kotlinx.android.synthetic.main.temperature_fragment.*
import java.lang.ClassCastException
import java.text.DecimalFormat
import java.time.LocalDateTime

class TemperatureFragment : Fragment() {

    var isCelsius = true
    val TAG = "DUDE"

    private lateinit var db: DatabaseReference
    private var tempAddListener: ValueEventListener? = null
    private var childEventListener : ChildEventListener? = null

    var activityCallback: TemperatureFragment.TemperatureListener? = null

    interface TemperatureListener{
        fun onChipClick(temperatureValue: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)

        //firebase realtime database
        db = Firebase.database.reference

        //use the Fragment Layout File
        val view = inflater.inflate(R.layout.temperature_fragment, container, false)

        val chipToggle: Chip? = view?.findViewById(R.id.chipToggle)
        val toggleButtonCF: ToggleButton? = view?.findViewById(R.id.toggleButtonCF)

        chipToggle?.setOnClickListener { v: View -> onChipClick(v) }
        toggleButtonCF?.setOnClickListener { v: View -> onSwitchClick(v) }

        return view
    }

    override fun onStart() {
        super.onStart()

        //event listeners
        //VALUE EVENT
        tempAddListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //get TemperateRecord
                val tempRec = dataSnapshot.getValue<TemperatureRecord>()
                Log.i(TAG, tempRec.toString())
                //Context appContext = getActivity().getApplicationContext()
                Toast.makeText(
                    activity?.applicationContext, "A database change occurred",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                TODO("Not yet implemented")
            }
        }

        //CHILD EVENT
        childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                val temperatureRecord = dataSnapshot.getValue<TemperatureRecord>()

                // ...
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val temperatureRecord = dataSnapshot.getValue<TemperatureRecord>()
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val temperatureRecord = dataSnapshot.getValue<TemperatureRecord>()
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        db.addValueEventListener(tempAddListener as ValueEventListener)
        db.addChildEventListener(childEventListener as ChildEventListener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            activityCallback = context as TemperatureListener
        }catch(e: ClassCastException){
            throw ClassCastException(context?.toString() + " must implement TemperatureListener")
        }
    }

//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        try{
//            activityCallback = context as TemperatureListener
//        }catch(e: ClassCastException){
//            throw ClassCastException(context?.toString() + " must implement TemperatureListener")
//        }
//    }

    private fun onSwitchClick(view: View) {
        if( isCelsius ){
            Log.i(TAG, "switched to Fahrenheit")
            toggleButtonCF?.textOn = "To Fahrenheit"
            isCelsius = false
        }else{
            Log.i(TAG, "switched to Celsius")
            toggleButtonCF?.textOff = "To Celsius"
            isCelsius = true
        }
    }

    private fun onChipClick(view: View){
        Log.i(TAG, "It happened")
        //text
        val input = editTextTemperature.text.toString()
        var isNumeric: Boolean = true
        var displayTemp: String = ""
        var conversionType: String = ""

        //credit: https://www.programiz.com/kotlin-programming/examples/check-string-numeric
        isNumeric = input.matches("-?\\d+(\\.\\d+)?".toRegex())

        if(isNumeric) {
            val inputNumber = input.toInt();
            val df = DecimalFormat("#.#")

            if(isCelsius){
                val result = CtoF(inputNumber)
                conversionType = "CtoF"
                displayTemp = df.format(result)
                displayTemp += " F"
                LogToFirebase(displayTemp, conversionType, LocalDateTime.now())
            }
            else{
                val result = FtoC(inputNumber)
                conversionType = "FtoC"
                displayTemp = df.format(result)
                displayTemp += " C"
                LogToFirebase(displayTemp, conversionType, LocalDateTime.now())
            }

        }else{
            Log.i(TAG, "That was not a number")
        }

        //call activity
        activityCallback?.onChipClick(displayTemp)
    }

    private fun CtoF(temp: Int): Float{
        //https://www.rapidtables.com/convert/temperature/how-celsius-to-fahrenheit.html
        return temp * (9f / 5f) + 32
    }

    private fun FtoC(temp: Int): Float{
        //https://www.rapidtables.com/convert/temperature/how-fahrenheit-to-celsius.html
        return (temp - 32) * (5f / 9f)
    }

    private fun LogToFirebase(temp: String, type: String, timestamp: LocalDateTime){

        Log.i(TAG, "IN LOG TO FIREBASE")
        val tr =
            TemperatureRecord(
                temp,
                type
            )
        Log.i(TAG, "Temperature Record: $tr" )

        db.child("conversions").push().setValue(tr)
            .addOnSuccessListener {
                // Write was successful!
                // ...
                Toast.makeText(
                    activity?.applicationContext, "Temperate Written: $tr",
                    Toast.LENGTH_SHORT).show()                
            }
            .addOnFailureListener {
                // Write failed
                // ...
                Toast.makeText(
                    activity?.applicationContext, "Temperate COULD NOT BE WRITTEN: $tr",
                    Toast.LENGTH_SHORT).show()
            }
        //myRef.setValue(TemperatureRecord(temp, type))
    }
}