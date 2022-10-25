package uz.eloving.farmy.ui.shop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.FragmentShopBinding


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        binding.tvName.text = PrefManager.getUsername(requireContext())
        binding.fabAdd.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    AddActivity::class.java
                )
            )
        }
        return binding.root
    }


}