package top.eviarch.simplenote.extra

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import top.eviarch.simplenote.core.SimpleNoteApplication

object ToastUtil {
    private lateinit var mToast: Toast

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        val context: Context = SimpleNoteApplication.Context
        mToast = Toast(context).apply {
            setText(message)
            this.duration = duration
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    fun forceShowToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        forceCancelToast()
        showToast(message, duration)
    }

    /**
     * Cancel Toast manually
     */
    private fun forceCancelToast() {
        if (::mToast.isInitialized) {
            mToast.cancel()
        }
    }
}