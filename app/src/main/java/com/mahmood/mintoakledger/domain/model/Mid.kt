package com.mahmood.mintoakledger.domain.model

/**
 * Data class representing a Mid item, which is the top level of the hierarchy.
 */
data class Mid(
    val mid: Int,
    val tidItems: List<Tid>,
    var isExpanded: Boolean = false
)