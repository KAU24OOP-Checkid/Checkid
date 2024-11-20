package com.example.checkid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

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

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val adapter = NotificationAdapter(notifications,
            onDeleteClick = { position ->
                viewModel.deleteNotificationInstance(position)
            }
        )

        binding.recNotify.layoutManager = LinearLayoutManager(requireContext())
        binding.recNotify.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.notifications.observe(viewLifecycleOwner) { updateNotifications ->
            (binding.recNotify.adapter as NotificationAdapter).updateData(updateNotifications)
        }
    }
}

class NotificationAdapter(
    private var notifications: List<Notification>,
    private val onDeleteClick: (Int) -> Unit
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ListNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            notification: Notification,
            position: Int,
            onDeleteClick: (Int) -> Unit
        ) {

            when (notification.notificationType) {
                NotificationType.REPORT -> binding.iconNotificationType.setImageResource(R.drawable.ic_notification_report)
                NotificationType.WARNING -> binding.iconNotificationType.setImageResource(R.drawable.ic_notification_warning)
                else -> binding.iconNotificationType.setImageResource(R.drawable.ic_notification_system)
            }

            binding.txtNotificationTitle.text = notification.textTitle
            binding.txtNotificationContent.text = notification.textContent

            binding.listNotificationDeleteButton.setOnClickListener {
                onDeleteClick(position)
            }

            // binding.listNotificationSpeechBubble.setOnClickListener {
            //
            // }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notifications[position], position, onDeleteClick)
    }

    override fun getItemCount() = notifications.size

    fun updateData(newNotifications: List<Notification>) {
        notifications = newNotifications
        notifyDataSetChanged() // RecyclerView를 갱신하여 UI를 업데이트
    }
}