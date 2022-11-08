package uz.eloving.farmy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import uz.eloving.farmy.R

private class BaseGardenAdapter(context: Context) : BaseAdapter() {
    private var sList = arrayOf("Sunday", "Monday")
    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return sList.size
    }

    override fun getItem(position: Int): Any {
        return sList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = this.mInflator.inflate(R.layout.base_list_raw, parent, false)
        return view
    }
}