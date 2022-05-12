import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class Flow {
    private val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    fun main() {
        runBlocking {
            /**
             * Подписка на flow #1
             */
            scope.launch {
                createFlow().collect { value ->
                    println("Emit $value")
                }
            }.join()


            createFlow()
                .onEach { println("Emit $it") }
                .launchIn(scope)

            /**
             * Operators
             */
            createFlow()
                .filter { it % 2 == 0 }
                .onEach { println("Emit $it") }
                .launchIn(scope)

            /**
             * FLow on
             */
            createFlow()
                .filter { it % 2 == 0 }
                .flowOn(Dispatchers.IO)
                .onEach { println("Emit $it Thread = ${Thread.currentThread().name}") }
                .flowOn(Dispatchers.Default)
                .launchIn(scope)

            /**
             * Shared flow
             */
            val sharedFlow = MutableSharedFlow<String>(1)
            sharedFlow.emit("Moscow")
            scope.launch {
                sharedFlow
                    .onEach {
                        println("City $it")
                    }
                    .launchIn(scope)
            }.join()
        }
    }

    fun createFlow(): Flow<Int> {
        var counter = 0
        return flow {
            repeat(100) {
                delay(1000)
                emit(counter++)
            }
        }
    }
}