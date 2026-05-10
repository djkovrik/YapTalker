package ru.terrakok.cicerone.commands

interface Command

data class Forward(val screenKey: String, val data: Any?) : Command

data class Replace(val screenKey: String, val data: Any?) : Command

object Back : Command
