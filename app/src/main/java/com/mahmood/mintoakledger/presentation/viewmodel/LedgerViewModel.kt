package com.mahmood.mintoakledger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmood.mintoakledger.domain.model.Mid
import com.mahmood.mintoakledger.domain.usecase.GetHierarchicalTransactionsUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel for handling the three-level hierarchical ledger data and UI logic.
 */
class LedgerViewModel(private val getHierarchicalTransactionsUseCase: GetHierarchicalTransactionsUseCase
) : ViewModel() {
    
    private val _midItems = MutableLiveData<List<Mid>>()
    val midItems: LiveData<List<Mid>> = _midItems
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    

    fun loadLedgerData() {
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            try {
                val hierarchicalData = getHierarchicalTransactionsUseCase()
                _midItems.value = hierarchicalData
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
                _isLoading.value = false
            }
        }
    }
    

    fun toggleMidExpansion(position: Int) {
        val currentMids = _midItems.value?.toMutableList() ?: return
        if (position < 0 || position >= currentMids.size) return
        
        currentMids[position] = currentMids[position].copy(
            isExpanded = !currentMids[position].isExpanded
        )
        
        _midItems.value = currentMids
    }
    

    fun toggleTidExpansion(midPosition: Int, tidPosition: Int) {
        val currentMids = _midItems.value?.toMutableList() ?: return
        if (midPosition < 0 || midPosition >= currentMids.size) return
        
        val mid = currentMids[midPosition]
        val tidItems = mid.tidItems.toMutableList()
        
        if (tidPosition < 0 || tidPosition >= tidItems.size) return
        
        tidItems[tidPosition] = tidItems[tidPosition].copy(
            isExpanded = !tidItems[tidPosition].isExpanded
        )
        
        currentMids[midPosition] = mid.copy(tidItems = tidItems)
        _midItems.value = currentMids
    }
}