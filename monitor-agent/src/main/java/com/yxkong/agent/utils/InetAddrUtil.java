package com.yxkong.agent.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 本机ip获取工具
 * @Author: yxkong
 * @Date: 2021/4/20 10:16 上午
 * @version: 1.0
 */
public class InetAddrUtil {

    public static String getHost(){
        return getLocalHostLanAddress().getHostAddress();
    }
    public static InetAddress getLocalHostLanAddress()  {
        InetAddress candidateAddress = null;
        try {
            // 遍历所有的网络接口
            for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    // 排除loopback类型地址
                    if (inetAddr.isLoopbackAddress()) {
                        continue;
                    }
                    if (inetAddr.isSiteLocalAddress()) {
                        // 如果是site-local地址，就是它了
                        return inetAddr;
                    } else if (candidateAddress == null) {
                        // site-local类型的地址未被发现，先记录候选地址
                        candidateAddress = inetAddr;
                    }
                }
            }
            if (candidateAddress == null) {
                // 如果没有发现 non-loopback地址.只能用最次选的方案
                candidateAddress = InetAddress.getLocalHost();
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return candidateAddress;
    }
}