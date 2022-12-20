package com.example.fyp_mobile.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fyp_mobile.R
import com.example.fyp_mobile.model.Event
import com.example.fyp_mobile.model.Participant
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.model.UserList
import com.example.fyp_mobile.ui.activities.AdminEventActivity
import com.example.fyp_mobile.ui.activities.AdminRewardsActivity
import com.example.fyp_mobile.ui.activities.CalculationActivity
import com.example.fyp_mobile.ui.activities.EditEventActivity
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.firestore.*

class AdapterEventAdmin(private val eventList : ArrayList<Event>, private val context: Context) :
    RecyclerView.Adapter<AdapterEventAdmin.HolderEvent>(){

    //private lateinit var participantArrayList: ArrayList<Participant>

    //private lateinit var userArrayList: ArrayList<User>

    private var eventPoints = 0

    private var grantedPoints = 0

    private var totalEarn = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterEventAdmin.HolderEvent {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_admin_event,parent, false)

        return HolderEvent(itemView)
    }

    override fun onBindViewHolder(holder: AdapterEventAdmin.HolderEvent, position: Int) {

        //participantArrayList = arrayListOf()

        //userArrayList = arrayListOf()

        val event : Event = eventList[position]

        Glide.with(context).load(event.image).into(holder.eventImage)

        holder.eventName.text = event.name

        holder.deleteBtn.setOnClickListener{
            finishEvent(event, holder)
            eventPoints = event.points
            getParticipants(event)
            val intent = (Intent(context, AdminEventActivity::class.java))
            context.startActivity(intent)
        }

        holder.editBtn.setOnClickListener {
            val intent = (Intent(context, EditEventActivity::class.java))
            intent.putExtra("id", event.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return eventList.size

    }

    private fun finishEvent(model: Event, holder: HolderEvent) {
        val id = model.id
        val del = FirebaseFirestore.getInstance()
        del.collection(Constants.EVENT)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.e("event", "Event Deleted!")
            }
            .addOnFailureListener{ e->
                Log.e("event", "Event Fail to Delete!")
            }
    }

    private fun getParticipants(model: Event){
        val id = model.id

        val participantArrayList: ArrayList<Participant> = arrayListOf()

        val participant = FirebaseFirestore.getInstance()
        participant.collection(Constants.EVENT)
            .document(id)
            .collection(Constants.PARTICIPANT)
            .whereEqualTo("status", "Present")
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

                    getUserDetails(participantArrayList)
                }


            })
    }

    private fun getUserDetails(participantArrayList: ArrayList<Participant>){
        val user = FirebaseFirestore.getInstance()

        val userArrayList: ArrayList<User> = arrayListOf()

        participantArrayList.forEachIndexed { index , participant ->
            Log.e("participant", index.toString())
            user.collection(Constants.USERS)
                .whereEqualTo("email", participantArrayList[index].email)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ){
                        if(error != null){
                            Log.e("user",error.message.toString())
                            return
                        }

                        Log.e("participant",participantArrayList[index].email)

                        for(dc : DocumentChange in value?.documentChanges!!){
                            if (dc.type == DocumentChange.Type.ADDED){

                                //check
                                userArrayList.add(index, dc.document.toObject(User::class.java))
                                grantedPoints = userArrayList[index].points + eventPoints
                                totalEarn = userArrayList[index].earn + eventPoints
                                grantUserPoints(dc.document.id)

                            }
                        }
                        Log.e("user",userArrayList[index].email)

                    }


                })
        }

    }

    private fun grantUserPoints(userID: String){
        val grant = FirebaseFirestore.getInstance()
        grant.collection(Constants.USERS)
            .document(userID)
            .update(mapOf(
                "points" to grantedPoints,
                "earn" to totalEarn
            ))
            .addOnSuccessListener {
                Log.e("calculation", "Points Added!")

            }
            .addOnFailureListener{ e->
                Log.e("calculation", "Fail To Update")
            }
    }

    public class HolderEvent(itemView : View) : RecyclerView.ViewHolder(itemView){

        val eventImage : ImageView = itemView.findViewById(R.id.admin_event_image)
        val eventName : TextView = itemView.findViewById(R.id.text_event_name)
        val deleteBtn : Button = itemView.findViewById(R.id.event_delete_btn)
        val editBtn : Button = itemView.findViewById(R.id.event_edit_btn)

    }

}