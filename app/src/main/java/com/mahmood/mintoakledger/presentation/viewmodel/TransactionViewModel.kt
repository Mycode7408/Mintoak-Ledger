package com.mahmood.mintoakledger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmood.mintoakledger.domain.model.TransactionGroup
import com.mahmood.mintoakledger.domain.usecase.GetGroupedTransactionsUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel for handling transaction data and UI logic.
 */
class TransactionViewModel(private val getGroupedTransactionsUseCase: GetGroupedTransactionsUseCase) : ViewModel() {
    
    private val _transactionGroups = MutableLiveData<List<TransactionGroup>>()
    val transactionGroups: LiveData<List<TransactionGroup>> = _transactionGroups
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    /**
     * Loads transaction groups from the repository.
     */
    fun loadTransactionGroups() {
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            try {
                val groups = getGroupedTransactionsUseCase()
                _transactionGroups.value = groups
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Toggles the expanded state of a transaction group.
     * @param position The position of the group in the list.
     */
    fun toggleGroupExpansion(position: Int) {
        val currentGroups = _transactionGroups.value?.toMutableList() ?: return
        if (position < 0 || position >= currentGroups.size) return
        
        currentGroups[position] = currentGroups[position].copy(
            isExpanded = !currentGroups[position].isExpanded
        )
        
        _transactionGroups.value = currentGroups
    }
}