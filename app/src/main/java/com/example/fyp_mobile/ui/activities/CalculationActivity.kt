package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.example.fyp_mobile.databinding.ActivityCalculationBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Material
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.model.UserList

class CalculationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculationBinding

    private lateinit var mUserDetails: UserList

    private lateinit var userDetails: User

    private lateinit var mMaterialDetails: Material

    private var areaID = ""

    private var scheduleID = ""

    private var userlistID = ""

    //for adding points
    private var mUserID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val extras = intent.extras
        areaID = extras!!.getString("areaID").toString()
        scheduleID = extras.getString("scheduleID").toString()
        userlistID = extras.getString("userlistID").toString()

        FirestoreClass().getUserlistDetail(this, areaID, scheduleID, userlistID)

        FirestoreClass().getMaterials(this)

        binding.backBtnCalculation.setOnClickListener {
            onBackPressed()
        }

        binding.btnCalculate.setOnClickListener {
            if(validationCalculation()){
                calculation()
            }
        }
    }

    private fun validationCalculation(): Boolean{
        val paper = binding.etPaper.text.toString()
        val plastic = binding.etPlastic.text.toString()
        val aluminum = binding.etAluminum.text.toString()
        val metal = binding.etMetal.text.toString()
        val others = binding.etOthers.text.toString()

        if(paper.isEmpty()){
            Toast.makeText(applicationContext,"Please Enter Weight!", Toast.LENGTH_SHORT).show()
            return false
        }

        else if(paper.isDigitsOnly()){
            if(paper.toInt() <= 0){
                Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                return false
            }
            else{
                if(plastic.isEmpty()){
                    Toast.makeText(applicationContext,"Please Enter Weight!", Toast.LENGTH_SHORT).show()
                    return false
                }

                else if(plastic.isDigitsOnly()){
                    if(plastic.toInt() <= 0){
                        Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    else{
                        if(aluminum.isEmpty()){
                            Toast.makeText(applicationContext,"Please Enter Weight!", Toast.LENGTH_SHORT).show()
                            return false
                        }

                        else if(aluminum.isDigitsOnly()){
                            if(aluminum.toInt() <= 0){
                                Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                                return false
                            }
                            else{
                                if(metal.isEmpty()){
                                    Toast.makeText(applicationContext,"Please Enter Weight!", Toast.LENGTH_SHORT).show()
                                    return false
                                }

                                else if(metal.isDigitsOnly()){
                                    if(metal.toInt() <= 0){
                                        Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                                        return false
                                    }
                                    else{
                                        if(others.isEmpty()){
                                            Toast.makeText(applicationContext,"Please Enter Weight!", Toast.LENGTH_SHORT).show()
                                            return false
                                        }

                                        else if(others.isDigitsOnly()){
                                            if(others.toInt() <= 0){
                                                Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                                                return false
                                            }
                                            else{
                                                return true
                                            }
                                        }

                                        else{
                                            Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                                            return false
                                        }
                                    }
                                }

                                else{
                                    Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                                    return false
                                }
                            }
                        }

                        else{
                            Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                }

                else{
                    Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }

        else{
            Toast.makeText(applicationContext,"Please Enter Valid Weight!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun calculation(){

        //RM 1 = 4700 point
        val paper = binding.etPaper.text.toString().toInt()
        val plastic = binding.etPlastic.text.toString().toInt()
        val aluminum = binding.etAluminum.text.toString().toInt()
        val metal = binding.etMetal.text.toString().toInt()
        val others = binding.etOthers.text.toString().toInt()

        val calculate = paper * 1 + plastic * 2 + aluminum * 10 + metal * 3 + others * 1
        val total = userDetails.points + calculate
        val totalEarn = userDetails.earn + calculate

        val matPaper = mMaterialDetails.paper + paper
        val matPlastic = mMaterialDetails.plastic + plastic
        val matAluminum = mMaterialDetails.aluminum + aluminum
        val matMetal = mMaterialDetails.metal + metal
        val matOthers = mMaterialDetails.others + others

        val mat = Material(
            matPaper,
            matPlastic,
            matAluminum,
            matMetal,
            matOthers
        )
        FirestoreClass().updateMaterials(this, mat)

        FirestoreClass().updateCalculatePoint(this, total, totalEarn, mUserID)

        Toast.makeText(applicationContext, "Granted! $calculate points to " + userDetails.username, Toast.LENGTH_SHORT).show()

        val intents = Intent(this@CalculationActivity, UserListActivity::class.java)
        val extrass = Bundle()
        extrass.putString("areaID", areaID)
        extrass.putString("scheduleID", scheduleID)
        intents.putExtras(extrass)
        startActivity(intents)
        finish()
    }

    fun userlistDetailsSuccess(userList: UserList){
        mUserDetails = userList

        FirestoreClass().getUserCalculation(this, mUserDetails.email)
    }

    fun userIDGetSuccess(userID: String){

        Log.e("calculation", userID)
        mUserID = userID

        FirestoreClass().getUserDetailsCalculation(this, mUserID)
    }

    fun userDetailsSuccess(user: User){
        userDetails = user
    }

    fun loadMaterial(materials: Material){

        mMaterialDetails = materials

    }
}