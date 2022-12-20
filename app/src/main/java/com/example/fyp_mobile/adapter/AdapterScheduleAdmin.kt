package com.example.fyp_mobile.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.model.Schedule
import com.example.fyp_mobile.ui.activities.AdminScheduleActivity
import com.example.fyp_mobile.ui.activities.EditScheduleActivity
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class AdapterScheduleAdmin(private val scheduleList : ArrayList<Schedule>, private val context: Context, private val areaID: String) :
    RecyclerView.Adapter<AdapterScheduleAdmin.HolderSchedule>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterScheduleAdmin.HolderSchedule {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_admin_schedule,parent, false)

        return HolderSchedule(itemView)
    }

    override fun onBindViewHolder(holder: AdapterScheduleAdmin.HolderSchedule, position: Int) {
        val schedule : Schedule = scheduleList[position]

        holder.scheduleDay.text = schedule.day

        holder.scheduleTime.text = schedule.time

        holder.deleteBtn.setOnClickListener{
            deleteSchedule(schedule, holder)
        }

        holder.editBtn.setOnClickListener {
            val intent = (Intent(context, EditScheduleActivity::class.java))
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

    private fun deleteSchedule(model: Schedule, holder: HolderSchedule) {
        val id = model.id
        val del = FirebaseFirestore.getInstance()
        del.collection(Constants.AREA)
            .document(areaID)
            .collection(Constants.SCHEDULE)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.e("schedule", "Schedule Deleted!")
                val intent = (Intent(context, AdminScheduleActivity::class.java))
                intent.putExtra("id", areaID)
                context.startActivity(intent)
            }
            .addOnFailureListener{ e->
                Log.e("schedule", "Schedule Fail to Delete!")
            }
    }

    public class HolderSchedule(itemView : View) : RecyclerView.ViewHolder(itemView){

        val scheduleDay : TextView = itemView.findViewById(R.id.text_admin_schedule_day)
        val scheduleTime : TextView = itemView.findViewById(R.id.text_admin_schedule_time)
        val deleteBtn : Button = itemView.findViewById(R.id.schedule_delete_btn)
        val editBtn : Button = itemView.findViewById(R.id.schedule_edit_btn)

    }

}