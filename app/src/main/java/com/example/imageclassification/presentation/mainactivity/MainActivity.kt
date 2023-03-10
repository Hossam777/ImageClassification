package com.example.imageclassification.presentation.mainactivity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.data.local.IMG_SIZE
import com.example.imageclassification.data.local.buildings
import com.example.imageclassification.databinding.ActivityMainBinding
import com.example.imageclassification.ml.Buildings
import com.example.imageclassification.ml.Model
import com.example.imageclassification.ml.Model2
import com.example.imageclassification.presentation.authenticationactivities.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : BaseActivity(){
    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        binding.continueBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        val image = resources.getDrawable(R.drawable.img2).toBitmap(224, 224)
        println(image.getPixel(0, 0).red.toFloat())
        println(image.getPixel(0, 0).green.toFloat())
        println(image.getPixel(0, 0).blue.toFloat())
        image.let {
            try {
                val inputFeature0 =
                    TensorBuffer.createFixedSize(
                        intArrayOf(1, IMG_SIZE, IMG_SIZE, 3),
                        DataType.FLOAT32
                    )
                val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * IMG_SIZE * IMG_SIZE * 3)
                byteBuffer.order(ByteOrder.nativeOrder())
                for (i in 0 until IMG_SIZE) {
                    for (j in 0 until IMG_SIZE) {
                        byteBuffer.putFloat(it.getPixel(i, j).red.toFloat())
                        byteBuffer.putFloat(it.getPixel(i, j).green.toFloat())
                        byteBuffer.putFloat(it.getPixel(i, j).blue.toFloat())
                    }
                }
                inputFeature0.loadBuffer(byteBuffer)
                binding.logoIV.setImageBitmap(it)
                println(inputFeature0.floatArray.size)
                println(inputFeature0.floatArray.joinToString(", "))
                val model = Model.newInstance(this)
                val outputs: Model.Outputs = model.process(inputFeature0)
                val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
                val confidences = outputFeature0.floatArray
                println(confidences.joinToString(", "))
                println("-------------------------")
                val model2 = Model2.newInstance(this)
                val Outputs2: Model2.Outputs = model2.process(inputFeature0)
                val OutputFeature02: TensorBuffer = Outputs2.outputFeature0AsTensorBuffer
                val Confidences2 = OutputFeature02.floatArray
                println(Confidences2.joinToString(", "))
                println("-------------------------")
                val bModel = Buildings.newInstance(this)
                val bOutputs: Buildings.Outputs = bModel.process(inputFeature0)
                val bOutputFeature0: TensorBuffer = bOutputs.outputFeature0AsTensorBuffer
                val bConfidences = bOutputFeature0.floatArray
                println(bConfidences.joinToString(", "))
                println("-------------------------")
                model.close()
                model2.close()
                bModel.close()
            }catch (e: java.lang.Exception){
                println(e.stackTrace)
            }
        }
    }
}