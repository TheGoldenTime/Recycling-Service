package com.example.fyp_mobile.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fyp_mobile.R
import com.example.fyp_mobile.model.Area
import com.example.fyp_mobile.ui.activities.ScheduleActivity

class AdapterAreaPage(private val areaList : ArrayList<Area>, private val context: Context) :
    RecyclerView.Adapter<AdapterAreaPage.HolderRecycle>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterAreaPage.HolderRecycle {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_recycle,parent, false)

        return HolderRecycle(itemView)
    }

    override fun onBindViewHolder(holder: AdapterAreaPage.HolderRecycle, position: Int) {
        val area : Area = areaList[position]

        Glide.with(context).load(area.image).into(holder.areaImage)

        holder.txtName.text = area.name

        holder.areaImage.setOnClickListener {
            val intent = (Intent(context, ScheduleActivity::class.java))
            intent.putExtra("id", area.id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {

        return areaList.size

    }

    public class HolderRecycle(itemView : View) : RecyclerView.ViewHolder(itemView){

        val areaImage: ImageView = itemView.findViewById(R.id.area_image_view)
        val txtName: TextView = itemView.findViewById(R.id.area_text)

    }
}