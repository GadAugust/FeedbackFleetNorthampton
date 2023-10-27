package com.wicare.review_app

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wicare.review_app.constants.BaseUrl
import com.wicare.review_app.constants.Constants
import com.wicare.review_app.databinding.FragmentUpdateReviewBinding
import org.json.JSONException
import org.json.JSONObject

class UpdateReviewFragment : Fragment() {
    private var _binding: FragmentUpdateReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentUpdateReviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newReview.setText(Constants.review.toString())
        binding.updateButton.setOnClickListener {
            val newReview = binding.newReview.text.toString()
            val id = Constants.id

            val requestQueue = Volley.newRequestQueue(activity)
            val URL = BaseUrl.baseUrl
            val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener { response ->
                    Log.i("VOLLEY_res", "my responce>>$response")
                    try {
                        val res = JSONObject(response)
                        Log.i("Api response", res.toString())
                        val message = res.getString("message")
                        if (message == "Review Update") {
                            Toast.makeText(activity, "Review update Successful", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_UpdateReviewFragment_to_ReviewFragment)
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
                    params["updatenewReview"] = "updateNewReview"
                    params["newReview"] = newReview
                    params["id"] = id
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }
    }
}