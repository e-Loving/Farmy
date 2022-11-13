package uz.eloving.farmy.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.FragmentViewPagerBinding
import uz.eloving.farmy.model.UIModel
import uz.eloving.farmy.ui.welcome.auth.AuthFragment

class ViewPagerFragment(private val all: UIModel) : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        binding.lottieAnimationView.setAnimation(all.lottie_animation)
        binding.tvDescription.text = all.description
        binding.tvTitle.text = all.title
        binding.btnNext.visibility = if (all.nextButtonVisibility) View.VISIBLE else View.GONE
        binding.btnNext.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.welcome_container, AuthFragment())?.commit()
        }
        return binding.root
    }
}