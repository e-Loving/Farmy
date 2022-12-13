package uz.eloving.farmy.ui.welcome.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.FragmentAuthBinding
import uz.eloving.farmy.ui.welcome.WelcomeFragment
import uz.eloving.farmy.util.ProgressDialog
import uz.eloving.farmy.util.Utils.Companion.asToast
import uz.eloving.farmy.util.Utils.Companion.onBackPressedListener
import uz.eloving.farmy.util.hide
import uz.eloving.farmy.util.show
import java.util.concurrent.TimeUnit

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String
    private var tempNumber = "+"
    private lateinit var dialogProgress: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
        dialogProgress = ProgressDialog()
        onBackPressedListener(requireActivity(), R.id.welcome_container, WelcomeFragment())
        binding.ivBack.setOnClickListener {
            onBackPressedListener(
                requireActivity(),
                R.id.welcome_container,
                WelcomeFragment(),
                true
            )
        }
        binding.sendOTPBtn.setOnClickListener {
            sendCode()
        }

        binding.registeredAccount.setOnClickListener {
            toReg(false)
        }
        return binding.root
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                "Xush kelibsiz !".asToast(requireActivity())
                toReg(true)
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    "Xatolik !".asToast(requireActivity())
                }
            }
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                "Raqam noto'g'ri".asToast(requireActivity())
            } else if (e is FirebaseTooManyRequestsException) {
                "Iltimos birozdan so'ng harakat qiling".asToast(requireActivity())
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            val fragment = CodeConfirmationFragment()
            fragment.arguments = bundleOf(
                Pair("OTP", verificationId),
                Pair("resendToken", token),
                Pair("phoneNumber", number),
                Pair("tempNumber", tempNumber)
            )
            dialogProgress.hide()
            onBackPressedListener(requireActivity(), R.id.welcome_container, fragment, true)
        }
    }

    private fun sendCode() {
        number = binding.etPhoneNumber.text?.trim().toString()
        if (number.isNotEmpty()) {
            if (number.length == 17) {
                number.forEach { if (it in '0'..'9') tempNumber += it.toString() }
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(tempNumber)
                    .setTimeout(120L, TimeUnit.SECONDS)
                    .setActivity(requireActivity())
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
                dialogProgress.show(parentFragmentManager)
            } else "Raqam noto'g'ri".asToast(requireActivity())
        } else "Raqamingizni kiriting !".asToast(requireActivity())
    }

    private fun toReg(reg: Boolean) {
        val fragment = SigninFragment()
        fragment.arguments = bundleOf(Pair("reg", reg))
        onBackPressedListener(requireActivity(), R.id.welcome_container, fragment, true)
    }
}