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
import com.example.checkid.model.NotificationRepository.notifications
import com.example.checkid.model.NotificationType
import com.example.checkid.viewmodel.NotificationViewModel

class NotificationFragment() : Fragment(R.layout.fragment_notification) {
    private var _binding : FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        val adapter = NotificationAdapter(notifications)

        binding.recNotify.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class NotificationAdapter(private val notifications: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ListNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {

            when (notification.notificationType) {
                NotificationType.REPORT -> binding.iconNotificationType.setImageResource(R.drawable.ic_notification_report)
                NotificationType.WARNING -> binding.iconNotificationType.setImageResource(R.drawable.ic_notification_warning)
                else -> binding.iconNotificationType.setImageResource(R.drawable.ic_notification_system)
            }

            binding.txtNotificationTitle.text = notification.textTitle
            binding.txtNotificationContent.text = notification.textContent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        binding.listNotificationDeleteButton.setOnClickListener {
            // model 관련 로직
            // NotificationViewModel.deleteNotificationInstance(0)

            // view 갱신 관련 로직

        }

        binding.listNotificationSpeechBubble.setOnClickListener {
            // model 관련 로직

            // view 갱신 관련 로직
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount() = notifications.size
}