package com.ndroid.kotlin_practice

inline fun <reified  T> isOfType(value: Any, result: (Any) -> Any){
    if(value is List<*>){
        val filteredList = mutableListOf<T>()
        value.forEach{
            if(it is T){
                filteredList.add(it)
            }
        }
        result(filteredList)
    }else{
        result(value is T)
    }
}





