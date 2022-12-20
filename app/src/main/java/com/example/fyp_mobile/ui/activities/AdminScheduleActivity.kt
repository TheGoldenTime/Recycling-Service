package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.adapter.AdapterRewards
import com.example.fyp_mobile.adapter.AdapterScheduleAdmin
import com.example.fyp_mobile.databinding.ActivityAdminScheduleBinding
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.model.Schedule
import com.example.fyp_mobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.*

class AdminScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminScheduleBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold schedule data
    private lateinit var scheduleArrayList: ArrayList<Schedule>

    //Adapter
    private lateinit var adapterSchedule: AdapterScheduleAdmin

    //Firestore
    private lateinit var db : FirebaseFirestore

    //area ID
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!

        recyclerView = binding.adminScheduleRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        scheduleArrayList = arrayListOf()

        adapterSchedule = AdapterScheduleAdmin(scheduleArrayList, this, id)

        recyclerView.adapter = adapterSchedule

        EventChangeListener()

        binding.backBtnEditSchedule.setOnClickListener{
            startActivity(Intent(this, AdminRecycleActivity::class.java))
            finish()
        }

        binding.btnAddSchedule.setOnClickListener {

            val intent = Intent(this@AdminScheduleActivity, AddScheduleActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        val nav = findViewById<BottomNavigationView>(R.id.adminNav)
        nav.setOnNavigationItemSelectedListener {
            when(it.itemId){

                R.id.navigation_admin_rewards -> {
                    val intent = Intent(this, AdminRewardsActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_admin_recycle -> {
                    val intent = Intent(this, AdminRecycleActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_admin_report -> {
                    val intent = Intent(this, AdminReportActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_admin_event -> {
                    val intent = Intent(this, AdminEventActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_admin_profile -> {
                    val intent = Intent(this, AdminProfActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection(Constants.AREA)
            .document(id)
            .collection(Constants.SCHEDULE)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ){
                    if(error != null){
                        Log.e("schedule",error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){

                            scheduleArrayList.add(dc.document.toObject(Schedule::class.java))

                        }
                    }

                    adapterSchedule.notifyDataSetChanged()
                }


            })
    }
}