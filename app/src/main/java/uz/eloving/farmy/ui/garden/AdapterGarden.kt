package uz.eloving.farmy.ui.garden

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import uz.eloving.farmy.databinding.ItemGardenBinding

import uz.eloving.farmy.db.GardenDatabase
import uz.eloving.farmy.model.GardenItemModel

class AdapterGarden() : RecyclerView.Adapter<AdapterGarden.ViewHolder>() {

    private var list = listOf<GardenItemModel>()


    var onItemCLick: ((GardenItemModel) -> Unit)? = null


    inner class ViewHolder(private val binding: ItemGardenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GardenItemModel) {
            binding.pic.setImageBitmap(data.pic)
            binding.confidence.text = data.confidence.toString()
            binding.disease.text = data.disease
        }

        init {
            onItemCLick?.invoke(list[adapterPosition])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<GardenItemModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = ItemGardenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}