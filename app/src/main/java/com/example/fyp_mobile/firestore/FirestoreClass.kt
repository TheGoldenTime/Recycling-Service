package com.example.fyp_mobile.firestore

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.fyp_mobile.model.*
import com.example.fyp_mobile.ui.activities.*
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: Activity, userInfo: User){
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                Log.e("firestoreclass","Register Ok")
                //activity.userRegisterSuccess()
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Register Not Ok")
            }
    }

    fun registerCart(activity: Activity, cart: Cart, userID: String) {
        mFireStore.collection(Constants.USERS)
            .document(userID)
            .collection(Constants.CART)
            .document(cart.id)
            .set(cart, SetOptions.merge())
            .addOnSuccessListener {
                Log.e("firestoreclass","Cart Ok")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Cart Not Ok")
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("firestoreclass", document.toString())
                Log.e("firestoreclass","Login Success!")
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.GREENDAY_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // Key: logged_in_username
                // Value: username
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.username}"
                )
                editor.apply()

                when(activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is UserProfActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is AdminProfActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is EventDetailActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is RegisterScheduleActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is DonationActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is RewardsDetailActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","Data Failed")
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        Log.e("update", "Update Profile in Firestore")
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        Log.e("update", "Completed")
                        activity.userProfileUpdateSuccess()
                    }
                    is AdminProfileActivity ->{
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e->
                Log.e("update", "Error while updating user details.")
                when(activity){
                    is UserProfileActivity -> {
                        Log.e("update", "fail to update prof")
                    }
                    is AdminProfileActivity ->{
                        Log.e("update", "fail to update prof")
                    }
                }
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->

            Log.e("update", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("update", uri.toString())
                    when (activity){
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AdminProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddRewardActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is EditRewardActivity ->{
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddEventActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is EditEventActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }

        }
            .addOnFailureListener{ exception ->
                Log.e("update", "opps in fail")
                when (activity){
                    is UserProfileActivity ->{
                        Log.e("update", "update failed")
                    }
                    is AdminProfileActivity ->{
                        Log.e("update", "update failed")
                    }
                    is AddRewardActivity -> {
                        Log.e("update", "update failed")
                    }
                    is EditRewardActivity ->{
                        Log.e("update", "update failed")
                    }
                    is AddEventActivity -> {
                        Log.e("update", "update failed")
                    }
                    is EditEventActivity -> {
                        Log.e("update", "update failed")
                    }
                }

                Log.e(activity.javaClass.simpleName, exception.message, exception)

            }
    }

    //Rewards
    fun addReward(activity: Activity, rewards: Rewards){
        mFireStore.collection(Constants.REWARD)
            .document(rewards.id)
            .set(rewards, SetOptions.merge())
            .addOnSuccessListener {
                Log.e("firestoreclass","Rewards Added")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Rewards Not Added")
            }
    }

    fun getRewardDetails(activity: Activity, id: String){
        mFireStore.collection(Constants.REWARD)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                Log.e("firestoreclass", document.toString())
                Log.e("firestoreclass","Login Success!")
                val reward = document.toObject(Rewards::class.java)!!

                when(activity){
                    is EditRewardActivity -> {
                        activity.rewardDetailsSuccess(reward)
                    }
                    is RewardsDetailActivity -> {
                        activity.rewardDetailsSuccess(reward)
                    }

                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","Data Failed")
            }
    }

    fun updateRewardData(activity: Activity, rewardHashMap: HashMap<String, Any>, id: String){
        Log.e("update", "Update Profile in Firestore")
        mFireStore.collection(Constants.REWARD)
            .document(id)
            .update(rewardHashMap)
            .addOnSuccessListener {
                when(activity){
                    is EditRewardActivity -> {
                        Log.e("update", "Completed")
                        activity.rewardDetailsUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e->
                Log.e("update", "Error while updating reward details.")
                when(activity){
                    is EditRewardActivity -> {
                        Log.e("update", "fail to update reward")
                    }
                }
            }
    }

    fun userPurchase(activity: Activity, points: Int){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(mapOf(
                "points" to points,
            ))
            .addOnSuccessListener {
                Log.e("firestoreclass","Points Donated")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Points Failed")
            }
    }

    fun newQuantity(activity: Activity, rewardID: String, quantity: Int){
        mFireStore.collection(Constants.REWARD)
            .document(rewardID)
            .update(mapOf(
                "quantity" to  quantity,
            ))
            .addOnSuccessListener {
                Log.e("firestoreclass","Points Donated")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Points Failed")
            }
    }

    //Cart
    fun addToCart(activity: Activity, cart: Cart){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .collection(Constants.CART)
            .document(cart.id)
            .set(cart)
            .addOnSuccessListener {
                Log.e("firestoreclass","Added to Cart")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Not Add to Cart")
            }

    }

    //Event

    fun addEvent(activity: Activity, event: Event){
        mFireStore.collection(Constants.EVENT)
            .document(event.id)
            .set(event, SetOptions.merge())
            .addOnSuccessListener {
                Log.e("firestoreclass","Rewards Added")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Rewards Not Added")
            }
    }

    fun getEventDetails(activity: Activity, id: String){
        mFireStore.collection(Constants.EVENT)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                Log.e("firestoreclass", document.toString())
                Log.e("firestoreclass","Login Success!")
                val event = document.toObject(Event::class.java)!!

                when(activity){
                    is EditEventActivity -> {
                        activity.eventDetailsSuccess(event)
                    }
                    is EventDetailActivity -> {
                        activity.eventDetailsSuccess(event)
                    }

                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","Data Failed")
            }
    }

    fun updateEventData(activity: Activity, eventHashMap: HashMap<String, Any>, id: String){
        Log.e("update", "Update Profile in Firestore")
        mFireStore.collection(Constants.EVENT)
            .document(id)
            .update(eventHashMap)
            .addOnSuccessListener {
                when(activity){
                    is EditEventActivity -> {
                        Log.e("update", "Completed")
                        activity.eventDetailsUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e->
                Log.e("update", "Error while updating reward details.")
                when(activity){
                    is EditEventActivity -> {
                        Log.e("update", "fail to update reward")
                    }
                }
            }
    }

    fun joinEvent(activity: Activity, participant: Participant, eventID: String){
        mFireStore.collection(Constants.EVENT)
            .document(eventID)
            .collection(Constants.PARTICIPANT)
            .document(participant.id)
            .set(participant)
            .addOnSuccessListener {
                Log.e("firestoreclass","Participant Added")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Participant Not Added")
            }
    }

    fun checkParticipant(activity: Activity, eventID: String, email: String){
        mFireStore.collection(Constants.EVENT)
            .document(eventID)
            .collection(Constants.PARTICIPANT)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { document ->
                Log.e("firestoreclass", document.toString())
                Log.e("firestoreclass","User Exist")

                when(activity){
                    is EventDetailActivity -> {
                        activity.userRegisterEvent(false)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","New User Registered")
                when(activity){
                    is EventDetailActivity -> {
                        activity.userRegisterEvent(true)
                    }
                }
            }
    }

    //Schedule

    fun addSchedule(activity: Activity, schedule: Schedule, areaID: String){
        mFireStore.collection(Constants.AREA)
            .document(areaID)
            .collection(Constants.SCHEDULE)
            .document(schedule.id)
            .set(schedule, SetOptions.merge())
            .addOnSuccessListener {
                Log.e("firestoreclass","Rewards Added")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Rewards Not Added")
            }
    }

    fun getScheduleDetail(activity: Activity, areaID: String, scheduleID: String){
        mFireStore.collection(Constants.AREA)
            .document(areaID)
            .collection(Constants.SCHEDULE)
            .document(scheduleID)
            .get()
            .addOnSuccessListener { document ->
                Log.e("firestoreclass", document.toString())
                Log.e("firestoreclass","Login Success!")
                val schedule = document.toObject(Schedule::class.java)!!

                when(activity){
                    is EditScheduleActivity -> {
                        activity.scheduleDetailsSuccess(schedule)
                    }

                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","Data Failed")
            }
    }

    fun updateSchedule(activity: Activity, scheduleHashMap: HashMap<String, Any>, areaID: String, scheduleID: String){
        Log.e("update", "Update Profile in Firestore")
        mFireStore.collection(Constants.AREA)
            .document(areaID)
            .collection(Constants.SCHEDULE)
            .document(scheduleID)
            .update(scheduleHashMap)
            .addOnSuccessListener {
                when(activity){
                    is EditScheduleActivity -> {
                        Log.e("update", "Completed")
                        activity.scheduleUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e->
                Log.e("update", "Error while updating reward details.")
                when(activity){
                    is EditEventActivity -> {
                        Log.e("update", "fail to update reward")
                    }
                }
            }
    }

    //UserList
    fun registerSchedule(activity: Activity, userlist: UserList, areaID: String, scheduleID: String){
        mFireStore.collection(Constants.AREA)
            .document(areaID)
            .collection(Constants.SCHEDULE)
            .document(scheduleID)
            .collection(Constants.USERLIST)
            .document(userlist.id)
            .set(userlist)
            .addOnSuccessListener {
                Log.e("firestoreclass","UserList Added")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","UserList Not Added")
            }

    }

    fun checkUserList(activity: Activity, areaID: String, scheduleID: String, email: String){
        mFireStore.collection(Constants.AREA)
            .document(areaID)
            .collection(Constants.SCHEDULE)
            .document(scheduleID)
            .collection(Constants.USERLIST)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { document ->
                Log.e("firestoreclass", document.toString())
                Log.e("firestoreclass","User Exist")

                when(activity){
                    is RegisterScheduleActivity -> {
                        activity.userRegisterSchedule("false")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","New User Registered")
                when(activity){
                    is RegisterScheduleActivity -> {
                        activity.userRegisterSchedule("true")
                    }
                }
            }
    }

    //Calculation
    fun getUserlistDetail(activity: Activity, areaID: String, scheduleID: String, userlistID: String){
        mFireStore.collection(Constants.AREA)
            .document(areaID)
            .collection(Constants.SCHEDULE)
            .document(scheduleID)
            .collection(Constants.USERLIST)
            .document(userlistID)
            .get()
            .addOnSuccessListener { document ->
                Log.e("firestoreclass", document.toString())
                Log.e("firestoreclass","Get Userlist Success!")
                val userlist = document.toObject(UserList::class.java)!!

                when(activity){
                    is CalculationActivity -> {
                        activity.userlistDetailsSuccess(userlist)
                    }

                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","Data Failed")
            }
    }

    fun getUserCalculation(activity: Activity, email: String){
        mFireStore.collection(Constants.USERS)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener{ documents ->
                Log.e("firestoreclass", documents.toString())
                Log.e("firestoreclass","Get User Success!")

                for (document in documents) {
                    Log.e("firestoreclass", document.id)
                    val userID = document.id

                    when(activity){
                        is CalculationActivity -> {
                            activity.userIDGetSuccess(userID)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","Data Failed")
            }
    }

    fun updateCalculatePoint(activity: Activity, point: Int, earn: Int, userID: String){
        mFireStore.collection(Constants.USERS)
            .document(userID)
            .update(mapOf(
                "points" to point,
                "earn" to earn
            ))
            .addOnSuccessListener {
                Log.e("calculation", "Points Added!")

            }
            .addOnFailureListener{ e->
                Log.e("calculation", "Fail To Update")
            }
    }

    fun getUserDetailsCalculation(activity: Activity, userID: String){
        mFireStore.collection(Constants.USERS)
            .document(userID)
            .get()
            .addOnSuccessListener { document ->
                Log.e("firestoreclass", document.toString())
                Log.e("firestoreclass","Get Points!")
                val user = document.toObject(User::class.java)!!

                when(activity){
                    is CalculationActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","Data Failed")
            }
    }

    //Donation
    fun donatePoints(activity: Activity, donation: Int, newPoints: Int){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(mapOf(
                "points" to newPoints,
                "donation" to donation
            ))
            .addOnSuccessListener {
                Log.e("firestoreclass","Points Donated")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Points Failed")
            }

    }

    //Monthly Rankings
    fun resetRanking(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .whereGreaterThanOrEqualTo("earn", 0)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val update: MutableMap<String, Any> = HashMap()
                        update["earn"] = 0
                        mFireStore.collection(Constants.USERS).document(document.id).set(update, SetOptions.merge())
                    }
                }
                Log.e("firestoreclass","Ranking Reset!")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Reset Failed")
            }

    }

    //Split into 3
    fun firstRankPoint(activity: Activity, userID: String, points: Int, earn: Int){
            mFireStore.collection(Constants.USERS)
                .document(userID)
                .update(mapOf(
                    "points" to points,
                    "earn" to earn
                ))
                .addOnSuccessListener {
                    Log.e("firestoreclass","Points Donated")
                }
                .addOnFailureListener{ e ->
                    Log.e("firestoreclass","Points Failed")
                }
    }

    fun secondRankPoint(activity: Activity, userID: String, points: Int, earn: Int){
        mFireStore.collection(Constants.USERS)
            .document(userID)
            .update(mapOf(
                "points" to points,
                "earn" to earn
            ))
            .addOnSuccessListener {
                Log.e("firestoreclass","Points Donated")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Points Failed")
            }
    }

    fun thirdRankPoint(activity: Activity, userID: String, points: Int, earn: Int){
        mFireStore.collection(Constants.USERS)
            .document(userID)
            .update(mapOf(
                "points" to points,
                "earn" to earn
            ))
            .addOnSuccessListener {
                Log.e("firestoreclass","Points Donated")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Points Failed")
            }
    }

    //Reports
    fun getMaterials(activity: Activity){
        mFireStore.collection(Constants.MATERIAL)
            .document("material1")
            .get()
            .addOnSuccessListener { document ->
            Log.e("firestoreclass", document.toString())
            Log.e("firestoreclass","Get Points!")
            val materials = document.toObject(Material::class.java)!!

            when(activity){
                is AdminReportActivity -> {
                    activity.loadMaterial(materials)
                }
                is CalculationActivity ->{
                    activity.loadMaterial(materials)
                }
            }
        }
            .addOnFailureListener { e ->
                Log.e("firestoreclass","Data Failed")
            }
    }

    fun updateMaterials(activity: Activity, material: Material){
        mFireStore.collection(Constants.MATERIAL)
            .document("material1")
            .update(mapOf(
                "paper" to material.paper,
                "plastic" to material.plastic,
                "aluminum" to material.aluminum,
                "metal" to material.metal,
                "others" to material.others
            ))
            .addOnSuccessListener {
                Log.e("firestoreclass","Points Donated")
            }
            .addOnFailureListener{ e ->
                Log.e("firestoreclass","Points Failed")
            }
    }

}

