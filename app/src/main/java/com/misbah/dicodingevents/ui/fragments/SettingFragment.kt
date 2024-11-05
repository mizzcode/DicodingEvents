package com.misbah.dicodingevents.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.misbah.dicodingevents.R
import com.misbah.dicodingevents.databinding.FragmentSettingBinding
import com.misbah.dicodingevents.ui.MainViewModel
import com.misbah.dicodingevents.workmanager.DailyReminderWorker
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by inject()

    private lateinit var workManager: WorkManager
    private lateinit var dailyReminderWorkRequest: PeriodicWorkRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workManager = WorkManager.getInstance(requireContext())

        binding.textSetting.text = getString(R.string.setting)
        binding.textDarkMode.text = getString(R.string.dark_mode)
        binding.textDarkModeDesc.text = getString(R.string.dark_mode_desc)
        binding.textDailyReminder.text = getString(R.string.daily_reminder)
        binding.textDailyReminderDesc.text = getString(R.string.daily_reminder_desc)

        mainViewModel.getTheme().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            binding.switchTheme.isChecked = isDarkModeActive
        }

        mainViewModel.getReminder().observe(viewLifecycleOwner) { isDailyReminderActive: Boolean ->
            binding.switchNotification.isChecked = isDailyReminderActive
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveTheme(isChecked)
        }

        binding.switchNotification.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                startDailyReminder()
                mainViewModel.saveDailyReminder(true)
            } else {
                cancelDailyReminder()
                mainViewModel.saveDailyReminder(false)
            }
        }
    }

    private fun startDailyReminder() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        dailyReminderWorkRequest = PeriodicWorkRequest.Builder(DailyReminderWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyReminderWorkRequest
        )
    }

    private fun cancelDailyReminder() {
        workManager.cancelUniqueWork("daily_reminder")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}