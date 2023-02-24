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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.imageclassification.R
import com.example.imageclassification.databinding.FragmentClassificationBinding
import com.example.imageclassification.ml.Model
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

@AndroidEntryPoint
class ClassificationFragment : Fragment() {

    companion object {
        fun newInstance() = ClassificationFragment()
    }

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
            if (activity?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                captureImage.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
        binding.launchGalleryTxt.setOnClickListener {
            findNavController().navigate(R.id.navigateToLiveDetection)
            //getImageFromGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
    }
    private val captureImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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
    }
    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.let{
            it.data?.data.let {
                var image: Bitmap
                try {
                    image = MediaStore.Images.Media.getBitmap(activity?.getContentResolver(), it)
                    binding.imageIV.setImageBitmap(image);
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    classifyImage(image);
                }catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }
    }

    fun classifyImage(image: Bitmap) {
        try {
            val model: Model = Model.newInstance(activity?.applicationContext!!)

            // Creates inputs for reference.
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 32, 32, 3), DataType.FLOAT32)
            val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imageSize * imageSize)
            println(image.width)
            println(image.height)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)
            // Runs model inference and gets result.
            val outputs: Model.Outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf("Apple", "Banana", "Orange")
            var buildingID = ""
            if(maxConfidence < 2.5){
                buildingID = "7amra"
            }else{
                buildingID = classes[maxPos]
            }
            findNavController().navigate(R.id.navigateToResultFragment
                , bundleOf("buildingID" to buildingID))
            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }
}