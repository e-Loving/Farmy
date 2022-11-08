package uz.eloving.farmy.ui.shop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.ActivityAddBinding
import uz.eloving.farmy.model.ShopItemModel

class AddActivity : AppCompatActivity() {
    private var uri: Uri? = null
    private lateinit var binding: ActivityAddBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }
        binding.publish.setOnClickListener { uploadInfo() }
        binding.ivBackAdd.setOnClickListener { onBackPressed(); finish() }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val res = data!!.data
                binding.lottieView.visibility = View.GONE
                uri = res
                binding.image.setImageURI(uri)
            }
        }

    private fun uploadInfo() {
        databaseReference = FirebaseDatabase.getInstance().getReference("discount")
        databaseReference.child(
            PrefManager.getUsername(this).toString() + PrefManager.getShopItemCount(
                this
            )
        )
            .setValue(
                ShopItemModel(
                )
            )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    PrefManager.setShopItemCount(this)
                    upLoadShopItemImage(uri)
                } else {
                    Toast.makeText(this, "Failed with info", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun upLoadShopItemImage(imageUri: Uri?) {
        storageReference = FirebaseStorage.getInstance().reference
        imageUri?.let { image ->
            storageReference.child(
                "users/${PrefManager.getUsername(this)}${
                    PrefManager.getShopItemCount(this)
                }"
            ).putFile(image)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}