package com.wicare.review_app

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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wicare.review_app.constants.BaseUrl
import com.wicare.review_app.constants.Constants
import com.wicare.review_app.databinding.FragmentLoginBinding
import org.json.JSONException
import org.json.JSONObject

class LoginFragment : Fragment(), AdapterView.OnItemSelectedListener {
    //Fragment binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    var userType = "Admin";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //navigate to registration page
        binding.regLink.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }

        //Process login
        binding.loginButton.setOnClickListener {
            var email = binding.email.text.toString()
            var password = binding.password.text.toString()

            val requestQueue = Volley.newRequestQueue(activity)
            val URL = BaseUrl.baseUrl
            val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener { response ->
                    Log.i("VOLLEY_res", "my responce>>$response")
                    try {
                        val res = JSONObject(response)
                        Log.i("Api response", res.toString())
                        val status = res.getString("status")
                        val data = res.getJSONObject("data")
                        if (status == "success") {
                            Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT)
                                .show()
                            binding.email.text.clear()
                            binding.password.text.clear()

                            Constants.firstname = data.getString("first_name")
                            Constants.lastname = data.getString("last_name")
                            Constants.email = data.getString("email")
                            findNavController().navigate(R.id.action_LoginFragment_to_ReviewFragment)
                        } else {
                            Toast.makeText(activity, "Invalid credentials", Toast.LENGTH_SHORT).show()
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
                    params["logIn"] = if(userType=="User") "logUser" else "logAdmin"
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }

        //Adapt spinner data
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

    //Handle spinner option selection
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent != null) {
            Log.i("UserType", parent.getItemAtPosition(pos).toString())
            userType = parent.getItemAtPosition(pos).toString();
        };
    }

    //When nothing is selected from spinner
    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.i("NoUserSelected", parent?.getItemAtPosition(0).toString());
    }
}