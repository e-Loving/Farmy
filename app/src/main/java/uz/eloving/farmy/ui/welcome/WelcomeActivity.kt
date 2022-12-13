package uz.eloving.farmy.ui.welcome

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.ActivityWelcomeBinding

class WelcomeActivity : FragmentActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // If either one fragment of ()
        supportFragmentManager.beginTransaction()
            .replace(R.id.welcome_container, WelcomeFragment()).commit()
    }
}