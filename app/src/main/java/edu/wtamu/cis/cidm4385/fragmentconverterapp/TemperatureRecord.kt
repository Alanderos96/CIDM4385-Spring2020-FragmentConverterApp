package edu.wtamu.cis.cidm4385.fragmentconverterapp

import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalDateTime

@IgnoreExtraProperties
data class TemperatureRecord (
    var convertedTemperature: String = "",
    var conversionType: String = "",
    var timestamp: LocalDateTime = LocalDateTime.now()
){
    override fun toString(): String {
        return "Date: $timestamp Temperature: $convertedTemperature Type: $conversionType"
    }
}