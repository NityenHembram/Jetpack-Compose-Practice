package com.ndroid.kotlin_practice

import java.util.concurrent.Flow


sealed class Vehicle{
    data object Car : Vehicle()
}

data object SealedClassExample : Vehicle() {
}

sealed class ApiResponse{
    data class Success(val data:String):ApiResponse()
    data class Failure(val message:String):ApiResponse()
    data object loading :ApiResponse()
}

fun handleResponse(response: ApiResponse){
    when(response){
        is ApiResponse.Success -> {
            println("Success ${response.data}")
        }

        is ApiResponse.Failure -> { println("Failure: ${response.message}")
        }
        ApiResponse.loading -> {
            println("Loading....")
        }
    }
}

fun main() {
    val data = "name:Nityen,age:28"
    val success = ApiResponse.Success(data)
    val failure = ApiResponse.Failure("Something went wrong")
    val loading = ApiResponse.loading

    handleResponse(success)
    handleResponse(failure)
    handleResponse(loading)
}
class Ls{

}
