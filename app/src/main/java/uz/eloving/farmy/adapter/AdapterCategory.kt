package uz.eloving.farmy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.eloving.farmy.data.MockData
import uz.eloving.farmy.databinding.ItemCategoryBinding
import uz.eloving.farmy.model.ShopItemModel

class AdapterCategory(private val ctx: Context) :
    RecyclerView.Adapter<AdapterCategory.ViewHolder>() {
    private var list = hashMapOf<String, ArrayList<ShopItemModel>>()

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String, data: ArrayList<ShopItemModel>) {


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[MockData.categoryData[position]]?.let {
            holder.bind(MockData.categoryData[position], it)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(data: HashMap<String, ArrayList<ShopItemModel>>) {
        this.list = data
        notifyDataSetChanged()
    }
}