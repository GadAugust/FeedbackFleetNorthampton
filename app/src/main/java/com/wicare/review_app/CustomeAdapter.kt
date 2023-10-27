package com.wicare.review_app

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wicare.review_app.constants.BaseUrl
import com.wicare.review_app.models.ReviewData
import org.json.JSONException
import org.json.JSONObject

class CustomeAdapter(private val mList: List<ReviewData>, private val optionsMenuClickListener: OptionsMenuClickListener)  : RecyclerView.Adapter<CustomeAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review, parent, false)

        return ViewHolder(view)
    }

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ReviewData = mList[position]

        // sets the image to the imageview from our itemHolder class
        // holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.nameView.text = ReviewData.fullname
        holder.emailView.text = ReviewData.email
        holder.reviewView.text = ReviewData.review
        holder.imageView.setImageResource(ReviewData.image)
        holder.likesView.text = ReviewData.likes.toString()
        holder.dislikesView.text = ReviewData.dislikes.toString()
        holder.optionsView.setOnClickListener {
            optionsMenuClickListener.onOptionsMenuClicked(position)
        }
        holder.likeView.setOnClickListener {
            Log.i("Like button clicked", "Clicked")
            var l: String =  holder.likesView.text.toString()
            var lInt: Int = l.toInt() + 1
            holder.likesView.text = lInt.toString()

            val requestQueue = Volley.newRequestQueue(holder.imageView.context)
            val URL = BaseUrl.baseUrl
            val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener { response ->
                    Log.i("VOLLEY_res", "my responce>>$response")
                    try {
                        val res = JSONObject(response)
                        Log.i("Api response", res.toString())
                        val message = res.getString("message")
                        if (message == "Review liked") {
                            Toast.makeText(holder.imageView.context, "Liked", Toast.LENGTH_SHORT)
                                .show()
                            } else {
                            Toast.makeText(holder.imageView.context, "Failed", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(holder.imageView.context, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                        Log.i("VOLLEY_res1", "eerr2>>$e")

                    }
                }, Response.ErrorListener { error ->
                    Toast.makeText(holder.imageView.context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Log.i("VOLLEY_res2", "eerr2>>$error")
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["likedReview"] = "liked"
                    params["id"] = ReviewData.id
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }

        holder.dislikeView.setOnClickListener {
            var k: String =  holder.dislikesView.text.toString()
            var kInt: Int = k.toInt() + 1
            holder.dislikesView.text = kInt.toString()

            val requestQueue = Volley.newRequestQueue(holder.imageView.context)
            val URL = BaseUrl.baseUrl
            val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener { response ->
                    Log.i("VOLLEY_res", "my responce>>$response")
                    try {
                        val res = JSONObject(response)
                        Log.i("Api response", res.toString())
                        val message = res.getString("message")
                        if (message == "Review disliked") {
                            Toast.makeText(holder.imageView.context, "Liked", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(holder.imageView.context, "Failed", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(holder.imageView.context, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                        Log.i("VOLLEY_res1", "eerr2>>$e")

                    }
                }, Response.ErrorListener { error ->
                    Toast.makeText(holder.imageView.context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Log.i("VOLLEY_res2", "eerr2>>$error")
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["dislikedReview"] = "disliked"
                    params["id"] = ReviewData.id
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameView: TextView = itemView.findViewById(R.id.fullname)
        val emailView: TextView = itemView.findViewById(R.id.email)
        val reviewView: TextView = itemView.findViewById(R.id.review)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val likeView: TextView = itemView.findViewById(R.id.likeButton)
        val dislikeView: TextView = itemView.findViewById(R.id.dislikeButton)
        val likesView: TextView = itemView.findViewById(R.id.likes)
        val dislikesView: TextView = itemView.findViewById(R.id.dislikes)
        val optionsView: ImageButton = itemView.findViewById(R.id.reviewListview)
    }
}