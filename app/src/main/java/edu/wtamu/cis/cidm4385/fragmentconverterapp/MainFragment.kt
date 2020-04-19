package edu.wtamu.cis.cidm4385.fragmentconverterapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip

class MainFragment : Fragment(){
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)

        //use the Fragment Layout File
        val view = inflater.inflate(R.layout.main_fragment, container, false)

//        val chipToggle: Chip? = view?.findViewById(R.id.chipToggle)
//        val toggleButtonCF: ToggleButton? = view?.findViewById(R.id.toggleButtonCF)
//
//        chipToggle?.setOnClickListener { v: View -> onChipClick(v) }
//        toggleButtonCF?.setOnClickListener { v: View -> onSwitchClick(v) }

        return view
    }
}