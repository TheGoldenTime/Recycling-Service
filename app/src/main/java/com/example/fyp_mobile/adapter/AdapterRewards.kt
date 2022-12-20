package com.example.fyp_mobile.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fyp_mobile.R
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.ui.activities.AdminRewardsActivity
import com.example.fyp_mobile.ui.activities.EditRewardActivity
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class AdapterRewards(private val rewardList : ArrayList<Rewards>, private val context: Context) :RecyclerView.Adapter<AdapterRewards.HolderRewards>() {

    //private lateinit var binding: CardViewAdminRewardBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRewards.HolderRewards {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_admin_reward,parent, false)

        return HolderRewards(itemView)
    }

    override fun onBindViewHolder(holder: AdapterRewards.HolderRewards, position: Int) {
        val reward : Rewards = rewardList[position]

        Glide.with(context).load(reward.image).into(holder.rewardImage)

        holder.rewardName.text = reward.name

        holder.deleteBtn.setOnClickListener{
            deleteReward(reward, holder)
            val intent = (Intent(context, AdminRewardsActivity::class.java))
            context.startActivity(intent)
        }

        holder.editBtn.setOnClickListener {
            val intent = (Intent(context, EditRewardActivity::class.java))
            intent.putExtra("id", reward.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return rewardList.size

    }

    private fun deleteReward(model: Rewards, holder: HolderRewards) {
        val id = model.id
        val del = FirebaseFirestore.getInstance()
        del.collection(Constants.REWARD)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.e("reward", "Reward Deleted!")
            }
            .addOnFailureListener{ e->
                Log.e("reward", "Reward Fail to Delete!")
            }
    }

    public class HolderRewards(itemView : View) : RecyclerView.ViewHolder(itemView){

        val rewardImage : ImageView = itemView.findViewById(R.id.admin_rewards_image)
        val rewardName : TextView = itemView.findViewById(R.id.text_rewards_name)
        val deleteBtn : Button = itemView.findViewById(R.id.rewards_delete_btn)
        val editBtn : Button = itemView.findViewById(R.id.rewards_edit_btn)

    }


}