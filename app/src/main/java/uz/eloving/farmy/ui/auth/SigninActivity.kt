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
import uz.eloving.farmy.databinding.ActivitySigninBinding
import uz.eloving.farmy.model.UserModel

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.ivBackSignIn.setOnClickListener {
            onBackPressed()
        }
        binding.signlogin.setOnClickListener {
            if (intent.getBooleanExtra("reg", false)) {
                Toast.makeText(this, binding.usernamelogin.text.toString(), Toast.LENGTH_SHORT)
                    .show()
                auth.signInWithEmailAndPassword(
                    "${binding.usernamelogin.text.toString()}@gmail.com",
                    binding.passwordlogin.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            PrefManager.saveUsername(
                                this,
                                binding.usernamelogin.text.toString()
                            )
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(
                                baseContext, "Bunday xisob mavjud emas",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                auth.createUserWithEmailAndPassword(
                    "${binding.usernamelogin.text.toString()}@gmail.com",
                    binding.passwordlogin.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("users")
                            databaseReference.child(
                                binding.usernamelogin.text.toString()
                            ).setValue(
                                UserModel(
                                    binding.usernamelogin.text.toString(),
                                    binding.passwordlogin.text.toString(),
                                    intent.getStringExtra("phoneNumber")!!
                                )
                            ).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Muvaffaqiyatli yakunlandi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    PrefManager.saveUsername(
                                        this,
                                        binding.usernamelogin.text.toString()
                                    )
                                } else {
                                    Toast.makeText(this, "Muammo yuzaga keldi", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(this, "Bu nom band qilingan", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}