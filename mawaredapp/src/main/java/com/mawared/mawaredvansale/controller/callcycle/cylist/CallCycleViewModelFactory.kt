package com.mawared.mawaredvansale.controller.callcycle.cylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mawared.mawaredvansale.services.repositories.callcycle.ICallCycleRepository
import com.mawared.mawaredvansale.services.repositories.masterdata.IMDataRepository

@Suppress("UNCHECKED_CAST")
class CallCycleViewModelFactory(private val repository: ICallCycleRepository, private val mdRepository: IMDataRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CallCycleViewModel(repository, mdRepository) as T
    }
}