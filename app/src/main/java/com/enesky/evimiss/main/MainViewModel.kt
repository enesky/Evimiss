package com.enesky.evimiss.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enesky.evimiss.data.model.EventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.LocalDate
import javax.inject.Inject

/**
 * Created by Enes Kamil YILMAZ on 14/02/2022
 */

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel()