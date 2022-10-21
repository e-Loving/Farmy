package uz.eloving.farmy.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import uz.eloving.farmy.databinding.ActivityAuthBinding
import uz.eloving.farmy.ui.auth.dialog.ProgressDialog
import uz.eloving.farmy.ui.auth.dialog.show
import java.util.concurrent.TimeUnit

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String
    private var tempNumber = "+"
    lateinit var dialogProgress: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.sendOTPBtn.setOnClickListener {
            sendCode()
            dialogProgress = ProgressDialog()
            dialogProgress.show(supportFragmentManager)


        }

        binding.registeredAccount.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            intent.putExtra("reg", true)
            startActivity(intent)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Xush kelibsiz !", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }

                }

            }
    }

    private fun sendToMain() {
        startActivity(Intent(this, CodeConfirmationActivity::class.java))
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {

            } else if (e is FirebaseTooManyRequestsException) {

            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            val intent = Intent(this@AuthActivity, CodeConfirmationActivity::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("phoneNumber", number)
            intent.putExtra("tempNumber", tempNumber)
            startActivity(intent)
        }
    }

    private fun sendCode() {
        number = binding.etPhoneNumber.text?.trim().toString()
        if (number.isNotEmpty()) {
            if (number.length == 17) {
                number.forEach { if (it in '0'..'9') tempNumber += it.toString() }
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(tempNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)

            } else {
                Toast.makeText(this, "Please Enter correct Number", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please Enter Number", Toast.LENGTH_SHORT).show()

        }
    }

}



