package iam.thevoid.epic.myapplication.presentation.ui

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import iam.thevoid.epic.myapplication.R

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}