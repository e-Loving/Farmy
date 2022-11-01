package uz.eloving.farmy.ui.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.ActivityItemShopBinding

class ItemShopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityItemShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}