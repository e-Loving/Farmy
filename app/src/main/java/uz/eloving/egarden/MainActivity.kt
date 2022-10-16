package uz.eloving.egarden

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import uz.eloving.egarden.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var categorization: Categorization
    private lateinit var bitmap: Bitmap
    private lateinit var binding: ActivityMainBinding
    private var globalCode = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categorization =
            Categorization(
                assets,
                "plant_disease_model.tflite",
                "plant_labels.txt",
                224
            )
        resources.assets.open("autumn.jpg").use {
            bitmap = BitmapFactory.decodeStream(it)
            bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            binding.ivMain.setImageBitmap(bitmap)
        }
        binding.btnCamera.setOnClickListener {
            startForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0)
        }
        binding.btnGallery.setOnClickListener {
            startForResult(Intent(Intent.ACTION_PICK), 1)
        }
        binding.btnDetect.setOnClickListener {
            val results = categorization.recognizeImages(bitmap).firstOrNull()
            binding.tvResult.text =
                results?.title + "\n Confidence:" + results?.confidence

        }
    }

    private fun startForResult(intent: Intent, requestCode: Int) {
        if (requestCode == 1)
            intent.type = "image/*"
        globalCode = requestCode
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                when (globalCode) {
                    0 -> {
                        bitmap = data?.extras!!.get("data") as Bitmap
                        binding.ivMain.setImageBitmap(bitmap)
                    }
                    1 -> {
                        bitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, data!!.data)
                        binding.ivMain.setImageBitmap(bitmap)
                    }
                }
            }
        }
}