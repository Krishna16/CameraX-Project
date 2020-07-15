package com.example.cameraxproject

import android.content.Context
import android.net.Uri
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.collections.ArrayList

class GalleryImageAdapter(var imageMap: HashMap<String, Any>, var context: Context): RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

    private val TAG = "GalleryImageAdapter"

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image)

        init {
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_image_item, parent, false) as View

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageMap.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //var imageRef = storageRef.child("images/")

        //storageReference = imageCount[position]

        /*val TWO_MEGABYTES: Long = 2048 * 1024
        storageReference.getBytes(TWO_MEGABYTES).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            Glide.with(context)
                .load(it)
                .into(holder.imageView)
        }.addOnFailureListener {
            // Handle any errors
        }*/

        Log.d(TAG, "imageMap item: " + imageMap)

        var keys: MutableSet<String> = imageMap.keys

        Glide.with(context)
            .load(imageMap[keys.elementAtOrNull(position)])
            .into(holder.imageView)
    }
}