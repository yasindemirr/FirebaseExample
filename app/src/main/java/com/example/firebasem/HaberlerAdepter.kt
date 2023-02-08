package com.example.firebasem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class HaberlerAdepter(val postList: ArrayList<Post>): RecyclerView.Adapter<HaberlerAdepter.HaberlerHolder>() {
    class HaberlerHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HaberlerHolder {
        val gorunum =LayoutInflater.from(parent.context).inflate(R.layout.rec_row,parent,false)
       return HaberlerHolder(gorunum)

    }

    override fun onBindViewHolder(holder: HaberlerHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.kullaniciMailTextId).text= postList[position].kullaniciEmaili
        holder.itemView.findViewById<TextView>(R.id.yuklenenYorumId).text=postList[position].kullaniciYorumu
        Picasso.get().load(postList[position].kullaniciUri).into(holder.itemView.findViewById<ImageView>(R.id.yuklenenResimId))

    }

    override fun getItemCount(): Int {
        return postList.size


    }
}