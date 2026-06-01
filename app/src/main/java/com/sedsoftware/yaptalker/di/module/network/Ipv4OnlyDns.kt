package com.sedsoftware.yaptalker.di.module.network

import okhttp3.Dns
import java.net.Inet4Address
import java.net.InetAddress

class Ipv4OnlyDns : Dns {

    override fun lookup(hostname: String): List<InetAddress> =
        Dns.SYSTEM.lookup(hostname).filterIsInstance<Inet4Address>()
}
