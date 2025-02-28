package com.ndroid.kotlin_practice


class ArrayUtils<T>(private val array: Array<T>){
    fun findElement(element:T, foundElement: (index:Int, element:T?) -> Unit){
        for(i in array.indices){
            if(array[i] == element){
                foundElement(i, array[i])
                return
            }
        }
        foundElement(-1,null)
        return
    }
}


fun <abc,i> testingGenerics (a:Int, b:String){
    println("$a  $b")
}

class DataProcessor<Any> {  // ❌ Incorrect generic use
    fun processList(data: List<Any>) {  // ❌ Using List<Any>
        for (item in data) {
            when (item) {
                is Int -> println("Int: ${item * 2}")   // ✅ Works fine
                is String -> println("String: ${item.uppercase()}") // ✅ Works fine
                else -> println("Unknown Type")
            }
        }
    }
}

class Calculator <T:Number>{
    fun squre(value:T):Double{
        return  value.toDouble() * value.toDouble()
    }
}



fun printList(list: List<Any>) {
    for (item in list) {
        println(item)
    }
}




//data class Phyysicist(val firstName:String)
//
//open class Animal()
//class Frog:Animal()
//class Snack:Animal()
//
//data class Transport<T>(var passenger : T)


open class Thingy()
open class Animal:Thingy()

class Frog:Animal()
class Snack:Animal()

interface Transport<T:Thingy> {
    abstract fun getPassenger():T
}

data class Van(private val animal: Animal):Transport<Thingy>{
    override fun getPassenger(): Thingy {
        return animal
    }

}

fun main() {

    val van = Van(Snack())
    println(van.getPassenger())

//    val kermit:Animal = Frog()
//    val cc = Transport(kermit)
//    cc.passenger = Snack()
//    println(cc.passenger)

//    val a  = mutableListOf<Any>()
//    a.add(true)
//    a.add("true")
//    print(a)
//   val oppenheimer  = AtomicReference(Phyysicist("Nityen"))
//    print(oppenheimer)


//   val au = ArrayUtils(arrayOf(1,3,2,23,32,))
//   val bu = ArrayUtils(arrayOf("Cat","Dog","Cow"))
//    bu.findElement("Cow"){ index, element ->
//        println("$index  $element")
//    }
//
//    testingGenerics<Long,Boolean>(1,"fjdl")

//    val calculator = Calculator<Number>()
//    println(calculator.squre(3))



}
