package uz.eloving.farmy.ui.jobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uz.eloving.farmy.databinding.FragmentJobsBinding

@Suppress("UNREACHABLE_CODE")
class JobsFragment : Fragment() {
    private lateinit var binding: FragmentJobsBinding
    private lateinit var adapterJob: AdapterJob
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJobsBinding.inflate(inflater, container, false)
        adapterJob = AdapterJob()
        binding.rvJobName.adapter = adapterJob
        return binding.root
        binding.rvJobName.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

}