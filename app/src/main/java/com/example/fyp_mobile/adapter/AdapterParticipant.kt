package com.example.fyp_mobile.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fyp_mobile.R
import com.example.fyp_mobile.model.Participant
import com.example.fyp_mobile.ui.activities.ParticipantActivity
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore


class AdapterParticipant(private val participantList : ArrayList<Participant>, private val context: Context, private val eventID: String) :
RecyclerView.Adapter<AdapterParticipant.HolderParticipant>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterParticipant.HolderParticipant {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_event, parent, false)

        return HolderParticipant(itemView)

    }

    override fun onBindViewHolder(holder: AdapterParticipant.HolderParticipant, position: Int) {
        val participant: Participant = participantList[position]

        Glide.with(context).load(participant.image).into(holder.participantImage)

        holder.txtName.text = participant.name

        holder.txtStatus.text = participant.status

        holder.txtEmail.text = participant.email

        holder.txtPresent.setOnClickListener {
            updatePresent(participant, holder)
            val intent = (Intent(context, ParticipantActivity::class.java))
            intent.putExtra("id", eventID)
            context.startActivity(intent)
            (context as ParticipantActivity).finish()
        }

        holder.txtAbsent.setOnClickListener {
            updateAbsent(participant, holder)
            val intent = (Intent(context, ParticipantActivity::class.java))
            intent.putExtra("id", eventID)
            context.startActivity(intent)
            (context as ParticipantActivity).finish()
        }


    }

    override fun getItemCount(): Int {

        return participantList.size

    }

    private fun updatePresent(participant: Participant, holder: AdapterParticipant.HolderParticipant) {
        val id = participant.id
        val update = FirebaseFirestore.getInstance()
        update.collection(Constants.EVENT)
            .document(eventID)
            .collection(Constants.PARTICIPANT)
            .document(id)
            .update(mapOf(
                "status" to "Present"
            ))
            .addOnSuccessListener {
                Log.e("participant", "Status Present!")

            }
            .addOnFailureListener{ e->
                Log.e("participant", "Fail To Update")
            }
    }

    private fun updateAbsent(participant: Participant, holder: AdapterParticipant.HolderParticipant) {
        val id = participant.id
        val update = FirebaseFirestore.getInstance()
        update.collection(Constants.EVENT)
            .document(eventID)
            .collection(Constants.PARTICIPANT)
            .document(id)
            .update(mapOf(
                "status" to "Absent"
            ))
            .addOnSuccessListener {
                Log.e("participant", "Status Absent!")

            }
            .addOnFailureListener{ e->
                Log.e("participant", "Fail To Update")
            }
    }

    public class HolderParticipant(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val participantImage: ImageView = itemView.findViewById(R.id.eventIV)
        val txtName: TextView = itemView.findViewById(R.id.event_name_text)
        val txtStatus: TextView = itemView.findViewById(R.id.event_status_text)
        val txtEmail: TextView = itemView.findViewById(R.id.event_email_text)
        val txtPresent: TextView = itemView.findViewById(R.id.event_present_text)
        val txtAbsent: TextView = itemView.findViewById(R.id.event_absent_text)

    }

}