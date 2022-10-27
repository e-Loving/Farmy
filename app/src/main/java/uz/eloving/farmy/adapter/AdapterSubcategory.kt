package uz.eloving.farmy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.eloving.farmy.R
import uz.eloving.farmy.databinding.ItemSubcategoryBinding
import uz.eloving.farmy.model.ShopItemModel

class AdapterSubcategory(private val ctx: Context) :
    RecyclerView.Adapter<AdapterSubcategory.ViewHolder>() {
    private var dataList = arrayListOf<ShopItemModel>()

    inner class ViewHolder(private val binding: ItemSubcategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ShopItemModel) {
            Glide.with(ctx).load(data.imageDonwloadUrl).placeholder(R.drawable.back)
                .into(binding.pic)
//            binding.name.text = data.name
//            binding.price.text = data.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root =
            ItemSubcategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: AdapterSubcategory.ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(dataList: ArrayList<ShopItemModel>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
}