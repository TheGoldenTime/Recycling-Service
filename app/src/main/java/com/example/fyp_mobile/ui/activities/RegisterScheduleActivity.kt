package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fyp_mobile.databinding.ActivityRegisterScheduleBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.model.UserList
import kotlin.properties.Delegates

class RegisterScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterScheduleBinding

    private var areaID = ""

    private var scheduleID = ""

    private lateinit var mUserDetails: User

    private var valid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirestoreClass().getUserDetails(this)

        val intent = intent
        val extras = intent.extras
        areaID = extras!!.getString("areaID").toString()
        scheduleID = extras.getString("scheduleID").toString()

        binding.backBtnRegisterSchedule.setOnClickListener {
            onBackPressed()
        }

        binding.btnUseAddress.setOnClickListener {
            if(valid == "true"){
                useCurrentAddress()
            }
            else{
                Toast.makeText(applicationContext,"You Already Registered!", Toast.LENGTH_SHORT).show()
                val intended = Intent(this, ScheduleActivity::class.java)
                intended.putExtra("id", areaID)
                startActivity(intended)
                finish()
            }
        }

        binding.btnRegisterSchedule.setOnClickListener {
            if(valid == "true"){
                registerSchedule()
            }
            else{
                Toast.makeText(applicationContext,"You Already Registered!", Toast.LENGTH_SHORT).show()
                val intended = Intent(this, ScheduleActivity::class.java)
                intended.putExtra("id", areaID)
                startActivity(intended)
                finish()
            }
        }
    }

//    private fun validateRegisterSchedule(valid: Boolean): Boolean{
//        if(valid){
//
//        }
//        else{
//            Toast.makeText(applicationContext,"You Already Register!", Toast.LENGTH_SHORT).show()
//        }
//    }

    fun userRegisterSchedule(user: String){
        valid = user
        Log.e("valid", valid)
    }

    fun userDetailsSuccess(user: User){

        mUserDetails = user
        Log.e("user", mUserDetails.email)
        FirestoreClass().checkUserList(this, areaID, scheduleID, mUserDetails.email)

    }

    private fun useCurrentAddress(){

        val id = System.currentTimeMillis().toString()
        val address = mUserDetails.address

        val userlist = UserList(
            id,
            mUserDetails.username,
            mUserDetails.email,
            address
        )

        FirestoreClass().registerSchedule(this, userlist, areaID, scheduleID)

        Toast.makeText(applicationContext,"Schedule Registered!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ScheduleActivity::class.java)
        intent.putExtra("id", areaID)
        startActivity(intent)
        finish()
    }

    private fun registerSchedule(){

        val id = System.currentTimeMillis().toString()
        val address = binding.etRegisterScheduleAddress.text.toString().trim()

        val userlist = UserList(
            id,
            mUserDetails.username,
            mUserDetails.email,
            address
        )

        FirestoreClass().registerSchedule(this, userlist, areaID, scheduleID)

        Toast.makeText(applicationContext,"Schedule Registered!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ScheduleActivity::class.java)
        intent.putExtra("id", areaID)
        startActivity(intent)
        finish()
    }
}