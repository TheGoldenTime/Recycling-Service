package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fyp_mobile.databinding.ActivityRewardsDetailBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Cart
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_edit_reward.*
import kotlinx.android.synthetic.main.activity_rewards_detail.*

class RewardsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRewardsDetailBinding

    private lateinit var mRewardDetails: Rewards

    private lateinit var mUserDetails: User

    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!

        FirestoreClass().getRewardDetails(this, id)

        FirestoreClass().getUserDetails(this)

        binding.btnBackRewardDetails.setOnClickListener {
            onBackPressed()
        }

        binding.btnRedeemReward.setOnClickListener {
            if(validateRedeem()){
                redeemReward()
            }
        }

    }

    private fun validateRedeem(): Boolean{
        if(mRewardDetails.quantity < 1){
            Toast.makeText(applicationContext,"Sorry, This Reward Has Sold Out!", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            if(mUserDetails.points < 1){
                Toast.makeText(applicationContext,"Insufficient Points!", Toast.LENGTH_SHORT).show()
                return false
            }
            else{
                return true
            }
        }
    }

    private fun redeemReward(){

        val id = System.currentTimeMillis().toString()

        val status = "pending"

        val cart = Cart(
            id,
            mRewardDetails.name,
            mRewardDetails.points,
            status,
            mRewardDetails.image
        )

        val userPoints = mUserDetails.points - mRewardDetails.points

        val quantity = mRewardDetails.quantity - 1

        //Reward quantity -1 and user point -
        FirestoreClass().userPurchase(this@RewardsDetailActivity, userPoints)

        FirestoreClass().newQuantity(this@RewardsDetailActivity, mRewardDetails.id ,quantity)

        FirestoreClass().addToCart(this@RewardsDetailActivity, cart)

        Toast.makeText(applicationContext,"Reward Redeemed!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, RewardsActivity::class.java)
        startActivity(intent)
        finish()

    }


    fun rewardDetailsSuccess(rewards: Rewards){

        mRewardDetails = rewards

        GlideLoader(this@RewardsDetailActivity).loadRewardPicture(rewards.image, reward_detail_imageView)

        binding.rewardDetailImageName.text = rewards.name
        binding.rewardDetailDescText.text = rewards.desc
        binding.rewardDetailPriceText.text = rewards.points.toString() + " points"
        binding.rewardDetailQuantityText.text = rewards.quantity.toString() + " Available Now"

    }

    fun userDetailsSuccess(user: User){

        mUserDetails = user

    }


}