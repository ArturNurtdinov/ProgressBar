package com.example.interpolator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    enum class TaskState {
        FREE, PRE_WORKING, CONNECTING, POST_WORKING, COMPLETED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timer = Timer()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tv = findViewById<TextView>(R.id.textView)
        var taskState = TaskState.FREE
        var taskProgress = 0

        class ToChangeTV(var text: String) : Runnable {
            override fun run() {
                tv.text = text
            }
        }
        button.setOnClickListener {
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    when (taskState) {
                        TaskState.FREE -> {
                            taskState = TaskState.PRE_WORKING
                            ++taskProgress

                            progressBar.progress = taskProgress

                            runOnUiThread(ToChangeTV("Working..."))
                        }

                        TaskState.PRE_WORKING -> {
                            if (taskProgress < 50) {
                                ++taskProgress

                                progressBar.progress = taskProgress
                            } else {
                                taskState = TaskState.CONNECTING

                                runOnUiThread(ToChangeTV("Connecting to server"))
                                progressBar.isIndeterminate = true

                                this.cancel()
                            }
                        }

                        TaskState.CONNECTING -> {
                            taskState = TaskState.POST_WORKING
                            ++taskProgress

                            runOnUiThread(ToChangeTV("Working..."))
                            progressBar.isIndeterminate = false
                            progressBar.progress = taskProgress
                        }

                        TaskState.POST_WORKING -> {
                            if (taskProgress < 100) {
                                ++taskProgress

                                progressBar.progress = taskProgress
                            } else {
                                runOnUiThread(ToChangeTV("Completed!"))
                                taskState = TaskState.COMPLETED
                            }
                        }

                        TaskState.COMPLETED -> {
                            timer.cancel()
                        }
                    }
                }
            }, 1000, 250)
        }
    }
}
