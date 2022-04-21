package iam.thevoid.epic.myapplication.di_sample

interface Engine {
    fun start(): Boolean
}

class DieselEngine : Engine {
    override fun start(): Boolean {
        return true
    }
}

class GasolineEngine : Engine {
    override fun start(): Boolean {
        return true
    }

}