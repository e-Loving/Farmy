package uz.eloving.vcraft.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import uz.eloving.vcraft.R
import uz.eloving.vcraft.auth.AuthActivity
import uz.eloving.vcraft.databinding.ActivityWelcomeBinding

class WelcomeActivity : FragmentActivity() {
    private lateinit var adapter: WelcomeFragmentAdapter
    private lateinit var binding: ActivityWelcomeBinding
    private var currentIndex = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = WelcomeFragmentAdapter(this)
        binding.viewPager2.adapter = adapter
        binding.btnNext.setOnClickListener {
            binding.viewPager2.currentItem++
            if (currentIndex) {
                startActivity(Intent(this, AuthActivity::class.java))
            } else if (binding.viewPager2.currentItem == 2) {
                binding.btnNext.text = "Ro'yxatgdan o'tish"
                currentIndex = true
            }

        }
    }
}