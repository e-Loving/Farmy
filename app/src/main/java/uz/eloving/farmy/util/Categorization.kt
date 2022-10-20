package uz.eloving.farmy.util

import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.PriorityQueue

class Categorization(
    assetManager: AssetManager,
    modelPath: String,
    labelPath: String,
    inputSize: Int,
) {
    private val givenInputSize: Int = inputSize
    private val photoCode = 255.0f
    private val outcome = 3
    private val interpreter: Interpreter
    private val rowLine: List<String>
    private val imagePixelSize: Int = 3
    private val photo = 0
    private val point = 0.4f


    data class Categorization(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F,

        ) {

        override fun toString(): String {
            return "Title = $title,Confidence=$confidence"

        }
    }

    init {
        interpreter = Interpreter(loadModelFile(assetManager, modelPath))
        rowLine = loadableList(assetManager, labelPath)


    }

    private fun loadableList(assetManager: AssetManager, labelPath: String): List<String> {


        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }


    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {

        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

    }

    fun recognizeImages(bitmap: Bitmap): List<Categorization> {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, givenInputSize, givenInputSize, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        val result = Array(1) { FloatArray(rowLine.size) }
        interpreter.run(byteBuffer, result)
        return getSortedResult(result)
    }

    private fun getSortedResult(result: Array<FloatArray>): List<Categorization> {
        val pq = PriorityQueue(outcome,
            kotlin.Comparator<Categorization> { (_, _, confidence1), (_, _, confidence2) ->
                confidence1.compareTo(confidence2) * -1
            }
        )
        for (i in rowLine.indices) {
            val confidence = result[0][i]
            if (confidence >= point) {
                pq.add(
                    Categorization(
                        ""+i,
                        if (rowLine.size > 1) rowLine[i] else "Unknown", confidence
                    )
                )

            }
        }
        val recognitions = ArrayList<Categorization>()
        val recognitionsSize = pq.size.coerceAtMost(outcome)
        for (i in 0 until recognitionsSize) {
            pq.poll()?.let { recognitions.add(it) }
        }

        return recognitions
    }

    private fun convertBitmapToByteBuffer(scaledBitmap: Bitmap?): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * givenInputSize * givenInputSize * imagePixelSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValue = IntArray(givenInputSize * givenInputSize)

        scaledBitmap!!.getPixels(
            intValue,
            0,
            scaledBitmap.width,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height
        )
        var pixel = 0
        for (i in 0 until givenInputSize) {
            for (j in 0 until givenInputSize) {
                val `val` = intValue[pixel++]

                byteBuffer.putFloat((((`val`.shr(16) and 0xFF) - photo) / photoCode))
                byteBuffer.putFloat((((`val`.shr(8) and 0xFF) - photo) / photoCode))
                byteBuffer.putFloat((((`val` and 0xFF) - photo) / photoCode))
            }
        }
        return byteBuffer

    }


}