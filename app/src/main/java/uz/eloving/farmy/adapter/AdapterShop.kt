package uz.eloving.farmy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.eloving.farmy.databinding.ItemCategoryBinding
import uz.eloving.farmy.model.TypeModule

class AdapterShop() : RecyclerView.Adapter<AdapterShop.ViewHolder>() {

    private var list = listOf<TypeModule>()
    var onItemCLick: ((TypeModule) -> Unit)? = null


    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TypeModule) {
            binding.ivCategory.setImageResource(data.image)
            binding.categoryName.text = data.name

        }  init {
            onItemCLick?.invoke(list[adapterPosition])
        }


    }
    fun updateList(list: List<TypeModule>) {
        this.list=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}