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


class Box<T>(val content:T){
    fun  getValueOfDefaultValue(value:T?, defaultValue:T = defaultValue()):T{
        return value ?: defaultValue
    }

    private  fun defaultValue():T{
        throw NoSuchElementException("No Default Value Provided")
    }
}


    fun processTime(items: MutableCollection<in Any>) {
        val iterator = items.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item == 3) {
                items.add(94) // ❌ Throws ConcurrentModificationException in ArrayList
            }
            println(item)
        }
    }

fun main() {
//
//    val numbers: MutableCollection<Any> = mutableListOf(1, 2, 3, 4, 5)
//    processTime(numbers) // ❌ Throws ConcurrentModificationException

//    val box = Box<Int>(932)
//    print("${box.getValueOfDefaultValue(null)}")


//    val van = Van(Snack())
//    println(van.getPassenger())

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




//    val stringContainer:Container<Any> = Container<Any>("3043")
////    val anyContainer:Container<Any> = stringContainer
//
//    println(stringContainer.value)

//    val appleProducer :FruitProducer<Fruit> = FruitProducer()
//    val fruitProducer :FruitProducer<Apple> = appleProducer
//    println(fruitProducer.consume())
//
//   Cologger.log("hellow")
//
//
//    val stringSource: Source<String> = object : Source<String>{
//        override fun next(): String {
//            return "Hello"
//        }
//    }
//
//    val anySource:Source<Any> = stringSource
//    println(anySource.next())
//
//    val sinkString :Sink<Fruit> = object :Sink<Fruit>{
//        override fun accept(item: Fruit) {
//            println("${item.name} + ${item.type}  = healthy")
//        }
//
//    }
//    val sinkAny:Sink<Apple> = sinkString
//    feedSink(Apple("Apple", "fruit"),sinkAny)

//    val producerPhone:ProducerPhone<Phone> = ProducerPhone(Ios())
//    val consumerPhone:ConsumerPhone<Ios> = ConsumerPhone<Phone>()
//    consumerPhone.printPhone(Ios())
//    println(producerPhone.returnPhone())




    val randomList = listOf(3,"NI", false, "TY", 9.0, true, "N")

   isOfType<Boolean>("true"){it ->
       when(it){
           is String ->  { println("is String $it")}
           is Int ->  { println("is Int $it")}
           is Boolean ->  { println("is Boolean $it")}
           is List<*> -> {println("is List $it")}
       }
   }
    val name = "Nityen"
    println(name.plus(""))

    val p1 =  Point(3,4)
    val p2 =  Point(4,3)
    println(p1 + p2)

    val v1 = Vector(2, 3)
    val v2 = Vector(4, 5)

    println(v1[v2])
}




open class  Fruit(val name:String , val type:String)
 class Apple( name:String, type:String):Fruit(name, type)

interface Source<out T>{
    fun next():T
}

interface Sink<in T>{
    fun accept(item:T)
}

fun <T> feedSink(item:T, sink:Sink<T>){
    sink.accept(item)
}

class  FruitProducer<in T:Fruit>{
//    fun produce():T{
//        return fruit
//    }

    fun consume():String{
        return "Consume a fruit"
    }
}

class ProducerPhone<out T: Phone>(private val phone: T){
    fun returnPhone():T{
        return phone
    }
}
class ConsumerPhone<in T:Phone>(){
    fun printPhone(phone:T){
        println(phone)
    }
}
interface Phone {

}

class Android:Phone{

}

class Ios:Phone{

}

//object Cologger : Logger{
//    override fun log(message: String) {
//        println(message)
//    }
//
//}
//
//
//interface Logger {
//    fun log(message: String)
//}
//



//class Container<T>(val value:T)
