package com.lk.myproject.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lk.myproject.R
import com.lk.myproject.animation.effects.Effectstype
import com.lk.myproject.animation.effects.NiftyDialogBuilder

class DialogAnimationActivity : Activity() {

    private var effect: Effectstype? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_animation)
    }

    fun dialogShow(v: View) {
        val dialogBuilder: NiftyDialogBuilder = NiftyDialogBuilder.getInstance(this)
        when (v.id) {
            R.id.fadein -> effect = Effectstype.Fadein
            R.id.slideright -> effect = Effectstype.Slideright
            R.id.slideleft -> effect = Effectstype.Slideleft
            R.id.slidetop -> effect = Effectstype.Slidetop
            R.id.slideBottom -> effect = Effectstype.SlideBottom
            R.id.newspager -> effect = Effectstype.Newspager
            R.id.fall -> effect = Effectstype.Fall
            R.id.sidefall -> effect = Effectstype.Sidefill
            R.id.fliph -> effect = Effectstype.Fliph
            R.id.flipv -> effect = Effectstype.Flipv
            R.id.rotatebottom -> effect = Effectstype.RotateBottom
            R.id.rotateleft -> effect = Effectstype.RotateLeft
            R.id.slit -> effect = Effectstype.Slit
            R.id.shake -> effect = Effectstype.Shake
        }
        dialogBuilder
            .withTitle("Modal Dialog") //.withTitle(null)  no title
            .withTitleColor("#FFFFFF") //def
            .withDividerColor("#11000000") //def
            .withMessage("This is a modal Dialog.") //.withMessage(null)  no Msg
            .withMessageColor("#FFFFFFFF") //def  | withMessageColor(int resid)
            .withDialogColor("#FFE74C3C") //def  | withDialogColor(int resid)                               //def
            .withIcon(resources.getDrawable(R.drawable.icon))
            .isCancelableOnTouchOutside(true) //def    | isCancelable(true)
            .withDuration(700) //def
            .withEffect(effect) //def Effectstype.Slidetop
            .withButton1Text("OK") //def gone
            .withButton2Text("Cancel") //def gone
            .setCustomView(R.layout.custom_view, v.context) //.setCustomView(View or ResId,context)
            .setButton1Click(View.OnClickListener { v -> Toast.makeText(v.context, "i'm btn1", Toast.LENGTH_SHORT).show() })
            .setButton2Click(View.OnClickListener { v -> Toast.makeText(v.context, "i'm btn2", Toast.LENGTH_SHORT).show() })
            .show()
    }
}