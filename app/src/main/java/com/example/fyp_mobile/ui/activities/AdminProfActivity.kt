package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityAdminProfBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.Constants
import com.example.fyp_mobile.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_user_prof.*

class AdminProfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProfBinding

    private lateinit var mUserDetails: User

    private lateinit var rankingArrayList: ArrayList<User>

    private lateinit var db : FirebaseFirestore

    private var first = 0
    private var second = 0
    private var third = 0

    private var firstEarn = 0
    private var secondEarn = 0
    private var thirdEarn= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        rankingArrayList = arrayListOf()

        EventChangeListener()

        binding.tvEdit.setOnClickListener{
            val intent = Intent(this@AdminProfActivity, AdminProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
            startActivity(intent)
            finish()
        }

        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@AdminProfActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener{
            val intent = Intent(this@AdminProfActivity, AdminRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnResetRanking.setOnClickListener {
            FirestoreClass().resetRanking(this)
            FirestoreClass().firstRankPoint(this, rankingArrayList[0].id, first, firstEarn)
            FirestoreClass().secondRankPoint(this, rankingArrayList[1].id, second, secondEarn)
            FirestoreClass().thirdRankPoint(this, rankingArrayList[2].id, third, thirdEarn)
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
            val intent = Intent(this, AdminRewardsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getUserDetails(){
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User){

        mUserDetails = user

        GlideLoader(this@AdminProfActivity).loadUserPicture(user.image, iv_user_photo)
        binding.tvName.text = "${user.username}"
        binding.tvEmail.text = user.email
        binding.tvMobile.text = "${user.mobile}"
    }

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
                    grantPoints()
                }


            })
    }

    private fun grantPoints(){
        first = rankingArrayList[0].points + 50000
        second = rankingArrayList[1].points + 30000
        third = rankingArrayList[2].points + 10000

        firstEarn = rankingArrayList[0].earn + 50000
        secondEarn = rankingArrayList[1].earn + 30000
        thirdEarn = rankingArrayList[2].earn + 10000
    }

    override fun onResume(){
        super.onResume()
        getUserDetails()
    }
}