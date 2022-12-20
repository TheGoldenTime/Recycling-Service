package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.adapter.AdapterEventAdmin
import com.example.fyp_mobile.adapter.AdapterRewards
import com.example.fyp_mobile.databinding.ActivityAdminEventBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Event
import com.example.fyp_mobile.model.Participant
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.*

class AdminEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEventBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold rewards data
    private lateinit var eventAdminArrayList: ArrayList<Event>

    //Adapter
    private lateinit var adapterEventAdmin: AdapterEventAdmin

    //Firestore
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.adminEventsRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        eventAdminArrayList = arrayListOf()

        adapterEventAdmin = AdapterEventAdmin(eventAdminArrayList, this)

        recyclerView.adapter = adapterEventAdmin

        EventChangeListener()

        binding.btnAddEvent.setOnClickListener{
            startActivity(Intent(this, AddEventActivity::class.java))
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
        db.collection(Constants.EVENT)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ){
                    if(error != null){
                        Log.e("rewards",error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){

                            eventAdminArrayList.add(dc.document.toObject(Event::class.java))

                        }
                    }

                    adapterEventAdmin.notifyDataSetChanged()
                }


            })
    }
}