package com.sds.someflashlight.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sds.someflashlight.R
import com.sds.someflashlight.room.DashboardItem

class DashboardAdapter() : RecyclerView.Adapter<DashboardAdapter.DataViewHolder>() {

    private var dashboardItems: ArrayList<DashboardItem> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.dashboard_rv_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = dashboardItems.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

       val itemTitle =  holder.itemView.findViewById<TextView>(R.id.item_title)
       val itemDescription =  holder.itemView.findViewById<TextView>(R.id.item_description)
       val itemImage =  holder.itemView.findViewById<ImageView>(R.id.item_image)
       val itemErrorCount =  holder.itemView.findViewById<TextView>(R.id.item_error_count)
       val itemRedLine =  holder.itemView.findViewById<View>(R.id.redLine)
       val itemOpenCategory =  holder.itemView.findViewById<ImageView>(R.id.item_open_category)


        itemTitle.text = dashboardItems[position].title
        itemDescription.text = dashboardItems[position].description
        itemImage.setImageResource(dashboardItems[position].iconResId)

        if (dashboardItems[position].alerts != 0){
            itemErrorCount.visibility = View.VISIBLE
            itemRedLine.visibility = View.VISIBLE
            itemErrorCount.text = dashboardItems[position].alerts.toString()
        }else itemErrorCount.visibility = View.GONE


    }

    fun addDashboardItems(dashboardItems: List<DashboardItem>) {
        this.dashboardItems.apply {
            clear()
            addAll(dashboardItems)
            notifyDataSetChanged()
        }
    }

}