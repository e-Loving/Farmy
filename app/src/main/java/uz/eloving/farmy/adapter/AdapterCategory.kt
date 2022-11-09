package uz.eloving.farmy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.eloving.farmy.data.MockData
import uz.eloving.farmy.databinding.ItemCategoryBinding
import uz.eloving.farmy.model.CategoryModel

class AdapterCategory :
    RecyclerView.Adapter<AdapterCategory.ViewHolder>() {
    private var list = MockData.categoryData
    var onItemCLick: ((CategoryModel) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategoryModel) {
            binding.ivCategory.setImageResource(data.img)
            binding.categoryName.text = data.title
        }

        init {
            itemView.setOnClickListener {
                onItemCLick?.invoke(list[adapterPosition])
            }
        }
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