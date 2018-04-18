package com.example.pektu.lifecounter.View.View.Activities

import android.app.AlarmManager
import android.app.Fragment
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.pektu.lifecounter.Controller.AndroidController
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Controller.Services.DayRateService
import com.example.pektu.lifecounter.Controller.Services.DoPlanNotificationSenderService
import com.example.pektu.lifecounter.Controller.Services.PlansUpdaterService
import com.example.pektu.lifecounter.Controller.Services.UndonePlansNotificationSenderService
import com.example.pektu.lifecounter.Controller.TimeManager
import com.example.pektu.lifecounter.Model.AndroidModel
import com.example.pektu.lifecounter.Model.DB.DB
import com.example.pektu.lifecounter.Model.DB.DBHelper
import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Preferences.AndroidPreferences
import com.example.pektu.lifecounter.R
import com.example.pektu.lifecounter.View.View.Fragments.MainFragment
import com.example.pektu.lifecounter.View.View.Interfaces.MainView
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainView {

    private val mainFragment = MainFragment()
    private lateinit var dbHelper: SQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        initController()

        fab.setOnClickListener { view ->
            ControllerSingleton.controller.onAddPlanButtonClickedInMainActivity()
        }

        changeFragment(mainFragment)

        nav_view.setNavigationItemSelectedListener(this)
        title = "Do It!"

        initServices()
        ControllerSingleton.controller.onMainViewInited()
    }

    private fun changeFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_main_layout, fragment).commit()
    }

    private fun initController() {
        dbHelper = DBHelper(this)
        val model = AndroidModel(DB(dbHelper.writableDatabase), AndroidPreferences(getSharedPreferences("", Context.MODE_PRIVATE)))
        val controller = AndroidController(this, model)
        ControllerSingleton.init(controller)
    }

    private fun initServices() {
        initStickyServices()
        initAlarmManagerServices()
    }

    private fun initStickyServices() {
        val notificationSenderServiceIntent = Intent(this, DoPlanNotificationSenderService::class.java)
        val plansUpdaterServiceIntent = Intent(this, PlansUpdaterService::class.java)
        startService(notificationSenderServiceIntent)
        startService(plansUpdaterServiceIntent)
    }

    private fun initAlarmManagerServices() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        initUndonePlansNotificationSenderService(alarmManager)
        initRateDayService(alarmManager)
    }

    private fun initUndonePlansNotificationSenderService(alarmManager: AlarmManager) {
        val intent = Intent(this, UndonePlansNotificationSenderService::class.java)
        intent.action = UndonePlansNotificationSenderService.OPERATION_SEND_NOTIFICATION
        val pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        TimeManager.dayTimeGettingOut() // To init TimeManager.dayRemainingTime
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
                + TimeManager.dayRemainingTime * 60 * 1000 - UndonePlansNotificationSenderService.TIME_TO_SlEEP_START_SEND_NOTIFICATIONS_MS, pendingIntent)
    }

    private fun initRateDayService(alarmManager: AlarmManager) {
        val intent = Intent(this, DayRateService::class.java)
        intent.action = DayRateService.INTENT_ACTION_SEND_NOTIFICATION
        intent.putExtra(DayRateService.INTENT_DATE_EXTRA_NAME, ControllerSingleton.controller.getModel().getCurrentDay().toString())
        val pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        TimeManager.dayTimeGettingOut()
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
                + TimeManager.dayRemainingTime * 60 * 1000 - DayRateService.TIME_TO_START_SEND_NOTIFICATION_DELTA_MS, pendingIntent)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                ControllerSingleton.controller.onSettingsButtonClick()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showStartActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDayActivity(date: DayDate, requestCode: Int) {
        val intent = Intent(this, DayPlansActivity::class.java)
        intent.putExtra(PlansView.INTENT_REQUEST_CODE, requestCode)
        intent.putExtra(PlansView.INTENT_DATE, date.toString())
        startActivityForResult(intent, 0)
    }

    override fun showFirstSetUpActivity() {
        val intent = Intent(this, FirstSetUpActivity::class.java)
        startActivity(intent)
    }

    override fun showSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
