package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.adapter.AdapterEvent
import com.example.fyp_mobile.adapter.AdapterRewardsPage
import com.example.fyp_mobile.databinding.ActivityCartBinding
import com.example.fyp_mobile.databinding.ActivityEventBinding
import com.example.fyp_mobile.model.Event
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.*

class EventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold rewards data
    private lateinit var eventArrayList: ArrayList<Event>

    //Adapter
    private lateinit var adapterEvent: AdapterEvent

    //Firestore
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.eventPageRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        eventArrayList = arrayListOf()

        adapterEvent = AdapterEvent(eventArrayList, this)

        recyclerView.adapter = adapterEvent

        EventChangeListener()

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

//                R.id.navigation_donation -> {
//                    val intent = Intent(this, DonationActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }

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

                            eventArrayList.add(dc.document.toObject(Event::class.java))

                        }
                    }

                    adapterEvent.notifyDataSetChanged()
                }


            })
    }

}