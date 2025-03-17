package com.ndroid.kotlin_practice

open class animal(val age: Int)

class Dog(age: Int) : animal(age), Comparable<Dog> {
    override fun compareTo(other: Dog): Int {
        return age - other.age
    }
}

fun <T> findOldAnimal(anima: List<T>): T where T : animal, T : Comparable<T> {
    return anima.maxOrNull() ?: throw IllegalArgumentException("List is empty")
}