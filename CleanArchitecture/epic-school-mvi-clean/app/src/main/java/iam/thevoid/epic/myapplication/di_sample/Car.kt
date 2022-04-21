package iam.thevoid.epic.myapplication.di_sample

class Car(private val engine: Engine) {
    fun start(): String {
        if (engine.start()) {
            return "OK"
        } else {
            return "FAIL"
        }
    }
}

fun test() {
   // val engine: Engine = mock<DieselEngine>(on { start() } doReturn true)
   // val engineFail: Engine = mock<DieselEngine>(on { start() } doReturn false)

   // assert(Car(engine).start() == "OK")
   // assert(Car(engineFail).start() == "FAIL")
}