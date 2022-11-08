package uz.eloving.farmy.ui.shop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import uz.eloving.farmy.adapter.AdapterCategory
import uz.eloving.farmy.data.MockData
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.FragmentShopBinding
import uz.eloving.farmy.model.ShopItemModel


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: AdapterCategory

    //    private lateinit var databaseReference: DatabaseReference
    private var allData = hashMapOf<String, ArrayList<ShopItemModel>>()
    val data = arrayListOf<ArrayList<ShopItemModel>>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        binding.tvName.text = PrefManager.getUsername(requireContext())
        adapter = AdapterCategory()
        binding.rvCategory.adapter = adapter
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddActivity::class.java))
        }
//        binding.swipe.setOnRefreshListener {
//            fetchData()
//        }
        return binding.root

    }

//    private fun fetchData() {
//        for (type in MockData.categoryData) {
//            databaseReference = FirebaseDatabase.getInstance().getReference("sell/$type")
//            val postListener = object : ValueEventListener {
//
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for (item in dataSnapshot.children) {
//                        item.getValue(ShopItemModel::class.java)?.let {
//
//                        }
//                    }
//
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Toast.makeText(requireContext(), "Iltimos qayta urining !", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//            databaseReference.addValueEventListener(postListener)
//        }
//
////        binding.swipe.isRefreshing = false
//    }


}