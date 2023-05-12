package com.example.imageclassification.presentation.homeactivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ThumbnailUtils
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageclassification.R
import com.example.imageclassification.data.local.IMG_SIZE
import com.example.imageclassification.data.local.SUCCESS_RATE_CAMERA
import com.example.imageclassification.data.local.UserSessionManager
import com.example.imageclassification.data.local.buildings
import com.example.imageclassification.databinding.FragmentLiveDetectionBinding
import com.example.imageclassification.presentation.homeactivity.objectdetection.ImageClassifier
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class LiveDetectionFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }
    @Inject
    lateinit var imageClassifier: ImageClassifier
    @Inject
    lateinit var userSessionManager: UserSessionManager
    private lateinit var bitmapBuffer: Bitmap
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var preview: Preview? = null
    private lateinit var cameraExecutor: ExecutorService
    private var cameraProvider: ProcessCameraProvider? = null
    private val REQUIRED_PERMISSIONS = mutableListOf (Manifest.permission.CAMERA).toTypedArray()
    private val REQUEST_CODE_PERMISSIONS = 10
    private lateinit var viewModel: LiveDetectionViewModel
    lateinit var binding: FragmentLiveDetectionBinding
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    lateinit var buildingsBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var buidlingsAdpater: BuildingsRecyclerAdapter
    private lateinit var snackbar: Snackbar
    private var lastDetectionRatio = 0.0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_detection, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LiveDetectionViewModel::class.java)
        binding.lifecycleOwner = this
        setUpBottomView()
        setUpBuildingAdapter()
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.cameraPreview.post { setUpCamera() }
    }

    private fun setUpBuildingAdapter() {
        buidlingsAdpater = BuildingsRecyclerAdapter({
            buildingsBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }, {
            buildingsBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }, {index, image ->
            image?.let { userSessionManager.processedImage = image }
            findNavController().navigate(R.id.navigateToResultFragment
                , bundleOf("buildingIndex" to index))
        })
        binding.buildingsRV.adapter = buidlingsAdpater
        binding.buildingsRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val swipeHandler = object: SwipeToDeleteCallback(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                buidlingsAdpater.deleteBuilding(viewHolder.bindingAdapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.buildingsRV)
    }

    private fun setUpBottomView() {
        buildingsBottomSheetBehavior = BottomSheetBehavior.from(binding.buildingsBottomSheet)
        buildingsBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun startCamera() {
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview) } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LiveDetectionFragment().apply {
                arguments = Bundle().apply {}
            }
    }
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        preview =
            Preview.Builder()
                .setTargetResolution(Size(1000, 1000))
                .build()
        imageAnalyzer =
            ImageAnalysis.Builder()
                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .setTargetResolution(Size(1000, 1000))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        if (!::bitmapBuffer.isInitialized) {
                            bitmapBuffer = Bitmap.createBitmap(
                                image.width,
                                image.height,
                                Bitmap.Config.ARGB_8888
                            )
                        }
                        try {
                            image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
                            val dimension = image.width.coerceAtMost(image.height)
                            val imageBitmap = ThumbnailUtils.extractThumbnail(bitmapBuffer, dimension, dimension)
                            val imageScaled = Bitmap.createScaledBitmap(imageBitmap, IMG_SIZE, IMG_SIZE, false)
                            imageClassifier.classifyImage(imageScaled, SUCCESS_RATE_CAMERA).let { outputArr ->
                                if(outputArr[1] > 0.9f)
                                    showMessage("اقتربت جداً")
                                else if(outputArr[1] > 0.5f)
                                    showMessage("اقتربت، ركز على المبنى")
                                else
                                    showMessage("لا يمكن تحديده")
                                lastDetectionRatio = outputArr[1]
                                if(outputArr[0] != -1f){
                                    activity?.runOnUiThread { buidlingsAdpater.addBuilding(buildings[outputArr[0].toInt()], imageBitmap) }
                                }
                            }
                        } catch (e: java.lang.Exception){
                            println(e.stackTrace)
                        }
                    }
                }
        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            preview?.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }
    private fun showMessage(message: String) {
        activity?.runOnUiThread { binding.messageTxt.text = message }
    }
}