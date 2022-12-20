package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.adapter.AdapterSchedule
import com.example.fyp_mobile.adapter.AdapterScheduleAdmin
import com.example.fyp_mobile.databinding.ActivityAdminRecycleBinding
import com.example.fyp_mobile.databinding.ActivityScheduleBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Participant
import com.example.fyp_mobile.model.Schedule
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.model.UserList
import com.example.fyp_mobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.*

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold schedule data
    private lateinit var scheduleArrayList: ArrayList<Schedule>

    //Adapter
    private lateinit var adapterSchedule: AdapterSchedule

    //Firestore
    private lateinit var db : FirebaseFirestore

    //area ID
    private var id = ""

    //private lateinit var mUserDetails: User

    //private lateinit var mUserList: UserList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!

        //FirestoreClass().getUserDetails(this)

        //check
        recyclerView = binding.scheduleRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        scheduleArrayList = arrayListOf()

        adapterSchedule = AdapterSchedule(scheduleArrayList, this, id)

        recyclerView.adapter = adapterSchedule

        EventChangeListener()

        binding.backBtnSchedule.setOnClickListener{
            startActivity(Intent(this, RecycleActivity::class.java))
            finish()
        }

        val nav = findViewById<BottomNavigationView>(R.id.nav)
        nav.setOnNavigationItemSelectedListener {
            when(it.itemId){

                R.id.navigation_rewards -> {
                    val intent = Intent(this, RewardsActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_recycle -> {
                    val intent = Intent(this, RecycleActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_cart -> {
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_event -> {
                    val intent = Intent(this, EventActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_profile -> {
                    val intent = Intent(this, UserProfActivity::class.java)
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

//    fun userDetailsSuccess(user: User){
//
//        mUserDetails = user
//
//        val id = System.currentTimeMillis().toString()
//
//        val userlist = UserList(
//            id,
//            user.username,
//            user.email,
//            address,
//        )
//
//    }
}