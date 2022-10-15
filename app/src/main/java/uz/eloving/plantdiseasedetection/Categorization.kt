package uz.eloving.plantdiseasedetection

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
    private val GVN_INP_SZ: Int = inputSize
    private val PHOTO_SDEVIATE = 225.0f
    private val GREAT_OUTCOME_MXX = 3
    private val PITNR: Interpreter
    private val ROW_LINE: List<String>
    private val IMAGE_PXL_SZ: Int = 3
    private val PHOTO_MEN = 0
    private val POINT_THRHOLD = 0.4f


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
        PITNR = Interpreter(loadModelFile(assetManager, modelPath))
        ROW_LINE = loadableList(assetManager, labelPath)


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
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, GVN_INP_SZ, GVN_INP_SZ, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        val result = Array(1) { FloatArray(ROW_LINE.size) }
        PITNR.run(byteBuffer, result)
        return getSortedResult(result)
    }

    private fun getSortedResult(result: Array<FloatArray>): List<Categorization> {
        val pq = PriorityQueue(GREAT_OUTCOME_MXX,
            kotlin.Comparator<Categorization> { (_, _, confidence1), (_, _, confidence2) ->
                confidence1.compareTo(confidence2) * -1
            }
        )
        for (i in ROW_LINE.indices) {
            val confidence = result[0][1]
            if (confidence >= POINT_THRHOLD) {
                pq.add(
                    Categorization(
                        ""+i,
                        if (ROW_LINE.size > 1) ROW_LINE[i] else "Unknown", confidence
                    )
                )

            }
        }
        val recognitions = ArrayList<Categorization>()
        val recognitionsSize = pq.size.coerceAtMost(GREAT_OUTCOME_MXX)
        for (i in 0 until recognitionsSize) {
            pq.poll()?.let { recognitions.add(it) }
        }

        return recognitions
    }

    private fun convertBitmapToByteBuffer(scaledBitmap: Bitmap?): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * GVN_INP_SZ * GVN_INP_SZ * IMAGE_PXL_SZ)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValue = IntArray(GVN_INP_SZ * GVN_INP_SZ)

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
        for (i in 0 until GVN_INP_SZ) {
            for (j in 0 until GVN_INP_SZ) {
                val `val` = intValue[pixel++]

                byteBuffer.putFloat((((`val`.shr(16) and 0xFF) - PHOTO_MEN) / PHOTO_SDEVIATE))
                byteBuffer.putFloat((((`val`.shr(8) and 0xFF) - PHOTO_MEN) / PHOTO_SDEVIATE))
                byteBuffer.putFloat((((`val` and 0xFF) - PHOTO_MEN) / PHOTO_SDEVIATE))
            }
        }
        return byteBuffer

    }


}