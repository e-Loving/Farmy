package uz.eloving.farmy.ui.shop

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.coroutines.*
import uz.eloving.farmy.adapter.AdapterItems
import uz.eloving.farmy.databinding.FragmentItemsBinding
import uz.eloving.farmy.db.ShopDatabase
import uz.eloving.farmy.model.ShopItemModel
import uz.eloving.farmy.ui.MainActivity

class ItemsFragment : Fragment() {
    private lateinit var adapter: AdapterItems
    private lateinit var binding: FragmentItemsBinding
    private val db by lazy { ShopDatabase }
    private lateinit var databaseReference: DatabaseReference
    private var list = listOf<ShopItemModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        adapter = AdapterItems(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cacheToList()
        binding.refresh.setOnRefreshListener {
            fetchData()
            binding.refresh.isRefreshing = false
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
            })
        return binding.root
    }

    private fun fetchData() {
        databaseReference = FirebaseDatabase.getInstance()
            .getReference(requireArguments().getString("category").toString())
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
                db.getDatabase(requireContext()).shopDao()
                    .getByCategory(requireArguments().getString("category").toString())
                    .observe(viewLifecycleOwner) {
                        list = it
                        adapter.updateList(it)
                    }
            }
        }
    }

    private suspend fun updateCategory() =
        withContext(Dispatchers.Default) {
            db.getDatabase(requireContext()).shopDao()
                .updateCategory(requireArguments().getString("category").toString())
        }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateCache(dataSnapshot: DataSnapshot) {
        GlobalScope.launch {
            updateCategory()
            for (item in dataSnapshot.children) {
                item.getValue(ShopItemModel::class.java)?.let {
                    db.getDatabase(requireContext()).shopDao().insertItem(it)
                }
            }
        }
        cacheToList()
        adapter.updateList(list)
    }

    private fun String.asToast() = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
}