package uz.eloving.farmy.ui.shop

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.coroutines.*
import uz.eloving.farmy.adapter.AdapterItems
import uz.eloving.farmy.databinding.ActivityItemsBinding
import uz.eloving.farmy.db.ShopDatabase
import uz.eloving.farmy.model.ShopItemModel

class ItemsActivity : AppCompatActivity() {
    private lateinit var adapter: AdapterItems
    private lateinit var binding: ActivityItemsBinding
    private val db by lazy { ShopDatabase }
    private lateinit var databaseReference: DatabaseReference
    private var list = listOf<ShopItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = AdapterItems(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cacheToList()
        binding.refresh.setOnRefreshListener {
            fetchData()
            binding.refresh.isRefreshing = false
        }
    }

    private fun fetchData() {
        databaseReference = FirebaseDatabase.getInstance()
            .getReference(intent.getStringExtra("category").toString())
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                updateCache(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                "Xatolik aniqlandi".asToast()
            }
        }
        databaseReference.addValueEventListener(postListener)
        binding.refresh.isRefreshing = false
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun cacheToList() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                db.getDatabase(this@ItemsActivity).shopDao()
                    .getByCategory(intent.getStringExtra("category").toString())
                    .observe(this@ItemsActivity) {
                        list = it
                        adapter.updateList(it)
                    }
            }
        }
    }

    private suspend fun updateCategory() =
        withContext(Dispatchers.Default) {
            db.getDatabase(this@ItemsActivity).shopDao()
                .updateCategory(intent.getStringExtra("category").toString())
        }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateCache(dataSnapshot: DataSnapshot) {
        GlobalScope.launch {
            updateCategory()
            for (item in dataSnapshot.children) {
                item.getValue(ShopItemModel::class.java)?.let {
                    db.getDatabase(this@ItemsActivity).shopDao().insertItem(it)
                }
            }
        }
        cacheToList()
        adapter.updateList(list)
    }


    private fun String.asToast() {
        Toast.makeText(this@ItemsActivity, this, Toast.LENGTH_SHORT).show()
    }
}