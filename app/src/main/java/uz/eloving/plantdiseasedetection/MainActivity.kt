package uz.eloving.plantdiseasedetection

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Toast
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar
import uz.eloving.plantdiseasedetection.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var mCategorization: Categorization
    private lateinit var mBitmap: Bitmap
    private val mCameraRequestCode = 0
    private val mGalleryRequestCode = 2
    private val mInputSize = 224
    private val mModelPath = "plant_disease_model.tflite"
    private val mLabelPath = "plant_labels.txt"
    private val mSamplePath = "automn.jpg"
    private lateinit var binding: ActivityMainBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mCategorization = Categorization(assets, mModelPath, mLabelPath, mInputSize)
        resources.assets.open(mSamplePath).use {
            mBitmap = BitmapFactory.decodeStream(it)
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
            binding.mPhotoImageView.setImageBitmap(mBitmap)
        }
        val bottomnav = findViewById<SmoothBottomBar>(R.id.bottom_nav)
        bottomnav.setOnItemReselectedListener {
            object : OnItemSelectedListener {
                override fun onItemSelect(pos: Int): Boolean {
                    when (pos) {
                        1 -> {
                            startActivity(Intent(this@MainActivity, CommonRemedies::class.java))
                            return true
                        }
                        2 -> {
                            startActivity(Intent(this@MainActivity, AboutUs::class.java))
                            return true
                        }
                    }
                    return false
                }
            }
        }
        binding.mCameraButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, mCameraRequestCode)
        }
        binding.mGalleryButton.setOnClickListener {
            val callGalleryIntent = Intent(Intent.ACTION_PICK)
            callGalleryIntent.type = "image/*"
            startActivityForResult(callGalleryIntent, mGalleryRequestCode)
        }
        binding.mDetectButton.setOnClickListener {
            val progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setTitle("Please wait")
            progressDialog.setMessage("Wait there I doing something")
            progressDialog.show()
            val handler = Handler()
            handler.postDelayed({
                progressDialog.dismiss()
                val results = mCategorization.recognizeImages(mBitmap).firstOrNull()
                binding.mResultTextView.text = results?.title + "\n Confidence:" + results?.confidence
            }, 2000)
        }

    }

    @SuppressLint("SetTextI18n")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = data.extras!!.get("data") as Bitmap
//                mBitmap = scaleImage(mBitmap)
                binding.mPhotoImageView.setImageBitmap(mBitmap)
                binding.mResultTextView.text = "You photo image set now"
            } else {
                Toast.makeText(this@MainActivity, "Camera cancelled", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == mGalleryRequestCode) {
            if (data != null) {
                val uri = data.data
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
//                mBitmap = scaleImage(mBitmap)
                binding.mPhotoImageView.setImageBitmap(mBitmap)
            }
        }
    }

//    private fun scaleImage(bitmap: Bitmap): Bitmap {
//        val originalWidth = bitmap.width
//        val originalHeight = bitmap.height
//        val scaleWidth = mInputSize.toFloat()
//        val scaleHeight = mInputSize.toFloat()
//        val matrix = Matrix()
//        matrix.postScale(scaleWidth, scaleHeight)
//        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true)
//    }
}