package com.chatapp.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<S : UIState, in I : UIEvent>:ViewModel() {

    private val _uiState by lazy { MutableStateFlow(initialState) }
    val uiState: StateFlow<S> by lazy { _uiState }

    abstract val initialState: S

    abstract fun handleUserIntent(intent: I)

    fun sendUIEvent(uiEvent: I) {
        handleUserIntent(uiEvent)
    }

    protected fun updateState(handler: (oldState: S) -> S) {
        _uiState.update { handler(uiState.value) }
    }

    protected fun <R> Flow<ApiResult<R>>.handleApiResultFlow(
        scope: CoroutineScope,
        onSuccess: (R) -> Unit,
        onError: (String?) -> Unit,
        onLoading: () -> Unit
    ) {
        scope.launch {
            withContext(Dispatchers.IO) {
                catch { exception ->
                    withContext(Dispatchers.Main) {
                        onError(exception.message)
                    }
                }.collect { result ->
                    withContext(Dispatchers.Main) {
                        when (result.status) {
                            ApiStatus.SUCCESS -> result.data?.let { onSuccess(it) }
                            ApiStatus.ERROR -> onError(result.message)
                            ApiStatus.LOADING -> onLoading()
                        }
                    }
                }
            }
        }
    }
}