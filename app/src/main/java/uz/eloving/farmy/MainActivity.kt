package uz.eloving.farmy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.ActivityMainBinding
import uz.eloving.farmy.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PrefManager.getUsername(this) != "") {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupSmoothBottomMenu()
        } else startActivity(Intent(this, WelcomeActivity::class.java))

    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.main_menu)
        val menu = popupMenu.menu
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(menu, navController)
    }
}