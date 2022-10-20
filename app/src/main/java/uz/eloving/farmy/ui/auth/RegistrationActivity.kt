package uz.eloving.farmy.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.eloving.farmy.MainActivity
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.ActivityRegistraionBinding
import uz.eloving.farmy.databinding.ActivitySigninBinding
import uz.eloving.farmy.model.ShopItemModel
import uz.eloving.farmy.model.UserModel

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegistraionBinding
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistraionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.sign.setOnClickListener {
            authEmail()
        }
    }

    private fun authEmail() {
        val password = binding.password.editableText.toString()
        val username = binding.username.editableText.toString()
        auth.createUserWithEmailAndPassword("$username@gmail.com", password)
            .addOnCompleteListener(this) { task ->
                Toast.makeText(this, "$username@gmail.com", Toast.LENGTH_SHORT).show()
                if (task.isSuccessful) {
                    databaseReference = FirebaseDatabase.getInstance().getReference("users")
                    databaseReference.child(
                        username
                    ).setValue(
                        intent.getStringExtra("phoneNumber")?.let {
                            UserModel(
                                binding.firstname.text.toString(),
                                binding.lastname.text.toString(),
                                binding.username.text.toString(),
                                binding.password.text.toString(),
                                it
                            )
                        }
                    )
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                            } else {

                            }
                        }
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Bu nom band qilingan", Toast.LENGTH_SHORT).show()
                }
            }
    }
}