package com.example.fyp_mobile.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fyp_mobile.R
import com.example.fyp_mobile.model.Event
import com.example.fyp_mobile.ui.activities.EventDetailActivity

class AdapterEvent(private val eventList : ArrayList<Event>, private val context: Context) : RecyclerView.Adapter<AdapterEvent.HolderEvent>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterEvent.HolderEvent {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_design,parent, false)

        return HolderEvent(itemView)
    }

    override fun onBindViewHolder(holder: AdapterEvent.HolderEvent, position: Int) {
        val event : Event = eventList[position]

        Glide.with(context).load(event.image).into(holder.eventImage)

        holder.eventName.text = event.name

        holder.eventPoint.text = event.points.toString() + " points"

        holder.eventImage.setOnClickListener {
            val intent = (Intent(context, EventDetailActivity::class.java))
            intent.putExtra("id", event.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return eventList.size

    }


    public class HolderEvent(itemView : View) : RecyclerView.ViewHolder(itemView){

        val eventImage : ImageView = itemView.findViewById(R.id.card_reward_imageview)
        val eventName : TextView = itemView.findViewById(R.id.reward_text_name)
        val eventPoint : TextView = itemView.findViewById(R.id.reward_text_point)

    }

}