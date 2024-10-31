package com.example.checkid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkid.R
import com.example.checkid.databinding.FragmentNotificationBinding
import com.example.checkid.databinding.ListNotificationBinding
import com.example.checkid.model.Notification
import com.example.checkid.model.NotificationRepository
import com.example.checkid.model.NotificationType

class NotificationFragment() : Fragment() {
    lateinit var binding : FragmentNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        binding.recNotify.layoutManager = LinearLayoutManager(context)
        binding.recNotify.adapter = NotificationAdapter(NotificationRepository.notifications)

        return binding.root
    }
}

class NotificationAdapter(val notifications: ArrayList<Notification>) : RecyclerView.Adapter<NotificationAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListNotificationBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun getItemCount() = notifications.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(NotificationRepository.notifications[position])
    }

    class Holder(private val binding: ListNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.imageNotification.setImageResource(when (notification.notificationType) {
                // NotificationType.SYSTEM ->
                // NotificationType.REPORT ->
                // NotificationType.WARNING ->
                else -> android.R.drawable.ic_lock_idle_alarm
            })

            binding.txtNotificationTitle.text = notification.textTitle
            binding.txtNotificationContent.text = notification.textContent
        }
    }
}