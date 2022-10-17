package uz.eloving.vcraft.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.eloving.vcraft.auth.AuthActivity
import uz.eloving.vcraft.model.UIModule
import uz.eloving.vcraft.databinding.FragmentWelcomeBinding

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
        return binding.root
    }
}