package com.wicare.review_app

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wicare.review_app.constants.BaseUrl
import com.wicare.review_app.constants.Constants
import com.wicare.review_app.databinding.FragmentReviewBinding
import com.wicare.review_app.models.ReviewData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    val data = ArrayList<ReviewData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {


            findNavController().navigate(R.id.action_ReviewFragment_to_CreateReviewFragment)
        }



        fetchAllReviews()
    }

    fun fetchAllReviews(){
        val requestQueue = Volley.newRequestQueue(activity)
        val URL = BaseUrl.baseUrl
        val stringRequest = object : StringRequest(Request.Method.POST, URL,
            Response.Listener { response ->
                Log.i("VOLLEY_res", "my responce>>$response")
                try {
                    val res = JSONArray(response)
                    Log.i("Api response", res.toString())

                    var recyclerview = activity?.findViewById<RecyclerView>(R.id.recyclerview)
                    recyclerview?.layoutManager = LinearLayoutManager(activity)
                    data.clear();
                    for (i in 0 until res.length()){
                        val review = res.getJSONObject(i);
                        data.add(ReviewData(review.getString("id"),review.getString("first_name") + " " + review.getString("last_name"), review.getString("email"), review.getString("user_review"), R.drawable.profileicon, review.getString("likes"), review.getString("dislike")))
                    }
                    val adapter = CustomeAdapter(data, object: CustomeAdapter.OptionsMenuClickListener{
                        override fun onOptionsMenuClicked(position: Int) {
                            performOptionsMenuClick(position)
                        }
                    })
                    recyclerview?.adapter = adapter

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
                params["getReviews"] = "allReviews"
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    // this method will handle the onclick options click
    private fun performOptionsMenuClick(position: Int) {
        // create object of PopupMenu and pass context and view where we want
        // to show the popup menu
        val popupMenu = PopupMenu(activity!!, binding.recyclerview.findViewById(R.id.reviewListview))
        // add the menu
        popupMenu.inflate(R.menu.menu_options)
        // implement on menu item click Listener
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.action_update -> {
                        // here are the logic to delete an item from the list
                        val selectedData = data[position]
                        Constants.id = selectedData.id
                        Constants.review = selectedData.review
                        findNavController().navigate(R.id.action_ReviewFragment_to_UpdateReviewFragment)
                        return true
                    }
                    // in the same way you can implement others
                    R.id.action_delete -> {
                        val dataToUpdate = data[position]
                        Log.i("Data to update", dataToUpdate.id)
                        val requestQueue = Volley.newRequestQueue(activity)
                        val URL = BaseUrl.baseUrl
                        val stringRequest = object : StringRequest(Request.Method.POST, URL,
                            Response.Listener { response ->
                                Log.i("VOLLEY_res", "my responce>>$response")
                                try {
                                    val res = JSONObject(response)
                                    Log.i("Api response", res.toString())
                                    val message = res.getString("message")
                                    if (message == "Review Deleted") {
                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT)
                                            .show()
                                        fetchAllReviews()
                                    } else {
                                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                    }

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.i("VOLLEY_res1", "eerr2>>$e")

                                }
                            }, Response.ErrorListener { error ->
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                                Log.i("VOLLEY_res2", "eerr2>>$error")
                            }) {
                            override fun getParams(): Map<String, String> {
                                val params = HashMap<String, String>()
                                params["delReview"] = "delReview"
                                params["id"] = dataToUpdate.id
                                return params
                            }
                        }
                        requestQueue.add(stringRequest)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.action_refresh){
            fetchAllReviews()
        }
        return true;
    }
}