package uz.eloving.farmy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import uz.eloving.farmy.R
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.ActivityMainBinding
import uz.eloving.farmy.ui.welcome.WelcomeActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (PrefManager.getUsername(this) != "") {
            setContentView(binding.root)
            setupSmoothBottomMenu()
            binding.tvName.text = PrefManager.getUsername(this)
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        } else startActivity(Intent(this, WelcomeActivity::class.java))

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString().toInt()
        binding.tvGreeting.text = when (currentHour) {
            in 3..11 -> "Xayrli tong"
            in 12..18 -> "Xayrli kun"
            else -> "Xayrli kech"
        }
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.main_menu)
        val menu = popupMenu.menu
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(menu, navController)
    }
}