package com.example.imageclassification.presentation.homeactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.imageclassification.R
import com.example.imageclassification.data.local.buildings
import com.example.imageclassification.databinding.FragmentResultBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    lateinit var resultBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var binding: FragmentResultBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater
            , R.layout.fragment_result, container, false)
        resultBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet as ConstraintLayout)
        binding.buildingIV.setImageResource(R.drawable.ain_shams)
        resultBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.showBuildingDetails.setOnClickListener {
            resultBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.bottomSheet.findViewById<Button>(R.id.hideBuildingDetails).setOnClickListener {
            resultBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        val pos = arguments?.getInt("buildingIndex")

        pos?.let {
            if(pos == -1)
                findNavController().popBackStack()
            showBuildingDetails(pos)
        }
        return binding.root
    }

    private fun showBuildingDetails(pos: Int) {
        binding.bottomSheet.findViewById<TextView>(R.id.buildingDetailsTV).text =
            buildings[pos].name
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}