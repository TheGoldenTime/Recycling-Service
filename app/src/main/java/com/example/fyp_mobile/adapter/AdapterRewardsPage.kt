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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fyp_mobile.R
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.ui.activities.RewardsDetailActivity

class AdapterRewardsPage(private val rewardList : ArrayList<Rewards>, private val context: Context) :
    RecyclerView.Adapter<AdapterRewardsPage.HolderRewards>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRewardsPage.HolderRewards {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_design,parent, false)

        return HolderRewards(itemView)
    }

    override fun onBindViewHolder(holder: AdapterRewardsPage.HolderRewards, position: Int) {
        val reward : Rewards = rewardList[position]

        Glide.with(context).load(reward.image).into(holder.rewardImage)

        holder.txtName.text = reward.name

        holder.txtPoint.text = reward.points.toString() + " points"

        holder.rewardImage.setOnClickListener {
            val intent = (Intent(context, RewardsDetailActivity::class.java))
            intent.putExtra("id", reward.id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {

        return rewardList.size

    }

    public class HolderRewards(itemView : View) : RecyclerView.ViewHolder(itemView){

        val rewardImage: ImageView = itemView.findViewById(R.id.card_reward_imageview)
        val txtName: TextView = itemView.findViewById(R.id.reward_text_name)
        val txtPoint: TextView = itemView.findViewById(R.id.reward_text_point)

    }
}