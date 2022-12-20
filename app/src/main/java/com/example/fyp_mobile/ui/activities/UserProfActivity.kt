package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityUserProfBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.Constants
import com.example.fyp_mobile.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_prof.*
import kotlinx.android.synthetic.main.activity_user_prof.iv_user_photo

class UserProfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfBinding

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.tvEdit.setOnClickListener{
            val intent = Intent(this@UserProfActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
            startActivity(intent)
            finish()
        }

        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@UserProfActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnDonation.setOnClickListener {
            val intent = Intent(this@UserProfActivity, DonationActivity::class.java)
            startActivity(intent)
        }

        binding.btnMonthlyRankings.setOnClickListener {
            val intent = Intent(this@UserProfActivity, RankingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener{
            val intent = Intent(this, RewardsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getUserDetails(){
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User){

        mUserDetails = user

        GlideLoader(this@UserProfActivity).loadUserPicture(user.image, iv_user_photo)
        binding.tvName.text = "${user.username}"
        binding.tvPoints.text = user.points.toString() + " Points"
        binding.tvEmail.text = user.email
        binding.tvMobile.text = "${user.mobile}"
    }

    override fun onResume(){
        super.onResume()
        getUserDetails()
    }
}