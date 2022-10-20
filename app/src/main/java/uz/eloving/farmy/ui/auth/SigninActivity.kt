package uz.eloving.farmy.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import uz.eloving.farmy.MainActivity
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.sign.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.username.text.toString() + "@gmail.com",
                binding.password.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}