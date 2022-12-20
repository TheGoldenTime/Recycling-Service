package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.adapter.AdapterScheduleAdmin
import com.example.fyp_mobile.adapter.AdapterUserList
import com.example.fyp_mobile.databinding.ActivityEditScheduleBinding
import com.example.fyp_mobile.databinding.ActivityUserListBinding
import com.example.fyp_mobile.model.Schedule
import com.example.fyp_mobile.model.UserList
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.firestore.*

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold schedule data
    private lateinit var userlistArrayList: ArrayList<UserList>

    //Adapter
    private lateinit var adapterUserList: AdapterUserList

    //Firestore
    private lateinit var db : FirebaseFirestore

    private var areaID = ""

    private var scheduleID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val extras = intent.extras
        areaID = extras!!.getString("areaID").toString()
        scheduleID = extras.getString("scheduleID").toString()

        recyclerView = binding.userlistRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userlistArrayList = arrayListOf()

        adapterUserList = AdapterUserList(userlistArrayList, this, areaID, scheduleID)

        recyclerView.adapter = adapterUserList

        EventChangeListener()

        binding.backBtnUserList.setOnClickListener{
            onBackPressed()
        }

    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection(Constants.AREA)
            .document(areaID)
            .collection(Constants.SCHEDULE)
            .document(scheduleID)
            .collection(Constants.USERLIST)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ){
                    if(error != null){
                        Log.e("userlist",error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){

                            userlistArrayList.add(dc.document.toObject(UserList::class.java))

                        }
                    }

                    adapterUserList.notifyDataSetChanged()
                }


            })
    }
}