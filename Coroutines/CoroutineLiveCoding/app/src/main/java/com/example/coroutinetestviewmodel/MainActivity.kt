package com.example.coroutinetestviewmodel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutinetestviewmodel.databinding.MainActivityBinding
import com.example.coroutinetestviewmodel.ui.main.BackendApi
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val backendApi = BackendApi()

    private val context: CoroutineContext = SupervisorJob() + Dispatchers.Main
    private val scope = CoroutineScope(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            scope.launch(Dispatchers.IO) {
                //Паралельный запрос погоды
                val moscow = async { weatherMoscow() }
                val kazan = async { weatherKazan() }


                val result = "В москве ${moscow.await()} в Казани ${kazan.await()}"
                withContext(Dispatchers.Main) {
                    //Показываем в textview на Main потоке
                    binding.textViewTowns.text = result
                }
            }
        }

        binding.button2.setOnClickListener {
            //Отмена дочерних корутин в скоупе
            scope.coroutineContext.cancelChildren()
        }
    }

    suspend fun weatherMoscow(): String {
        delay(3000)
        return "Облачно"
    }

    suspend fun weatherKazan(): String {
        delay(1000)
        return "Солнечно"
    }

    suspend fun calc(): Long {
        return withContext(Dispatchers.Default) {
            delay(5000)
            return@withContext 123
        }
    }

    suspend fun launchExample() {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        scope.launch(dispatcher) {
            //On single thread executor dispatcher
            val cities = backendApi.getCities().joinToString()

            withContext(Dispatchers.Main) {
                //on main thread
                binding.textViewTowns.text = cities
            }
            withContext(Dispatchers.IO) {
                //on IO dispatcher
                val cities3 = backendApi.getCities().map { "Город $it" }
                Log.d("EPIC", cities3.joinToString())
            }
        }
    }

    override fun onStop() {
        super.onStop()
        //Отмена скоупа и всех корутин
        scope.cancel()
    }
}