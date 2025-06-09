package com.mahmood.mintoakledger.domain.model

import com.mahmood.mintoakledger.data.model.Transaction

/**
 * Data class representing a Tid item, which is the second level of the hierarchy.
 * Contains a list of Transaction items.
 */
data class Tid(
    val tid: String,
    val transactions: List<Transaction>,
    var isExpanded: Boolean = false
)