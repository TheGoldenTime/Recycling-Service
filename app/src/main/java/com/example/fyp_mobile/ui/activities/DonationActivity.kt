package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fyp_mobile.databinding.ActivityCartBinding
import com.example.fyp_mobile.databinding.ActivityDonationBinding
import com.example.fyp_mobile.databinding.ActivityEventBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.User

class DonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonationBinding

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirestoreClass().getUserDetails(this)

        binding.backBtnDonation.setOnClickListener {
            onBackPressed()
        }

        binding.btnDonate.setOnClickListener {
            if(validateDonation()){
                donation()
            }
        }
    }

    private fun validateDonation(): Boolean{
        if(mUserDetails.points < 1){
            Toast.makeText(applicationContext,"Invalid Points!", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(binding.etDonatePoints.text.toString().toInt() > mUserDetails.points){
            Toast.makeText(applicationContext,"Insufficient Points!", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }

    private fun donation(){

        val donation = binding.etDonatePoints.text.toString().toInt()

        //validate if negative
        val newPoints = mUserDetails.points - donation

        val total = mUserDetails.donation + donation

        FirestoreClass().donatePoints(this, total, newPoints)

        Toast.makeText(applicationContext, "Thank You " + mUserDetails.username + " for donating $donation", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@DonationActivity, RewardsActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun userDetailsSuccess(user: User){

        mUserDetails = user

    }
}