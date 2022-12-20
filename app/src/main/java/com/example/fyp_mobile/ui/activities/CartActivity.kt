package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.R
import com.example.fyp_mobile.adapter.AdapterCart
import com.example.fyp_mobile.databinding.ActivityCartBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Cart
import com.example.fyp_mobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.*

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold cart data
    private lateinit var cartArrayList: ArrayList<Cart>

    //Adapter
    private lateinit var adapterCart: AdapterCart

    //Firestore
    private lateinit var db : FirebaseFirestore

    private lateinit var currentUserID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.CartRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        cartArrayList = arrayListOf()

        adapterCart = AdapterCart(cartArrayList, this)

        recyclerView.adapter = adapterCart

        currentUserID = FirestoreClass().getCurrentUserID()

        EventChangeListener()

        val nav = findViewById<BottomNavigationView>(R.id.navCart)
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
        db.collection(Constants.USERS)
            .document(currentUserID)
            .collection(Constants.CART)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ){
                    if(error != null){
                        Log.e("cart",error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){

                            cartArrayList.add(dc.document.toObject(Cart::class.java))

                        }
                    }

                    adapterCart.notifyDataSetChanged()
                }


            })
    }
}