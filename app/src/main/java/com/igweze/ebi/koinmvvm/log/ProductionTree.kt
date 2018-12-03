package ng.max.champion.config.log

import android.util.Log
import com.crashlytics.android.Crashlytics
import ng.max.champion.BuildConfig
import timber.log.Timber

class ProductionTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.INFO) {
            val tagPrime = tag ?: "UNKNOWN"
            Log.i(tagPrime, message)
        } else if (priority == Log.ERROR) {
            if (!BuildConfig.DEBUG) Crashlytics.logException(t)
        }
    }

}