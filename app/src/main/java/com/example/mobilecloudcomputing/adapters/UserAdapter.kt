package com.example.mobilecloudcomputing.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecloudcomputing.R
import com.example.mobilecloudcomputing.models.User
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(
        private var context: Context,
        private var data: ArrayList<User>,
        private var click: OnItemClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.tvName!!
        var number = view.tvNumber!!
        var address = view.tvAddress!!
        var image = view.imgDelete!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
                LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.number.text = data[position].number
        holder.address.text = data[position].address

        holder.image.setOnClickListener {
           click.onDeleteClick(position)
        }
    }

    interface OnItemClickListener {
       fun onDeleteClick(position: Int)
    }

}