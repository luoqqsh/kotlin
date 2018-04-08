package comm.test.kotlin.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import io.reactivex.disposables.CompositeDisposable
import comm.test.kotlin.R
import comm.test.kotlin.api.RetrofitManager
import comm.test.kotlin.api.SchedulerUtils

class WelcomeActivity : BaseActivity() {
    private var compositeDisposable = CompositeDisposable()
    override fun start() {
    }

    @SuppressLint("CheckResult")
    override fun initData() {
        handler.sendEmptyMessageDelayed(1, 3000)
    }

    override fun initView() {

    }

    override fun layoutId(): Int {
        return R.layout.activity_welcome
    }

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            getversion()
        }
    }

    fun getversion() {
        val disposable = RetrofitManager.service.Version("aa", "bb")
                .compose(SchedulerUtils.ioToMain())
                .subscribe({ version ->

                }, { t ->
//                  ViseLog.d(ExceptionHandle.handleException(t))
                })
        compositeDisposable.add(disposable)
    }



}
