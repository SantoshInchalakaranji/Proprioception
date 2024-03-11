package com.prplmnstr.proprioception.utils

import android.content.Context
import android.content.res.Configuration
import java.text.DateFormatSymbols
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Helper {
    companion object {



        fun getDateInStringFormat(day: Int, month: Int, year: Int): String {
            val monthString = DateFormatSymbols().months[month - 1].substring(0, 3)
            return String.format("%d %s %d", day, monthString, year)
        }

        fun getFormattedDate(): String {
            val dateFormat = SimpleDateFormat("MMMM dd, yyyy - hh:mma", Locale.getDefault())
            return dateFormat.format(Date())
        }


         fun isLightTheme(context: Context): Boolean {
            val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode == Configuration.UI_MODE_NIGHT_NO
        }

    }
}