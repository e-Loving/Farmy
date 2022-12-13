package uz.eloving.farmy.ui.welcome.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.eloving.farmy.R
import uz.eloving.farmy.ui.MainActivity
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.FragmentSigninBinding
import uz.eloving.farmy.model.UserModel
import uz.eloving.farmy.util.ProgressDialog
import uz.eloving.farmy.util.Utils.Companion.asToast
import uz.eloving.farmy.util.Utils.Companion.onBackPressedListener
import uz.eloving.farmy.util.hide
import uz.eloving.farmy.util.show

class SigninFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val dialog = ProgressDialog()
        binding.ivBackSignIn.setOnClickListener {
            onBackPressedListener(requireActivity(), R.id.welcome_container, AuthFragment(), true)
        }
        binding.signlogin.setOnClickListener {
            if (binding.usernamelogin.text?.length!! > 3 && binding.passwordlogin.text?.length!! >= 6) {
                if (!requireArguments().getBoolean("reg")) {
                    dialog.show(parentFragmentManager)
                    auth.signInWithEmailAndPassword(
                        "${binding.usernamelogin.text}@gmail.com",
                        binding.passwordlogin.text.toString()
                    ).addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            PrefManager.saveUsername(
                                requireContext(),
                                binding.usernamelogin.text.toString()
                            )
                            startMainActivity()
                        } else {
                            "Bunday xisob mavjud emas".asToast(requireActivity())
                            dialog.hide()
                        }
                    }
                } else {
                    dialog.show(parentFragmentManager)
                    auth.createUserWithEmailAndPassword(
                        "${binding.usernamelogin.text}@gmail.com",
                        binding.passwordlogin.text.toString()
                    ).addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            databaseReference =
                                FirebaseDatabase.getInstance().getReference("users")
                            databaseReference.child(
                                binding.usernamelogin.text.toString()
                            ).setValue(
                                UserModel(
                                    binding.usernamelogin.text.toString(),
                                    binding.passwordlogin.text.toString(),
                                    arguments?.getString("phoneNumber")!!
                                )
                            ).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    "Muvaffaqiyatli yakunlandi".asToast(requireActivity())
                                    PrefManager.saveUsername(
                                        requireContext(),
                                        binding.usernamelogin.text.toString()
                                    )
                                    dialog.hide()
                                    startMainActivity()
                                } else {
                                    "Muammo yuzaga keldi".asToast(requireActivity())
                                    dialog.hide()
                                }
                            }
                        } else {
                            "Bu nom band qilingan".asToast(requireActivity())
                            dialog.hide()
                        }
                    }
                }
            } else {
                "Ma'lumotlar juda qisqa".asToast(requireActivity())
            }
        }
        return binding.root
    }

    private fun startMainActivity() =
        startActivity(Intent(requireActivity(), MainActivity::class.java))
}