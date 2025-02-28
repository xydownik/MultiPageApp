package com.example.multipagelab.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.multipagelab.databinding.ItemCalendarEventBinding
import com.example.multipagelab.models.CalendarEvent
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(private val events: List<CalendarEvent>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(private val binding: ItemCalendarEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: CalendarEvent) {
            val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            binding.tvEventTitle.text = event.title
            binding.tvEventDate.text = dateFormat.format(Date(event.startTime))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size
}
