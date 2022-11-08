package uz.eloving.farmy.ui.jobs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.eloving.farmy.databinding.ItemJobBinding
import uz.eloving.farmy.model.JobModel

class AdapterJob() : RecyclerView.Adapter<AdapterJob.ViewHolder>() {


    private var list = listOf<JobModel>()

    inner class ViewHolder(private val binding: ItemJobBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: JobModel) {
            binding.tvJobs.text = data.jobType.toString()
        }
    }

    fun updateList(list: List<JobModel>) {
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