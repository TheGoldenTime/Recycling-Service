package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fyp_mobile.databinding.ActivityEventDetailBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Event
import com.example.fyp_mobile.model.Participant
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.activity_rewards_detail.*
import kotlinx.android.synthetic.main.activity_user_prof.*

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding

    private lateinit var mEventDetails: Event

    private lateinit var mUserDetails: User

    private var eventID = ""

    private var valid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventID = intent.getStringExtra("id")!!

        FirestoreClass().getEventDetails(this, eventID)

        FirestoreClass().getUserDetails(this)

        binding.btnBackEventDetails.setOnClickListener {
            onBackPressed()
        }

        binding.btnParticipate.setOnClickListener {
            if(valid){
                val id = System.currentTimeMillis().toString()
                val name = mUserDetails.username
                val email = mUserDetails.email
                val status = "Pending"
                val image = mUserDetails.image

                val participant = Participant(
                    id,
                    name,
                    email,
                    status,
                    image
                )

                FirestoreClass().joinEvent(this@EventDetailActivity, participant, eventID)

                Toast.makeText(applicationContext,"Event Participated!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, EventActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(applicationContext,"You Already Participated In this Event!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, EventActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun userRegisterEvent(user: Boolean){
        valid = user
        Log.e("valid", valid.toString())
    }

    fun userDetailsSuccess(user: User){

        mUserDetails = user
        Log.e("user", mUserDetails.email)
        FirestoreClass().checkParticipant(this, eventID, mUserDetails.email)

    }

    fun eventDetailsSuccess(event: Event){

        mEventDetails = event

        GlideLoader(this@EventDetailActivity).loadRewardPicture(event.image, event_detail_imageView)

        binding.eventDetailImageName.text = event.name
        binding.eventDetailDescText.text = event.desc
        binding.eventDetailPointsText.text = event.points.toString() + " points"
        binding.eventDetailDateText.text = event.date
        binding.eventDetailTimeText.text = event.time
        binding.eventDetailVenueText.text = event.venue

    }
}