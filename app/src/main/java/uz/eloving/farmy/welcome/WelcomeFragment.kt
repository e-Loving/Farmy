package uz.eloving.farmy.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.eloving.farmy.auth.AuthActivity
import uz.eloving.farmy.model.UIModule
import uz.eloving.farmy.databinding.FragmentWelcomeBinding

class WelcomeFragment(private val all: UIModule) : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding.lottieAnimationView.setAnimation(all.lottie_animation)
        binding.tvDescription.text = all.description
        binding.tvTitle.text = all.title
        binding.btnNext.visibility = if (all.nextButtonVisibility) View.VISIBLE else View.GONE
        binding.btnNext.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    AuthActivity::class.java
                )
            )
        }
        return binding.root
    }
}