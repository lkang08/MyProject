package com.lk.myproject.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.lk.myproject.R
import com.lk.myproject.widget.WaveHelper
import com.lk.myproject.widget.WaveProgressView
import kotlinx.android.synthetic.main.activity_seek_bar.*

class SeekBarActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek_bar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                myText.text = "Text:$progress"
                waveProgress.waterLevelRatio = progress.toFloat() / 100
                waveProgress.updateCurrProgressText("$progress%")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        button.setOnClickListener {
            var type = when (index++ % 3) {
                0 -> {
                    WaveProgressView.ShapeType.CIRCLE
                }
                1 -> {
                    WaveProgressView.ShapeType.SQUARE
                }
                else -> {
                    WaveProgressView.ShapeType.HEART
                }
            }
            waveProgress.setShapeType(type)
        }
        init()
    }

    var index = 1

    private fun init() {
        waveProgress.setShapeType(WaveProgressView.ShapeType.SQUARE)
        waveProgress.setWaveColor(Color.parseColor("#28dbff"), Color.parseColor("#71dbff"))
        var mWaveHelper = WaveHelper(waveProgress)
        mWaveHelper.start()
    }
}
