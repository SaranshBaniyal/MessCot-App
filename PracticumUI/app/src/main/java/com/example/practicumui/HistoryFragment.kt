package com.example.practicumui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.practicumui.databinding.FragmentHistoryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding //for viewBinding
    private lateinit var databaseUser: DatabaseReference
    private lateinit var Date: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(layoutInflater) //fragment viewBinding

        val user = Firebase.auth.currentUser

        binding.iconSearch.setOnClickListener {
            Date = binding.etYear.text.toString() + "-" + binding.etMonth.text.toString() + "-" + binding.etDay.text.toString()
            databaseUser = FirebaseDatabase.getInstance().getReference("User")
        databaseUser.child(user!!.uid.toString()).child("records").child(Date).get().addOnSuccessListener {
            if(it.value!=null){
                binding.tvSearchOutput.setText("Attendance marked for: \n"+it.value.toString())
            }
            else{
                Toast.makeText(activity, "No records found for the given date", Toast.LENGTH_SHORT).show()
                binding.tvSearchOutput.setText("")
            }
            }
        }

        return binding.root // part of fragment viewBinding
    }
}