package com.example.fyp_mobile.ui.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.adapter.AdapterRewardsPage
import com.example.fyp_mobile.databinding.ActivityRewardsBinding
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.*

class RewardsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRewardsBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold rewards data
    private lateinit var rewardArrayList: ArrayList<Rewards>

    //Adapter
    private lateinit var adapterRewardsPage: AdapterRewardsPage

    //Firestore
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rewardsPageRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        rewardArrayList = arrayListOf()

        adapterRewardsPage = AdapterRewardsPage(rewardArrayList, this)

        recyclerView.adapter = adapterRewardsPage

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
        db.collection(Constants.REWARD)
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

                            rewardArrayList.add(dc.document.toObject(Rewards::class.java))

                        }
                    }

                    adapterRewardsPage.notifyDataSetChanged()
                }


            })
    }
}