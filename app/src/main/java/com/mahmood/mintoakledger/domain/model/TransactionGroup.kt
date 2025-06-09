package com.mahmood.mintoakledger.domain.model

import com.mahmood.mintoakledger.data.model.Transaction

/**
 * Domain model class representing a group of transactions with the same Mid and Tid.
 */
data class TransactionGroup(
    val mid: Int,
    val tid: Long,
    val transactions: List<Transaction>,
    var isExpanded: Boolean = false
)