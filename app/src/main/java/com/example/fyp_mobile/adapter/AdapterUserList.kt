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
import com.example.fyp_mobile.model.Area
import com.example.fyp_mobile.model.Schedule
import com.example.fyp_mobile.model.UserList
import com.example.fyp_mobile.ui.activities.CalculationActivity
import com.example.fyp_mobile.ui.activities.EditEventActivity
import com.example.fyp_mobile.ui.activities.EditScheduleActivity

class AdapterUserList(private val userlistList : ArrayList<UserList>, private val context: Context, private val areaID: String, private val scheduleID: String) :
    RecyclerView.Adapter<AdapterUserList.HolderUserList>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterUserList.HolderUserList {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_userlist,parent, false)

        return HolderUserList(itemView)
    }

    override fun onBindViewHolder(holder: AdapterUserList.HolderUserList, position: Int) {
        val userlist : UserList = userlistList[position]

        holder.username.text = userlist.name

        holder.email.text = userlist.email

        holder.address.text = userlist.address

        holder.calculate.setOnClickListener {
            //To Calculate Module
            val intent = (Intent(context, CalculationActivity::class.java))
            val extras = Bundle()
            extras.putString("areaID", areaID)
            extras.putString("scheduleID", scheduleID)
            extras.putString("userlistID", userlist.id)
            intent.putExtras(extras)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {

        return userlistList.size

    }

    public class HolderUserList(itemView : View) : RecyclerView.ViewHolder(itemView){

        val username : TextView = itemView.findViewById(R.id.userlist_name_text)
        val email : TextView = itemView.findViewById(R.id.userlist_email_text)
        val address : TextView = itemView.findViewById(R.id.userlist_address_text)
        val calculate : TextView = itemView.findViewById(R.id.userlist_calculate_text)

    }
}