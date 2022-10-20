package uz.eloving.farmy.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import uz.eloving.farmy.MainActivity
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.ActivityWelcomeBinding

class WelcomeActivity : FragmentActivity() {
    private lateinit var adapter: WelcomeFragmentAdapter
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (PrefManager.getUID(this) != "uid") {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("uid", PrefManager.getUID(this))
            startActivity(intent)
        }
        adapter = WelcomeFragmentAdapter(this)
        binding.viewPager2.adapter = adapter
        binding.dots.attachTo(binding.viewPager2)
    }
}