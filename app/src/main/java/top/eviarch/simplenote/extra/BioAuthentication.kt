package top.eviarch.simplenote.extra

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import top.eviarch.simplenote.R
import top.eviarch.simplenote.core.SimpleNoteApplication

fun bioAuthentication (
    context: Context,
    subTitle: String,
    onSuccess: () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                ToastUtil.showToast(SimpleNoteApplication.Context.getString(R.string.authentication_error) + errString, Toast.LENGTH_LONG)
            }
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                ToastUtil.showToast(SimpleNoteApplication.Context.getString(R.string.authentication_failure), Toast.LENGTH_LONG)
            }
        })
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(SimpleNoteApplication.Context.getString(R.string.unlock_title))
        .setSubtitle(subTitle)
        .setNegativeButtonText(SimpleNoteApplication.Context.getString(R.string.cancel))
        .build()
    biometricPrompt.authenticate(promptInfo)
}