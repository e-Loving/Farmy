package uz.eloving.farmy.ui.shop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.FragmentShopBinding
import uz.eloving.farmy.model.ShopItemModel


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val dialog = AddFragment()
        binding.fabAdd.setOnClickListener { dialog.show(childFragmentManager, "Sotish") }
        return binding.root
    }

    private fun upLoadShopItemImage(imageUri: Uri?) {
        storageReference = FirebaseStorage.getInstance().reference
        imageUri?.let { image ->
            storageReference.child(
                "users/${PrefManager.getUID(requireContext())}${
                    PrefManager.getShopItemCount(
                        requireContext()
                    )
                }"
            ).putFile(image)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Fail", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun startForResult(intent: Intent, requestCode: Int) {
        if (requestCode == 1)
            intent.type = "image/*"
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val data: Intent? = result.data
                upLoadShopItemImage(data?.data)
            }
        }.launch(intent)
    }


    private fun uploadInfo() {
        databaseReference = FirebaseDatabase.getInstance().getReference("discount")
        databaseReference.child(
            PrefManager.getUID(requireContext()).toString() + PrefManager.getShopItemCount(
                requireContext()
            )
        )
            .setValue(ShopItemModel("Telefon", 12_000, 0))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    PrefManager.setShopItemCount(requireContext())
                    startForResult(Intent(Intent.ACTION_PICK), 1)
                } else {
                    Toast.makeText(requireContext(), "Failed with info", Toast.LENGTH_SHORT).show()
                }
            }
    }
}