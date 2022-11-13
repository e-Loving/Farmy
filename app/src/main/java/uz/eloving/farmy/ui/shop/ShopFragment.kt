package uz.eloving.farmy.ui.shop

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import me.ibrahimsn.lib.SmoothBottomBar
import uz.eloving.farmy.R
import uz.eloving.farmy.adapter.AdapterCategory
import uz.eloving.farmy.data.MockData
import uz.eloving.farmy.data.PrefManager
import uz.eloving.farmy.databinding.FragmentShopBinding
import uz.eloving.farmy.model.CategoryModel
import uz.eloving.farmy.model.ShopItemModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: AdapterCategory

    val data = arrayListOf<ArrayList<ShopItemModel>>()

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        adapter = AdapterCategory()
        binding.rvCategory.adapter = adapter
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                updateBySearch(s.toString().lowercase())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateBySearch(s.toString().lowercase())
            }

            override fun afterTextChanged(s: Editable?) {
                updateBySearch(s.toString().lowercase())
            }
        })
        adapter.onItemCLick = {
            val fragment = ItemsFragment()
            fragment.arguments = bundleOf(Pair("category", it.title))
            onProcess(fragment)
        }
        binding.fabAdd.setOnClickListener {
            onProcess(AddFragment())
        }
        return binding.root
    }

    fun updateBySearch(name: String) {
        adapter.updateList(MockData.categoryData.filter { name in it.title.lowercase() } as ArrayList<CategoryModel>)
    }

    private fun onProcess(fragment: Fragment) {
        view?.visibility = View.INVISIBLE
        view?.isEnabled = false
        activity?.findViewById<SmoothBottomBar>(R.id.nav_view)?.visibility = View.GONE
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.nav_host_fragment_activity_main, fragment)?.commit()
    }
}