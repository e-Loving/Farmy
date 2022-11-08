package uz.eloving.farmy.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.eloving.farmy.MainActivity
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.ActivitySigninBinding
import uz.eloving.farmy.databinding.DialogProgressBinding
import uz.eloving.farmy.model.UserModel
import uz.eloving.farmy.util.ProgressDialog
import uz.eloving.farmy.util.hide
import uz.eloving.farmy.util.show

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val dialog = ProgressDialog()
        binding.ivBackSignIn.setOnClickListener {
            onBackPressed()
        }
        binding.signlogin.setOnClickListener {
            if (binding.usernamelogin.text?.length!! > 3 && binding.passwordlogin.text?.length!! > 6) {
                if (intent.getBooleanExtra("reg", false)) {
                    dialog.show(supportFragmentManager)
                    auth.signInWithEmailAndPassword(
                        "${binding.usernamelogin.text}@gmail.com",
                        binding.passwordlogin.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                PrefManager.saveUsername(
                                    this,
                                    binding.usernamelogin.text.toString()
                                )
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    baseContext, "Bunday xisob mavjud emas",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.hide()
                            }
                        }
                } else {
                    dialog.show(supportFragmentManager)
                    auth.createUserWithEmailAndPassword(
                        "${binding.usernamelogin.text}@gmail.com",
                        binding.passwordlogin.text.toString()
                    ).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            databaseReference =
                                FirebaseDatabase.getInstance().getReference("users")
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
                                    dialog.hide()
                                    startActivity(Intent(baseContext, MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Muammo yuzaga keldi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.hide()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Bu nom band qilingan", Toast.LENGTH_SHORT)
                                .show()
                            dialog.hide()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Ma'lumotlar juda qisqa", Toast.LENGTH_SHORT).show()
                dialog.hide()
            }
        }
    }
}