package com.example.imageclassification.presentation.homeactivity

import android.content.Intent
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
import com.example.imageclassification.data.local.UserSessionManager
import com.example.imageclassification.databinding.FragmentClassificationBinding
import com.example.imageclassification.presentation.homeactivity.objectdetection.ImageClassifier
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class ClassificationFragment : Fragment() {

    companion object {
        fun newInstance() = ClassificationFragment()
    }
    @Inject
    lateinit var imageClassifier: ImageClassifier
    @Inject
    lateinit var userSessionManager: UserSessionManager
    private lateinit var viewModel: ClassificationViewModel
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

        binding.LiveCameraTxt.setOnClickListener {
            findNavController().navigate(R.id.navigateToLiveDetection)
        }
        binding.launchGalleryTxt.setOnClickListener {
            getImageFromGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
        binding.takePictureTxt.setOnClickListener {
            captureImage.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }
    private val captureImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.let{
            it.data?.extras?.get("data").let {
                var image = it as Bitmap
                userSessionManager.processedImage = image
                val dimension = Math.min(image.width, image.height)
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                binding.imageIV.setImageBitmap(image)
                image = Bitmap.createScaledBitmap(image, IMG_SIZE, IMG_SIZE, false)
                classifyImage(image)
            }
        }
    }

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.let{ activityResult ->
            activityResult.data?.data.let { uri ->
                var image: Bitmap
                try {
                    image = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                    binding.imageIV.setImageBitmap(image)
                    userSessionManager.processedImage = image
                    val dimension = Math.min(image.width, image.height)
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                    image = Bitmap.createScaledBitmap(image, IMG_SIZE, IMG_SIZE, false)
                    image.let { classifyImage(image) }
                }catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }
    }
    private fun classifyImage(image: Bitmap) {
        val outputArr = imageClassifier.classifyImage(image, SUCCESS_RATE_IMG)
        if(outputArr[0] != -1f){
            findNavController().navigate(R.id.navigateToResultFragment
                , bundleOf("buildingIndex" to outputArr[0].toInt()))}
        else
            binding.undefinedTxt.text = resources.getText(R.string.undefined)
    }
}