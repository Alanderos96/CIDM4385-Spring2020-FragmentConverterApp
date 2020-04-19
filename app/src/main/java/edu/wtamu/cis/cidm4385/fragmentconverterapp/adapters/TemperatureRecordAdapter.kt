package edu.wtamu.cis.cidm4385.fragmentconverterapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import edu.wtamu.cis.cidm4385.fragmentconverterapp.R
import edu.wtamu.cis.cidm4385.fragmentconverterapp.TemperatureRecord

class TemperatureRecordAdapter(private val context : Context?,
                               private val db: DatabaseReference) :
        RecyclerView.Adapter<TemperatureRecordAdapter.TRViewHolder>() {

    class TRViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    private val TAG = "DUDE"
    private var tempAddListener: ValueEventListener? = null
    private var childEventListener : ChildEventListener? = null
    private val temperatureRecordsIds = ArrayList<String>()
    private val temperatureRecords = ArrayList<TemperatureRecord>()

    init {

        //VALUE EVENT
        tempAddListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //get TemperateRecords
                for(temperatureRecord in dataSnapshot.children ){
                    val addTemperatureRecord = temperatureRecord.getValue<TemperatureRecord>()
                    if (addTemperatureRecord != null) {
                        temperatureRecords.add(addTemperatureRecord)
                    }
                }

                //Context appContext = getActivity().getApplicationContext()
                Toast.makeText(
                    context, "Records Loaded",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        //CHILD EVENT
        childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                // A new temperature record has been added, add it to the displayed list
                val temperatureRecord = dataSnapshot.getValue<TemperatureRecord>()
                temperatureRecordsIds.add(dataSnapshot.key!!)
                temperatureRecords.add(temperatureRecord!!)
                notifyItemInserted(temperatureRecords.size - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")

                // A temperature record has changed, use the key to determine if we are displaying this
                // temperature record and if so displayed the changed temperature record.
                val temperatureRecord = dataSnapshot.getValue<TemperatureRecord>()
                val temperatureRecordKey = dataSnapshot.key

                // [START_EXCLUDE]
                val temperatureRecordIndex = temperatureRecordsIds.indexOf(temperatureRecordKey)
                if (temperatureRecordIndex > -1 && temperatureRecord != null) {
                    // Replace with the new data
                    temperatureRecords[temperatureRecordIndex] = temperatureRecord

                    // Update the RecyclerView
                    notifyItemChanged(temperatureRecordIndex)
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child: $temperatureRecordKey")
                }
                // [END_EXCLUDE]
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A temperature record has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val temperatureRecordsKey = dataSnapshot.key

                // [START_EXCLUDE]
                val temperatureRecordsIndex = temperatureRecordsIds.indexOf(temperatureRecordsKey)
                if (temperatureRecordsIndex > -1) {
                    // Remove data from the list
                    temperatureRecords.removeAt(temperatureRecordsIndex)
                    temperatureRecords.removeAt(temperatureRecordsIndex)

                    // Update the RecyclerView
                    notifyItemRemoved(temperatureRecordsIndex)
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + temperatureRecordsKey!!)
                }
                // [END_EXCLUDE]
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A temperature record has changed position, use the key to determine if we are
                // displaying this temperature record and if so move it.
                val temperatureRecord = dataSnapshot.getValue<TemperatureRecord>()
                val commentKey = dataSnapshot.key

                // TODO
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        db.addValueEventListener(tempAddListener as ValueEventListener)
        db.addChildEventListener(childEventListener as ChildEventListener)

        this.childEventListener = childEventListener
        this.tempAddListener = tempAddListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TRViewHolder {
        //create new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.temperature_list_fragment, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters

        return TRViewHolder(textView)

    }

    override fun onBindViewHolder(holder: TRViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = temperatureRecords[position].toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = temperatureRecords.size
}