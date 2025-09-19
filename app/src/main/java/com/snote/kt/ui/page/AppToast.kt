package com.snote.kt.ui.page

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @author snote
 * @date 2025/9/11 10:42
 * @description Toast
 */

/**
 * 一个全局的、类似传统 Toast 的工具类，背后由 Snackbar 实现。
 *
 * 使用前必须在你的 Composable 树的顶层调用 [AppToastProvider] 来进行初始化。
 */
object AppToast {

    private var snackbarHostState: SnackbarHostState? = null
    private var coroutineScope: CoroutineScope? = null

    /**
     * 由 AppToastProvider 调用，用于注入必要的依赖。
     * @internal Internal use only.
     */
    internal fun register(
        snackbarHostState: SnackbarHostState,
        coroutineScope: CoroutineScope
    ) {
        this.snackbarHostState = snackbarHostState
        this.coroutineScope = coroutineScope
    }

    /**
     * 清理引用，防止内存泄漏。
     * @internal Internal use only.
     */
    internal fun unregister() {
        this.snackbarHostState = null
        this.coroutineScope = null
    }

    /**
     * 显示一条消息。
     *
     * @param message 要显示的消息文本。
     * @param actionLabel (可选) 动作按钮的文本。
     * @param duration (可选) 显示时长。
     */
    fun show(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        val hostState = snackbarHostState
        val scope = coroutineScope

        // 如果未注册，抛出异常提醒开发者
        if (hostState == null || scope == null) {
            throw IllegalStateException(
                "AppToast is not registered. Please call AppToastProvider at the top of your Composable hierarchy."
            )
        }

        // 在注入的、与UI生命周期绑定的协程中显示 Snackbar
        scope.launch {
            hostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )
        }
    }
}