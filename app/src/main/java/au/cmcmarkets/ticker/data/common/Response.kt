package au.cmcmarkets.ticker.data.common

sealed interface Response<T> {
    object Loading : Response<Nothing>
    data class Success<T>(val data: T) : Response<T>
    class Error(val errorMessage: String) : Response<Nothing>
}