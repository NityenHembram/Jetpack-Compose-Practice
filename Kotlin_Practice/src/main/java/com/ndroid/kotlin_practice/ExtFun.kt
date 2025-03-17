package com.ndroid.kotlin_practice

fun String.plus(other: Any?): String{
    return "this + Nityen $other"
}

data class Point(val x:Int, val y:Int)

  operator fun Point.plus(other:Point):Point{
    return Point(this.x + other.x, this.y +other.y)
}

data class Vector(val x: Int, val y: Int) {
    operator fun get(other: Vector): Vector {
        return Vector(this.x + other.x, this.y + other.y)
    }
}
