package uz.eloving.farmy.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.eloving.farmy.databinding.ItemItemBinding
import uz.eloving.farmy.model.ShopItemModel

class AdapterItems:
    RecyclerView.Adapter<AdapterItems.ViewHolder>() {
    private var list = arrayListOf<ShopItemModel>()
    var onItemCLick: ((ShopItemModel) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ShopItemModel) {

        }

        init {
            onItemCLick?.invoke(list[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }


    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(data: ArrayList<ShopItemModel>) {
        this.list = data
        notifyDataSetChanged()
    }
}