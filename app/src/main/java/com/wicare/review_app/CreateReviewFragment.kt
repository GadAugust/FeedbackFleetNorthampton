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
import com.wicare.review_app.databinding.FragmentCreateReviewBinding
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateReviewFragment : Fragment() {

    private var _binding: FragmentCreateReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateReviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createButton.setOnClickListener {
            var review = binding.review.text.toString()
            var firstname = Constants.firstname
            var lastname = Constants.lastname
            var email = Constants.email

            val requestQueue = Volley.newRequestQueue(activity)
            val URL = BaseUrl.baseUrl
            val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener { response ->
                    Log.i("VOLLEY_res", "my responce>>$response")
                    try {
                        val res = JSONObject(response)
                        Log.i("Api response", res.toString())
                        val message = res.getString("message")
                        if (message == "Review sent") {
                            Toast.makeText(activity, "Review sent Successful", Toast.LENGTH_SHORT)
                                .show()
                            binding.review.text.clear()
                            findNavController().navigate(R.id.action_CreateReviewFragment_to_ReviewFragment)
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
                    params["reviewSend"] = "sendReview"
                    params["first_name"] = firstname
                    params["last_name"] = lastname
                    params["email"] = email
                    params["user_review"] = review
                    return params
                }
            }
            requestQueue.add(stringRequest)

//            findNavController().navigate(R.id.action_CreateReviewFragment_to_ReviewFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}