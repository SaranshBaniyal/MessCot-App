package com.example.practicumui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicumui.databinding.FragmentHomeBinding // for viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding //for viewBinding
    private lateinit var databaseUser: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomeBinding.inflate(layoutInflater) //fragment viewBinding

        binding.btnDineNow.setOnClickListener {
            val intent = Intent(getActivity(),ScannerActivity::class.java)
            startActivity(intent)
        }
        binding.profileIcon.setOnClickListener{
            FirebaseAuth.getInstance().signOut();
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
        }
        val user = Firebase.auth.currentUser

        databaseUser = FirebaseDatabase.getInstance().getReference("User")
        databaseUser.child(user!!.uid.toString()).child("personalData").child("name").get().addOnSuccessListener {
            binding.tvHello.text = binding.tvHello.text.toString() + "\n" + it.value.toString()
        }

        return binding.root // part of fragment viewBinding
    }
}