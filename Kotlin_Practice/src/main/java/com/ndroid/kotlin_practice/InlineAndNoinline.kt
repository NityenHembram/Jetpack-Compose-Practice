package com.ndroid.kotlin_practice

fun main() {
    higherOrderFunction() {
        "Hey"
    }



    val contacts = listOf(1,2,5,53,5,32,2)

    val a  = ""
//    println(
//    manageContacts(contacts){ it ->
//         println(it)
//        return@manageContacts
//    })


//    myFunction(
//        { println("Inlined Lambda") },
//        { println("Not Inlined Lambda") }
//    )



    processList(listOf(1, 2, 3)) { num ->
        if (num == 2) return  // ✅ Allowed (Exits processList function)
        println(num)
    }

    println("Finished")
}
 inline fun higherOrderFunction(action: () -> String) {
    println("Before action")
    action()
    println("After action")

//     testCrossLine {
//         println("Hwllo")
//         return@testCrossLine
//     }


}

inline fun myFunction(inlined: () -> Unit, noinline notInlined: () -> Unit) {
    inlined()  // This will be inlined
    notInlined() // This will NOT be inlined (creates an object)
}

inline fun manageContacts(contacts:List<Int>,oneCallback: (Int) -> Int, noinline callBack: (Int) -> Unit){
    for(contact in contacts){
         callBack(contact)
    }
}


inline fun testCrossLine( crossinline params: (Int)->Unit){
    Runnable{
        params(3)
    }.run()

}

inline fun processList(numbers: List<Int>,  operation: (Int) -> Unit) {
    for (num in numbers) {
        operation(num)
    }
}