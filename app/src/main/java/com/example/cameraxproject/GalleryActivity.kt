package com.example.cameraxproject

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class GalleryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var galleryImageAdapter: GalleryImageAdapter
    private lateinit var firebaseStorage: FirebaseStorage
    private val TAG: String = "GalleryActivity"
    private lateinit var databaseReference: DatabaseReference
    private lateinit var imageMap: HashMap<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        firebaseStorage = FirebaseStorage.getInstance()
        val storage = FirebaseStorage.getInstance("gs://camerax-project.appspot.com")

        val listRef = storage.reference.child("images/")

        /*val task = MyAsyncTask(this)
        task.execute()*/

        databaseReference = Firebase.database.reference.child("images")
        imageMap = HashMap()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                imageMap = dataSnapshot.getValue<HashMap<String, Any>>()!!
                Log.d(TAG, "Value is: ${imageMap}")
                galleryImageAdapter = GalleryImageAdapter(imageMap, this@GalleryActivity)
                Log.d(TAG, "imageCount size: " + imageCount.size)
                linearLayoutManager = GridLayoutManager(this@GalleryActivity, 3, GridLayoutManager.VERTICAL, false)
                recyclerView = findViewById(R.id.recycler_view)
                recyclerView.apply {
                    adapter = galleryImageAdapter
                    layoutManager = linearLayoutManager
                    setHasFixedSize(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        /*listRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                    Log.d(TAG, "Gallery Activity prefix: $prefix.")
                }

                listResult.items.forEach { item ->
                    // All the items under listRef.
                    item.downloadUrl.addOnSuccessListener {
                        imageCount.add(it.toString())
                        Log.d(TAG, "Gallery Activity item: $it.")
                        Log.d(TAG, "imageCount size: ${imageCount.size}.")
                    }.addOnFailureListener{}
                }
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
            }*/

        //Thread.sleep(3000)

        //galleryImageAdapter = GalleryImageAdapter(storageRef, this, imageCount)

        Log.d(TAG, "imageMap size: " + imageMap.size)
        Log.d(TAG, "imageMap: " + imageMap)
        /*galleryImageAdapter = GalleryImageAdapter(imageMap, this)
        Log.d(TAG, "imageCount size: " + imageCount.size)
        linearLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.apply {
            adapter = galleryImageAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }*/
    }

    companion object {
        private var imageCount: ArrayList<String> = ArrayList<String>()
        private val TAG: String = "GalleryActivity"
        class MyAsyncTask internal constructor(context: GalleryActivity) :
            AsyncTask<Int, String, String?>() {

            private lateinit var databaseReference: DatabaseReference

            val storage = FirebaseStorage.getInstance("gs://camerax-project.appspot.com")
            val listRef = storage.reference.child("images/")

            override fun onPreExecute() {
                /*listRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                    Log.d(TAG, "Gallery Activity prefix: $prefix.")
                }

                listResult.items.forEach { item ->
                    // All the items under listRef.
                    item.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "Gallery Activity item: $it.")
                        imageCount.add(it.toString())
                        Log.d(TAG, "imageCount size: ${imageCount.size}.")
                    }.addOnFailureListener{}
                }
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
            }*/
            }

            override fun doInBackground(vararg params: Int?): String? {

                /*listRef.listAll()
                    .addOnSuccessListener { listResult ->
                        listResult.prefixes.forEach { prefix ->
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                            Log.d(TAG, "Gallery Activity prefix: $prefix.")
                        }

                        listResult.items.forEach { item ->
                            // All the items under listRef.
                            item.downloadUrl.addOnCompleteListener {
                                Log.d(TAG, "Gallery Activity item: $it.")
                                imageCount.add(it.result.toString())
                                Log.d(TAG, "imageCount size: ${imageCount.size}.")
                            }.addOnFailureListener{}
                        }
                    }
                    .addOnFailureListener {
                        // Uh-oh, an error occurred!
                    }*/

                /*databaseReference = Firebase.database.reference.child("images")
                imageMap = HashMap()*/

                /*databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        imageMap = dataSnapshot.getValue<HashMap<String, Any>>()!!
                        Log.d(TAG, "Value is: ${imageMap}")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException())
                    }
                })*/

                return ""
            }

            override fun onPostExecute(result: String?) {
            }

            override fun onProgressUpdate(vararg text: String?) {
            }
        }
    }
}