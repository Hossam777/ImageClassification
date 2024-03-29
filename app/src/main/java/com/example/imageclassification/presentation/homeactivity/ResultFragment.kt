package com.example.imageclassification.presentation.homeactivity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.imageclassification.R
import com.example.imageclassification.data.local.UserSessionManager
import com.example.imageclassification.data.local.buildings
import com.example.imageclassification.databinding.FragmentResultBinding
import com.example.imageclassification.presentation2.BuidlingDetailsActivity
import com.example.imageclassification.presentation2.BuildingsActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultFragment : Fragment() {

    @Inject
    lateinit var userSessionManager: UserSessionManager
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
        /*resultBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet as ConstraintLayout)
        resultBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.showBuildingDetails.setOnClickListener {
            resultBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.bottomSheet.findViewById<Button>(R.id.hideBuildingDetails).setOnClickListener {
            resultBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }*/
        val pos = arguments?.getInt("buildingIndex")
        pos?.let {
            if(pos == -1){
                Toast.makeText(requireContext(), "لا ", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()

            }
            else{
                showBuildingDetails(pos)
                binding.showBuildingDetails.setOnClickListener {
                    val intent = Intent(activity, BuidlingDetailsActivity::class.java)
                    intent.putExtra("buildingId", pos)
                    startActivity(intent)
                }
            }
        }
        return binding.root
    }

    private fun showBuildingDetails(pos: Int) {
        binding.buildingIV.setImageResource(buildings[pos].photo)
        //binding.bottomSheet.findViewById<TextView>(R.id.buildingDetailsTV).text = buildings[pos].name
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}