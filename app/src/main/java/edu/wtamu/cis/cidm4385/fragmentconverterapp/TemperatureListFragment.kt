package edu.wtamu.cis.cidm4385.fragmentconverterapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import edu.wtamu.cis.cidm4385.fragmentconverterapp.adapters.TemperatureRecordAdapter

class TemperatureListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        //use the Fragment Layout File
        val view = inflater.inflate(R.layout.temperature_list_fragment, container, false)

        //firebase realtime database
        db = Firebase.database.reference

        viewManager = LinearLayoutManager(activity?.applicationContext)
        viewAdapter = TemperatureRecordAdapter(activity?.applicationContext, db)

        recyclerView = view?.findViewById(R.id.temperature_list)!!
        recyclerView.apply {

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter
            adapter = viewAdapter
        }

        return view
    }
}