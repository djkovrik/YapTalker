package ru.terrakok.cicerone.android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

open class SupportAppNavigator(
    private val activity: AppCompatActivity,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : Navigator {

    override fun applyCommands(commands: Array<out Command>) {
        commands.forEach { command ->
            when (command) {
                is Forward -> forward(command)
                is Replace -> replace(command)
                is Back -> back()
            }
        }
    }

    open fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? = null

    open fun createFragment(screenKey: String?, data: Any?): Fragment? = null

    open fun setupFragmentTransactionAnimation(
        command: Command?,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction?
    ) = Unit

    private fun forward(command: Forward) {
        createActivityIntent(activity, command.screenKey, command.data)?.let {
            activity.startActivity(it)
            return
        }

        createFragment(command.screenKey, command.data)?.let { fragment ->
            val current = fragmentManager.findFragmentById(containerId)
            val transaction = fragmentManager.beginTransaction()
            setupFragmentTransactionAnimation(command, current, fragment, transaction)
            transaction.replace(containerId, fragment)
            transaction.addToBackStack(command.screenKey)
            transaction.commit()
        }
    }

    private fun replace(command: Replace) {
        createActivityIntent(activity, command.screenKey, command.data)?.let {
            activity.startActivity(it)
            return
        }

        createFragment(command.screenKey, command.data)?.let { fragment ->
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val current = fragmentManager.findFragmentById(containerId)
            val transaction = fragmentManager.beginTransaction()
            setupFragmentTransactionAnimation(command, current, fragment, transaction)
            transaction.replace(containerId, fragment)
            transaction.commit()
        }
    }

    private fun back() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            activity.finish()
        }
    }
}
