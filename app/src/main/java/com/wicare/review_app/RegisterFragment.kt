package com.wicare.review_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wicare.review_app.constants.BaseUrl
import com.wicare.review_app.databinding.FragmentRegisterBinding
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response

class RegisterFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    var userType = "Admin";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
        }

        //Handle registration
        binding.regButton.setOnClickListener {
            var firstname = binding.firstname.text.toString()
            var lastname = binding.lastname.text.toString()
            var email = binding.email.text.toString()
            var password = binding.password.text.toString()
            var rpassword = binding.rpassword.text.toString()

            val requestQueue = Volley.newRequestQueue(activity)
            val URL = BaseUrl.baseUrl
            val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener { response ->
                    Log.i("VOLLEY_res", "my responce>>$response")
                    try {
                        val res = JSONObject(response)
                        Log.i("Api response", res.toString())
                        val message = res.getString("message")
                        if (message == "successfully registered") {
                            Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT)
                                .show()
                            binding.firstname.text.clear()
                            binding.lastname.text.clear()
                            binding.email.text.clear()
                            binding.password.text.clear()
                            binding.rpassword.text.clear()
                            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
                        } else {
                            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                        Log.i("VOLLEY_res1", "eerr2>>$e")

                    }
                }, Response.ErrorListener { error ->
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Log.i("VOLLEY_res2", "eerr2>>$error")
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["registered"] = if(userType=="User") "registerUser" else "registerAdmin"
                    params["first_name"] = firstname
                    params["last_name"] = lastname
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }

        val userType: Spinner = binding.userType;
        userType.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.user_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            userType.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent != null) {
            Log.i("UserType", parent.getItemAtPosition(pos).toString())
            userType = parent.getItemAtPosition(pos).toString();
        };
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.i("NoUserSelected", parent?.getItemAtPosition(0).toString());
    }

}