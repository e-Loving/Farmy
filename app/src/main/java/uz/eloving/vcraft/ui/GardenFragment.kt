package uz.eloving.vcraft.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.eloving.vcraft.R
import uz.eloving.vcraft.databinding.FragmentGardenBinding
import uz.eloving.vcraft.databinding.FragmentWelcomeBinding

class GardenFragment : Fragment() {
    private lateinit var binding: FragmentGardenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGardenBinding.inflate(inflater, container, false)
        return binding.root
    }
}