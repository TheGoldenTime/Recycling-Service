package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.adapter.AdapterArea
import com.example.fyp_mobile.adapter.AdapterAreaPage
import com.example.fyp_mobile.databinding.ActivityRecycleBinding
import com.example.fyp_mobile.model.Area
import com.example.fyp_mobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.*

class RecycleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecycleBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold area data
    private lateinit var areaArrayList: ArrayList<Area>

    //Adapter
    private lateinit var adapterAreaPage: AdapterAreaPage

    //Firestore
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecycleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclePageRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        areaArrayList = arrayListOf()

        adapterAreaPage = AdapterAreaPage(areaArrayList, this)

        recyclerView.adapter = adapterAreaPage

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
        db.collection(Constants.AREA)
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

                            areaArrayList.add(dc.document.toObject(Area::class.java))

                        }
                    }

                    adapterAreaPage.notifyDataSetChanged()
                }


            })
    }
}