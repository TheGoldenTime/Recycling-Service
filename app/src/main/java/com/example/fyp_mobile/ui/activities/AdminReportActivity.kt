package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityAdminReportBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Material
import com.example.fyp_mobile.model.Participant
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.*

class AdminReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminReportBinding

    private lateinit var mMaterialDetail: Material

    private lateinit var  userArrayList: ArrayList<User>

    private var pointsSpent = 0

    private var pointsGranted = 0

    private var pointsDonated = 0

    private var pointsExist = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userArrayList = arrayListOf()

        FirestoreClass().getMaterials(this)

        getPointsReport()

        val nav = findViewById<BottomNavigationView>(R.id.adminNav)
        nav.setOnNavigationItemSelectedListener {
            when(it.itemId){

                R.id.navigation_admin_rewards -> {
                    val intent = Intent(this, AdminRewardsActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_admin_recycle -> {
                    val intent = Intent(this, AdminRecycleActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_admin_report -> {
                    val intent = Intent(this, AdminReportActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_admin_event -> {
                    val intent = Intent(this, AdminEventActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.navigation_admin_profile -> {
                    val intent = Intent(this, AdminProfActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    fun loadMaterial(materials: Material){

        mMaterialDetail = materials

        binding.materialPaper.text = "Paper Collected : " + mMaterialDetail.paper.toString() + " g"
        binding.materialPlastic.text = "Plastic Collected : " + mMaterialDetail.plastic.toString() + " g"
        binding.materialAluminum.text = "Aluminum Collected : " + mMaterialDetail.aluminum.toString() + " g"
        binding.materialMetal.text = "Metal Collected : " + mMaterialDetail.metal.toString() + " g"
        binding.materialOthers.text = "Others Collected : " + mMaterialDetail.others.toString() + " g"

    }

    private fun getPointsReport(){

        val user = FirebaseFirestore.getInstance()
        user.collection(Constants.USERS)
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

                            userArrayList.add(dc.document.toObject(User::class.java))

                        }
                    }

                    loadPointsReport(userArrayList)
                }


            })
    }

    private fun loadPointsReport(userArrayList: ArrayList<User>){

        userArrayList.forEachIndexed { index, user ->

            pointsGranted += userArrayList[index].earn
            pointsExist += userArrayList[index].points
            pointsDonated += userArrayList[index].donation

        }

        pointsSpent = pointsGranted - pointsExist

        binding.pointsSpent.text = "Points Spent : " + pointsSpent.toString()
        binding.pointsGrant.text = "Points Granted : " + pointsGranted.toString()
        binding.pointsDonate.text = "Points Donated " + pointsDonated.toString()
    }
}