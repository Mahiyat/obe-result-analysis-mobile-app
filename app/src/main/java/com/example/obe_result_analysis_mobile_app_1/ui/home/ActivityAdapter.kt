package com.example.obe_result_analysis_mobile_app_1.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obe_result_analysis_mobile_app_1.R
import com.example.obe_result_analysis_mobile_app_1.network.PendingActivity

class ActivityAdapter(private var activities: MutableList<PendingActivity>) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    init {
        Log.d("ActivityAdapter", "Adapter initialized with ${activities.size} items")
    }
    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val examTitle: TextView = view.findViewById(R.id.tvexamtitle)
        val title: TextView = view.findViewById(R.id.tvtitle)
        val details: TextView = view.findViewById(R.id.tvCC)
        val courseName: TextView = view.findViewById(R.id.tvCName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        Log.d("ActivityAdapter", "You are in ActivityAdapter")
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        Log.d("ActivityAdapter", "Passed Activity: $activity")
        holder.examTitle.text = activity.exam_title
        holder.title.text = activity.text
        holder.details.text = activity.course_id
        holder.courseName.text = activity.course_name
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateActivities(newActivities: List<PendingActivity>) {
        activities.clear()
        activities.addAll(newActivities)
        Log.d("ActivityAdapter", "New Activities received: $newActivities")
        Log.d("ActivityAdapter", "Activities updated with ${activities.size} new items")
        notifyDataSetChanged()
    }


    override fun getItemCount() = activities.size
}
