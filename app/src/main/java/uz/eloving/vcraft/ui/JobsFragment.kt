package uz.eloving.vcraft.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.eloving.vcraft.R
import uz.eloving.vcraft.databinding.FragmentJobsBinding

class JobsFragment : Fragment() {
    private lateinit var binding: FragmentJobsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

}