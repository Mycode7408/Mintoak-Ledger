package com.mahmood.mintoakledger.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing a transaction with Mid, Tid, amount, and narration.
 */
@JsonClass(generateAdapter = true)
data class Transaction(
    @Json(name = "Mid") val mid: Int,
    @Json(name = "Tid") val tid: Long,
    @Json(name = "amount") val amount: Double,
    @Json(name = "narration") val narration: Long
)