package com.example.fyp_mobile.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp_mobile.adapter.AdapterCart
import com.example.fyp_mobile.adapter.AdapterParticipant
import com.example.fyp_mobile.databinding.ActivityParticipantBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Cart
import com.example.fyp_mobile.model.Participant
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.firestore.*

class ParticipantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParticipantBinding

    private lateinit var recyclerView: RecyclerView

    //Array to hold cart data
    private lateinit var participantArrayList: ArrayList<Participant>

    //Adapter
    private lateinit var adapterParticipant: AdapterParticipant

    //Firestore
    private lateinit var db : FirebaseFirestore

    //Event ID
    private var eventID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getting Event ID
        eventID = intent.getStringExtra("id")!!

        recyclerView = binding.participantRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        participantArrayList = arrayListOf()

        adapterParticipant = AdapterParticipant(participantArrayList, this, eventID)

        recyclerView.adapter = adapterParticipant

        EventChangeListener()

        binding.backBtnParticipant.setOnClickListener {
            onBackPressed()
        }
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection(Constants.EVENT)
            .document(eventID)
            .collection(Constants.PARTICIPANT)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ){
                    if(error != null){
                        Log.e("participant",error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){

                            participantArrayList.add(dc.document.toObject(Participant::class.java))

                        }
                    }

                    adapterParticipant.notifyDataSetChanged()
                }


            })
    }
}