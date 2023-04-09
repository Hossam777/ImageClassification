package com.example.imageclassification.presentation.homeactivity.objectdetection

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.imageclassification.data.local.IMG_SIZE
import com.example.imageclassification.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassifier(context: Context) {
    private var model: Model
    init {
        model = Model.newInstance(context)
    }
    fun classifyImage(image: Bitmap, successRate: Float): FloatArray {
        try {
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, IMG_SIZE, IMG_SIZE, 3), DataType.FLOAT32)
            val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * IMG_SIZE * IMG_SIZE * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            //val intValues = IntArray(IMG_SIZE * IMG_SIZE)
            //image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            for (i in 0 until IMG_SIZE) {
                for (j in 0 until IMG_SIZE) {
                    byteBuffer.putFloat(image.getPixel(i, j).red.toFloat())
                    byteBuffer.putFloat(image.getPixel(i, j).green.toFloat())
                    byteBuffer.putFloat(image.getPixel(i, j).blue.toFloat())
                }
            }
            inputFeature0.loadBuffer(byteBuffer)
            //println(inputFeature0.floatArray.joinToString(","))
            val outputs: Model.Outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            println("values : " + confidences.joinToString(", "))
            //println("max : " + maxConfidence)
            //println("--------------------------------------")
            return if(maxConfidence < successRate){
                floatArrayOf(-1f, maxConfidence)
            }else{
                floatArrayOf(maxPos.toFloat(), maxConfidence)
            }
        } catch (e: IOException) {
            println(e.localizedMessage)
        }
        return floatArrayOf(-1f, 0f)
    }
}