package com.example.mobilecloudcomputing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mobilecloudcomputing.adapters.UserAdapter
import com.example.mobilecloudcomputing.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

    lateinit var db: FirebaseFirestore
    lateinit var adapter: UserAdapter
    lateinit var data: ArrayList<User>

    private val TAG = "abd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize the database, adapter, and data
        db = FirebaseFirestore.getInstance()
        data = ArrayList()
        adapter = UserAdapter(this, data, this)

        // give the adapter to recycler view adapter
        rv_users.adapter = adapter

        // call "fetchUsers" function
        fetchUsers()

        // "for swipe to refresh" refresh the data when user swipe down
        mSwipeRefreshLayout.setOnRefreshListener {
            fetchUsers()
        }

        // when user click on "add" floating action button go to "AddUser" activity
        fab.setOnClickListener {
            startActivity(Intent(this, AddUser::class.java))
        }
    }

    // when user click on "delete" icon delete the user
    override fun onDeleteClick(position: Int) {
        // call "deleteUser" function
        deleteUser(data[position].id)
    }

    // this function to get all users from database
    private fun fetchUsers() {
        db.collection("users")
            .addSnapshotListener { querySnapshot, exception ->
                if (exception == null)
                {
                    // clear the "data" array before fetching the users
                    data.clear()
                    querySnapshot!!.documents.forEach { userInfo ->
                        // get user data from database
                        val id = userInfo.id
                        val name = userInfo.data!!["name"].toString()
                        val number = userInfo.data!!["number"].toString()
                        val address = userInfo.data!!["address"].toString()

                        // store the user data in a variable of type "User"
                        val user = User(id, name, number, address)

                        // add the user info to "data" array list
                        data.add(0, user)
                    }
                    // notify the adapter
                    adapter.notifyDataSetChanged()

                    // if the array is empty show "txtNoUsers" text view
                    if (data.isEmpty())
                    {
                        txtNoUsers.visibility = View.VISIBLE // show the txtNoUsers
                        progressBar.visibility = View.GONE   // hide the progressBar
                    } else {
                        mSwipeRefreshLayout.isRefreshing = false // stop refreshing
                        progressBar.visibility = View.GONE // hide the progressBar
                        txtNoUsers.visibility = View.GONE  // hide the txtNoUsers
                    }
                } else {
                    Toast.makeText(this, "An error occurs", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // this function to delete user by id from database
    private fun deleteUser(id: String) {
        db.collection("users").document(id)
            .delete()
            .addOnSuccessListener {
                // show a message for the user
                Toast.makeText(this, "User Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, exception.message!!)
            }
    }

}