package com.example.fyp_mobile.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityRankingBinding
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_user_prof.*
import java.util.*
import kotlin.collections.ArrayList

class RankingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingBinding

    //array to store top 3
    private lateinit var rankingArrayList: ArrayList<User>

    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        rankingArrayList = arrayListOf()

        EventChangeListener()

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    //remember this special
    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection(Constants.USERS)
            .orderBy("earn", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ){
                    if(error != null){
                        Log.e("ranking",error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){

                            rankingArrayList.add(dc.document.toObject(User::class.java))

                        }
                    }
                    displayRanking()
                }


            })
    }

    private fun displayRanking(){

        binding.tvFirst.text = "1) " + rankingArrayList[0].username
        binding.tvFirstPoint.text = rankingArrayList[0].earn.toString() + " Points Earned"
        binding.tvSecond.text = "2) " + rankingArrayList[1].username
        binding.tvSecondPoint.text = rankingArrayList[1].earn.toString() + " Points Earned"
        binding.tvThird.text = "3) " + rankingArrayList[2].username
        binding.tvThirdPoint.text = rankingArrayList[2].earn.toString() + " Points Earned"

    }

}