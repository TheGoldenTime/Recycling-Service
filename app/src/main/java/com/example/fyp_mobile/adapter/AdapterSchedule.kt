package com.example.fyp_mobile.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Schedule
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.ui.activities.EditScheduleActivity
import com.example.fyp_mobile.ui.activities.RegisterScheduleActivity
import com.example.fyp_mobile.utils.Constants
import java.security.AccessController.getContext

class AdapterSchedule(private val scheduleList : ArrayList<Schedule>, private val context: Context, private val areaID: String) :
    RecyclerView.Adapter<AdapterSchedule.HolderSchedule>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSchedule.HolderSchedule {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_schedule,parent, false)

        return HolderSchedule(itemView)
    }

    override fun onBindViewHolder(holder: AdapterSchedule.HolderSchedule, position: Int) {
        val schedule : Schedule = scheduleList[position]

        holder.scheduleDay.text = schedule.day

        holder.scheduleTime.text = schedule.time

        holder.scheduleRegister.setOnClickListener {
            //Register Schedule
                val intent = (Intent(context, RegisterScheduleActivity::class.java))
                val extras = Bundle()
                extras.putString("areaID", areaID)
                extras.putString("scheduleID", schedule.id)
                intent.putExtras(extras)
                context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {

        return scheduleList.size

    }


    public class HolderSchedule(itemView : View) : RecyclerView.ViewHolder(itemView){

        val scheduleDay : TextView = itemView.findViewById(R.id.schedule_day_text)
        val scheduleTime : TextView = itemView.findViewById(R.id.schedule_time_text)
        val scheduleRegister : TextView = itemView.findViewById(R.id.schedule_register_text)

    }
}