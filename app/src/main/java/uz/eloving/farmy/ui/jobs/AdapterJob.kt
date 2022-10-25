package uz.eloving.farmy.ui.jobs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.eloving.farmy.databinding.ItemJobBinding
import uz.eloving.farmy.model.JobModule

class AdapterJob() : RecyclerView.Adapter<AdapterJob.ViewHolder>() {


    private var list = listOf<JobModule>()

    inner class ViewHolder(private val binding: ItemJobBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: JobModule) {
            binding.tvJobs.text = data.jobType.toString()
        }
    }

    fun updateList(list: List<JobModule>) {
        this.list = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(root)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}