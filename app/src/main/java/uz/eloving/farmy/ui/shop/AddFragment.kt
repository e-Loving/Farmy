package uz.eloving.farmy.ui.shop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import me.ibrahimsn.lib.SmoothBottomBar
import uz.eloving.farmy.R
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.FragmentAddBinding
import uz.eloving.farmy.model.ShopItemModel
import uz.eloving.farmy.ui.MainActivity
import uz.eloving.farmy.util.ProgressDialog
import uz.eloving.farmy.util.show

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private var uri: Uri? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var dialog: ProgressDialog
    private var categories =
        arrayOf("Mevalar", "Sabzavotlar", "Poliz-ekinlari", "Sut mahsulotlari")
    private var amount = arrayOf("Kilo", "Dona", "Litr")
    private var imgChanged = false
    private var downloadUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        arrayOf(binding.imageView, binding.lottieView, binding.image).forEach {
            it.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                resultLauncher.launch(intent)
            }
        }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.item_spinner)
        binding.spinnerCategory.adapter = adapter
        binding.spinnerCategory.setSelection(1)
        val amountAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, amount)
        adapter.setDropDownViewResource(R.layout.item_spinner)
        binding.spinnerAmount.adapter = amountAdapter
        binding.spinnerAmount.setSelection(1)
        binding.btnPublish.setOnClickListener { upLoadShopItemImage(uri) }
        binding.ivBackAdd.setOnClickListener { onBackPressed() }
        return binding.root
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val res = data!!.data
                binding.lottieView.visibility = View.GONE
                uri = res
                imgChanged = true
                binding.image.setImageURI(uri)
            }
        }

    private fun uploadInfo() {
        databaseReference = FirebaseDatabase.getInstance()
            .getReference(binding.spinnerCategory.selectedItem.toString())
        databaseReference.child(username() + num()).setValue(
            ShopItemModel(
                binding.etName.text.toString(),
                binding.etPrice.text.toString().toFloat(),
                downloadUrl,
                binding.spinnerCategory.selectedItem.toString(),
                binding.spinnerAmount.selectedItem.toString()
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) onBackPressed()
            else "Xatolik yuz berdi".asToast()
        }
    }

    private fun upLoadShopItemImage(imageUri: Uri?) {
        if (binding.etName.text.toString().isNotEmpty() && binding.etPrice.text.toString()
                .isNotEmpty() && imgChanged
        ) {
            dialog = ProgressDialog()
            dialog.show(parentFragmentManager)
            storageReference = FirebaseStorage.getInstance().reference.child("users")
            imageUri?.let { image ->
                storageReference.child(username() + num() + ".png").putFile(image)
                storageReference.child(username() + num() + ".png").downloadUrl.addOnSuccessListener {
                    downloadUrl = it.toString()
                    uploadInfo()
                    PrefManager.setShopItemCount(requireContext())
                }
            }
        } else "Xatolik yuz berdi".asToast()
    }

    private fun onBackPressed() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
    }

    private fun String.asToast() = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    private fun username() = PrefManager.getUsername(requireContext()).toString()
    private fun num() = PrefManager.getShopItemCount(requireContext()).toString()
}