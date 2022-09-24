package com.yuk.miuiHomeR.ui

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.BuildConfig
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import moralnorm.preference.Preference


class AboutActivity : BaseAppCompatActivity() {

    override fun initFragment(): Fragment {
        return AboutFragment()
    }

    class AboutFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_about
        }

        override fun initPrefs() {
            val mVersion = findPreference<Preference>("prefs_key_about_version")
            val mQQGroup = findPreference<Preference>("prefs_key_about_join_qq_group")

            mVersion.title = "v" + BuildConfig.VERSION_NAME + " - " + BuildConfig.BUILD_TYPE

            mQQGroup.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                joinQQGroup("g405srEn4hafy8xSJg1_EFJjxceLvpd7")
                true
            }
        }

        /****************
         *
         * 调用 joinQQGroup() 即可发起手Q客户端申请加群
         * @param key 由官网生成的key
         * @return 返回true表示呼起手Q成功，返回false表示呼起失败
         */
        private fun joinQQGroup(key: String): Boolean {
            val intent = Intent()
            intent.data =
                Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key")
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return try {
                startActivity(intent)
                true
            } catch (e: Exception) {
                // 未安装手Q或安装的版本不支持
                false
            }
        }

    }
}