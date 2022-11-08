package uz.eloving.farmy.ui.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import uz.eloving.farmy.R
import uz.eloving.farmy.adapter.AdapterItems
import uz.eloving.farmy.databinding.ActivityItemsBinding

class ItemsActivity : AppCompatActivity() {
    private lateinit var adapter: AdapterItems
    private lateinit var binding: ActivityItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = AdapterItems()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }
}