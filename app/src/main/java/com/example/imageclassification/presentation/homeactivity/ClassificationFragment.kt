package com.example.imageclassification.presentation.homeactivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.imageclassification.R
import com.example.imageclassification.data.local.IMG_SIZE
import com.example.imageclassification.data.local.SUCCESS_RATE_IMG
import com.example.imageclassification.databinding.FragmentClassificationBinding
import com.example.imageclassification.ml.Model
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

@AndroidEntryPoint
class ClassificationFragment : Fragment() {

    companion object {
        fun newInstance() = ClassificationFragment()
    }
    @Inject
    lateinit var imageClassifier: ImageClassifier
    private lateinit var viewModel: ClassificationViewModel
    val imageSize = 32
    lateinit var binding: FragmentClassificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classification, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ClassificationViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.takePictureTxt.setOnClickListener {
            findNavController().navigate(R.id.navigateToLiveDetection)
        }
        binding.launchGalleryTxt.setOnClickListener {
            getImageFromGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
    }
    /*private val captureImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.let{
            it.data?.extras?.get("data").let {
                var image = it as Bitmap
                val dimension = Math.min(image.width, image.height)
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                binding.imageIV.setImageBitmap(image)
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                classifyImage(image)
            }
        }
    }*/
    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.let{ activityResult ->
            activityResult.data?.data.let { uri ->
                var image: Bitmap
                try {
                    image = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                    binding.imageIV.setImageBitmap(image)
                    val dimension = Math.min(image.width, image.height)
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                    image = Bitmap.createScaledBitmap(image, IMG_SIZE, IMG_SIZE, false)
                    val succRate = imageClassifier.classifyImage(image, SUCCESS_RATE_IMG)
                    if(succRate != -1){
                    findNavController().navigate(R.id.navigateToResultFragment
                        , bundleOf("buildingIndex" to succRate))}
                    else
                        Toast.makeText(requireContext(), "Undefined", Toast.LENGTH_SHORT).show()
                }catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }
    }
}