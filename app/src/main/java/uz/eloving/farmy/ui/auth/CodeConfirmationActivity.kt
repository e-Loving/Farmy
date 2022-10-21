package uz.eloving.farmy.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import uz.eloving.farmy.MainActivity
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.ActivityCodeConfirmationBinding
import java.util.concurrent.TimeUnit

class CodeConfirmationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var otp: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    private lateinit var tempNumber: String
    private var isExpired = false
    private lateinit var binding: ActivityCodeConfirmationBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        otp = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phoneNumber")!!
        tempNumber = intent.getStringExtra("tempNumber")!!
        binding.phoneNumber.text = "Sizning telefon raqamingiz\n${phoneNumber}"
        startTimer()
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.btnVerify.setOnClickListener {
            if (!isExpired) {
                val typedOTP = binding.etPinView.text!!
                if (typedOTP.isNotEmpty()) {
                    if (typedOTP.length == 6) {
                        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                            otp, typedOTP.toString()
                        )
                        signInWithPhoneAuthCredential(credential)
                    } else {
                        Toast.makeText(this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
                }
            } else {
                resendVerificationCode()
            }
        }
    }

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(tempNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.d("TAG", "onVerificationFailed: $e")
            } else if (e is FirebaseTooManyRequestsException) {
                Log.d("TAG", "onVerificationFailed: $e")
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            startTimer()
            otp = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    PrefManager.saveUID(this, auth.currentUser?.uid.toString())
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToRegister()
                } else {

                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }
                }
            }
    }

    private fun sendToRegister() {
        val intent = Intent(this, SigninActivity::class.java)
        intent.putExtra("phoneNumber", phoneNumber)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        binding.btnVerify.text = "Tasdiqlash"
        isExpired = false
        val timer = object : CountDownTimer(60_000, 1_000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendCode.text =
                    "Kod yaroqlilik muddati ${millisUntilFinished / 1_000} soniya"
            }

            @SuppressLint("SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onFinish() {
                binding.btnVerify.text = "Qayta yuborish"
                binding.tvResendCode.text =
                    "Kod yaroqlilik muddati yakunlandi"
                isExpired = true
            }
        }
        timer.start()
    }
}
