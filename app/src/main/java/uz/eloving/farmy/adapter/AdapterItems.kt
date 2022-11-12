package uz.eloving.farmy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.eloving.farmy.databinding.ItemItemBinding
import uz.eloving.farmy.model.ShopItemModel

class AdapterItems(private val ctx: Context) :
    RecyclerView.Adapter<AdapterItems.ViewHolder>() {
    private var list = listOf<ShopItemModel>()
    var onItemCLick: ((ShopItemModel) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: ShopItemModel) {
            Glide.with(ctx).load(data.imageDownloadUrl).into(binding.pic)
            binding.name.text = data.name
            binding.price.text = "${data.price.toString()} so'm ${data.per}i"
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
    fun updateList(data: List<ShopItemModel>) {
        this.list = data
        notifyDataSetChanged()
    }
}