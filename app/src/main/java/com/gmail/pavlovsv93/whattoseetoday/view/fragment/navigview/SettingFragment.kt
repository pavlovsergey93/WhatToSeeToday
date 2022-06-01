package com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview

import android.content.Context
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentSettingBinding
import com.gmail.pavlovsv93.whattoseetoday.BasSuggestionProvider
import com.gmail.pavlovsv93.whattoseetoday.utils.Const
import com.gmail.pavlovsv93.whattoseetoday.utils.pullCheckSetting

class SettingFragment : Fragment() {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingSwitch.isChecked = activity?.pullCheckSetting(this.activity)!!

        binding.settingSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                Log.d(TAG_SHEET, "isChecked ${buttonView.text.toString()} ON")
//            } else {
//                Log.d(TAG_SHEET, "isChecked ${buttonView.text.toString()} OFF")
//            }
            isCheckedSwitch(isChecked)
        }
        binding.settingBtnCleanHistoryFind.setOnClickListener {
            SearchRecentSuggestions(context, BasSuggestionProvider.AUTHORITY, BasSuggestionProvider.MODE)
                .clearHistory()
        }
    }

    private fun isCheckedSwitch(isCheckSwitch: Boolean) {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putBoolean(Const.KEY_ADULT, isCheckSwitch)
        editor?.apply()
    }
}

