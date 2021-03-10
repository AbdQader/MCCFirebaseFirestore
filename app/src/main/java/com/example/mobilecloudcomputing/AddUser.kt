package com.example.mobilecloudcomputing

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_user.*

class AddUser : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var progressDialog: ProgressDialog

    private val TAG = "abd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        // initialize the firestore database
        db = FirebaseFirestore.getInstance()

        // initialize and setup the progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait ...")
        progressDialog.setCancelable(false)

        // for back arrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        // when user click on add button call "addUser" function
        btnAddUser.setOnClickListener {
            // show progressDialog
            progressDialog.show()
            // call "addUser" function
            addUser()
        }
    }

    // when click on back arrow finish the current activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    // this function add a user to the database
    private fun addUser() {
        // get user data from text fields
        val name = txtName.text.trim().toString()
        val number = txtNumber.text.trim().toString()
        val address = txtAddress.text.trim().toString()

        // check data validation "is not empty"
        if (name.isNotEmpty() && number.isNotEmpty() && address.isNotEmpty())
        {
            // store the variables in a map
            val data = mapOf("name" to name, "number" to number, "address" to address)

            // Add a new document with a generated ID "users"
            db.collection("users")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    // dismiss the progressDialog
                    progressDialog.dismiss()

                    // show a message for the user
                    Toast.makeText(this, "User Added Successfully", Toast.LENGTH_SHORT).show()

                    // to clear edit text after adding the data
                    txtName.text.clear()
                    txtNumber.text.clear()
                    txtAddress.text.clear()

                    // finish this activity
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, exception.message!!)
                }
        } else {
            // dismiss the progressDialog
            progressDialog.dismiss()
            // show a message for the user
            Toast.makeText(this, "Fill Fields First", Toast.LENGTH_SHORT).show()
        }
    }

}