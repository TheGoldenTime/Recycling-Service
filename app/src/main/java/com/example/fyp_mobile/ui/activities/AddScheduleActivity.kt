package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.example.fyp_mobile.databinding.ActivityAddScheduleBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Schedule
import java.util.regex.Pattern

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddScheduleBinding

    private var areaID = ""

    private val TIME: Pattern = Pattern.compile(
        "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)-(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        areaID = intent.getStringExtra("id")!!

        binding.backBtnSchedule.setOnClickListener {
            onBackPressed()
        }

        binding.btnSubmitSchedule.setOnClickListener{
            if(validateSchedule()){
                addSchedule()
                val intent = Intent(this@AddScheduleActivity, AdminScheduleActivity::class.java)
                intent.putExtra("id", areaID)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validateSchedule(): Boolean{

        val day = binding.etScheduleDay.text.toString().trim()
        val time = binding.etScheduleTime.text.toString().trim()

        if((day == "Monday") || (day == "Tuesday") || (day == "Wednesday") || (day == "Thursday") || (day == "Friday") || (day == "Saturday") || (day == "Sunday")){
            if(TIME.matcher(time).matches()){
                return true
            }

            Toast.makeText(applicationContext,"Please Follow the Time Format XX:XX AM-XX:XX PM!", Toast.LENGTH_SHORT).show()
            return false

        }

        else{
            Toast.makeText(applicationContext,"Please Enter A correct Day!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun addSchedule(){

        Log.e("schedule","Adding!")
        val id = System.currentTimeMillis().toString()
        Log.e("schedule",id)

        val day = binding.etScheduleDay.text.toString().trim()
        val time = binding.etScheduleTime.text.toString().trim()

        val schedule = Schedule(
            id,
            day,
            time
        )

        FirestoreClass().addSchedule(this@AddScheduleActivity, schedule,areaID)
        Log.e("schedule","Added?")

    }
}