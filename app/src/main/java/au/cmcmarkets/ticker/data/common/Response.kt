package au.cmcmarkets.ticker.data.common

sealed class Response<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T>(data: T? = null) : Response<T>(data)
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(errorMessage: String?, data: T? = null) : Response<T>(data, errorMessage)
}