package com.example.fyp_mobile.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fyp_mobile.R
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Cart
import com.example.fyp_mobile.ui.activities.CartActivity
import com.example.fyp_mobile.ui.activities.ParticipantActivity
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class AdapterCart(private val cartList : ArrayList<Cart>, private val context: Context) :
    RecyclerView.Adapter<AdapterCart.HolderRewards>() {

    private lateinit var currentUserID : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCart.HolderRewards {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_cart, parent, false)

        return HolderRewards(itemView)

    }

    override fun onBindViewHolder(holder: AdapterCart.HolderRewards, position: Int) {
        val cart: Cart = cartList[position]

        currentUserID = FirestoreClass().getCurrentUserID()

        Glide.with(context).load(cart.image).into(holder.cartImage)

        holder.txtName.text = cart.name

        holder.txtStatus.text = cart.status

        holder.txtPoint.text = cart.points.toString() + " points"

        holder.txtView.setOnClickListener {
            updateStatus(cart, holder)
            val intent = (Intent(context, CartActivity::class.java))
            context.startActivity(intent)
            (context as CartActivity).finish()
        }

        holder.txtDelete.setOnClickListener {
            deleteCart(cart, holder)
            val intent = (Intent(context, CartActivity::class.java))
            context.startActivity(intent)
            (context as CartActivity).finish()
        }


    }

    override fun getItemCount(): Int {

        return cartList.size

    }

    private fun updateStatus(cart: Cart, holder: AdapterCart.HolderRewards) {
        val id = cart.id
        val update = FirebaseFirestore.getInstance()
        update.collection(Constants.USERS)
            .document(currentUserID)
            .collection(Constants.CART)
            .document(id)
            .update(mapOf(
                "status" to "Delivered"
            ))
            .addOnSuccessListener {
                Log.e("reward", "Reward Deleted!")

            }
            .addOnFailureListener{ e->
                Log.e("reward", "Reward Fail to Delete!")
            }
    }

    private fun deleteCart(cart: Cart, holder: AdapterCart.HolderRewards) {
        val id = cart.id
        val del = FirebaseFirestore.getInstance()
        del.collection(Constants.USERS)
            .document(currentUserID)
            .collection(Constants.CART)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.e("reward", "Reward Deleted!")
            }
            .addOnFailureListener{ e->
                Log.e("reward", "Reward Fail to Delete!")
            }
    }

    public class HolderRewards(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cartImage: ImageView = itemView.findViewById(R.id.cartIV)
        val txtName: TextView = itemView.findViewById(R.id.cart_reward_text)
        val txtStatus: TextView = itemView.findViewById(R.id.cart_status_text)
        val txtPoint: TextView = itemView.findViewById(R.id.cart_point_text)
        val txtView: TextView = itemView.findViewById(R.id.cart_view_text)
        val txtDelete: TextView = itemView.findViewById(R.id.cart_delete_text)

    }

}
