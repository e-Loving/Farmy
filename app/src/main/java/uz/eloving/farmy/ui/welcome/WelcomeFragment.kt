package uz.eloving.farmy.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.eloving.farmy.adapter.WelcomeFragmentAdapter
import uz.eloving.farmy.databinding.FragmentWelcomeBinding
import uz.eloving.farmy.util.Utils.Companion.onBackPressedListener

class WelcomeFragment : Fragment() {
    private lateinit var adapter: WelcomeFragmentAdapter
    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        onBackPressedListener(requireActivity())
        adapter = WelcomeFragmentAdapter(requireActivity())
        binding.viewPager2.adapter = adapter
        binding.dots.attachTo(binding.viewPager2)
        return binding.root
    }
}