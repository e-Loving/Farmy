package uz.eloving.farmy.ui.welcome.auth

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import uz.eloving.farmy.R
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.FragmentCodeConfirmationBinding
import uz.eloving.farmy.util.Utils.Companion.asToast
import uz.eloving.farmy.util.Utils.Companion.onBackPressedListener
import java.util.concurrent.TimeUnit

class CodeConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentCodeConfirmationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var otp: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    private lateinit var tempNumber: String
    private var isExpired = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeConfirmationBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        otp = arguments?.getString("OTP").toString()
        @Suppress("DEPRECATION")
        resendToken = arguments?.getParcelable("resendToken")!!
        phoneNumber = arguments?.getString("phoneNumber")!!
        tempNumber = arguments?.getString("tempNumber")!!
        binding.phoneNumber.text = "Sizning telefon raqamingiz\n${phoneNumber}"
        startTimer()
        onBackPressedListener(requireActivity(), R.id.welcome_container, AuthFragment())
        binding.ivBack.setOnClickListener {
            onBackPressedListener(requireActivity(), R.id.welcome_container, AuthFragment(), true)
        }
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
                        "Kod noto'g'ri !".asToast(requireActivity())
                    }
                } else {
                    "Iltimos kod kiriting !".asToast(requireActivity())
                }
            } else {
                binding.etPinView.setText("")
                resendVerificationCode()
            }
        }
        return binding.root
    }

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(tempNumber)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
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
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    PrefManager.saveUID(requireContext(), auth.currentUser?.uid.toString())
                    "Authenticate Successfully".asToast(requireActivity())
                    sendToRegister()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        "Xatolik, qaytadan urining !".asToast(requireActivity())
                    }
                }
            }
    }

    private fun sendToRegister() {
        val fragment = SigninFragment()
        fragment.arguments = bundleOf(Pair("reg", true), Pair("phoneNumber", phoneNumber))
        onBackPressedListener(requireActivity(), R.id.welcome_container, fragment, true)
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        binding.btnVerify.text = "Tasdiqlash"
        isExpired = false
        val timer = object : CountDownTimer(120_000, 1_000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendCode.text =
                    "Kod yaroqlilik muddati ${timeConversion(millisUntilFinished / 1_000)}"
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

    private fun timeConversion(until: Long): String {
        val minutesInAnHour = 60
        val secondsInAMinute = 60
        val seconds = until % secondsInAMinute
        val totalMinutes = until / secondsInAMinute
        val minutes = totalMinutes % minutesInAnHour
        return "$minutes : $seconds"
    }

}