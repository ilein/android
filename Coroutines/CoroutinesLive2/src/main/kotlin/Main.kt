import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
val coroutineScope = CoroutineScope(coroutineContext)
fun main(args: Array<String>) {
    println("Start program!")
    runBlocking {
        /**
         * Run blocking
         */
        //printHello()
        /* coroutineScope.launch {
             printHello()
         }.join()*/

        /**
         * Cancel coroutine on same thread
         */
        /*val job = launch {
            repeat(1000) { i ->
                println("Номер $i")
                delay(500L)
            }
        }
        delay(1300L) // delay a bit
        println("Остановитесь!")
        job.cancel() // отмена джобы
        println("Расчет окончен")*/

        /**
         * Cancel coroutine with Dispatchers.IO
         */
        /*val job = launch(Dispatchers.IO) {
            repeat(1000) { i ->
                println("Номер $i")
                delay(500L)
            }
        }
        delay(1300L) // delay a bit
        println("Остановитесь!")
        job.cancel() // отмена джобы
        job.join() //??
        println("Расчет окончен")*/

        /**
         * Cancel scope vs cancel children
         */
        /*val scope = CoroutineScope(Job())
        scope.launch {
            println("First job complete")
        }
        scope.launch {
            println("Second job complete")
        }
        scope.cancel()
        scope.coroutineContext.cancelChildren()
        scope.launch {
            println("Third job complete")
        }*/

        /* */
        /**
         * Job vs SupervisorJob
         *//*
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
            // exception will be given here
        }
        val scope = CoroutineScope(Job() + coroutineExceptionHandler)
        val job1 = scope.launch {
            delay(3000)
            println("First job complete")
        }
        val job2 = scope.launch {
            delay(2000)
            println("Second job complete")
        }
        val job3 = scope.launch {
            println("Third job complete")
            throw java.lang.IllegalStateException("Ops")
        }
        job1.join()
        job2.join()
        job3.join()*/


        /**
         * Callback
         */
        /*createUser(object : ResultCallback {
            override fun onSuccess(user: User) {

            }

            override fun onError() {

            }
        })

        launch {
            val user2 = createUser()
            println(user.name)

            val user = createUserSuspend()
            user.name
        }*/

        /**
         * Cancellation exception
         */
        /* val job = launch(Dispatchers.IO) {
             try {
                 repeat(1000) { i ->
                     println("Загрузка файла $i")
                     delay(1000L)
                 }
             } catch (e: CancellationException) {
                 println("Корутину отменили")
             } finally {
                 println("удаляем скаченные файлы...")
             }
         }
         delay(3000L) // delay a bit
         println("Отмена загрузки")
         job.cancel() // отмена джобы
         job.join() //??*/
        Flow().main()
    }
}

suspend fun createUserSuspend(): User {
    /**
     * suspendCoroutine нельзя отменить job.cancel()
     */
    return suspendCoroutine { continuation ->
        createUser(object : ResultCallback {
            override fun onSuccess(user: User) {
                continuation.resume(user)
            }

            override fun onError() {
                continuation.resumeWithException(java.lang.Exception())
            }
        })
    }
}

fun createUser(callback: ResultCallback) {
    Thread.sleep(2000)
    callback.onSuccess(User("Петя"))
}

interface ResultCallback {
    fun onSuccess(user: User)
    fun onError()
}

data class User(val name: String)

suspend fun printHello() {
    delay(3000)
    println("Hello world!")
}

suspend fun downloadFile(): Int {
    delay(5000)
    println("Success download!")
    return 0
}

fun cancelDownload() {

}