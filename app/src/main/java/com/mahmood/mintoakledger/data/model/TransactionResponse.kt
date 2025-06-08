package com.mahmood.mintoakledger.data.model

import com.squareup.moshi.JsonClass

/**
 * Data class representing the response from the API containing a list of transactions.
 */
@JsonClass(generateAdapter = true)
data class TransactionResponse(
    val sort: List<Transaction>
)