package iam.thevoid.epic.myapplication.di_sample

object DI {
    val car = Car(GasolineEngine())

    val carDiesel = Car(DieselEngine())
}