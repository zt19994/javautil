package com.util.serverutil;

import com.sun.management.OperatingSystemMXBean;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 服务器信息工具类
 *
 * @author zt1994 2019/7/11 20:53
 */
public class ServerInfoUtil {

    private static Logger logger = Logger.getLogger(ServerInfoUtil.class);


    /**
     * 获取CPU使用情况
     */
    public static Float getCPUUsage() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        // 用来分隔String的应用类
        StringTokenizer tokenStat = null;
        try {
            Process process = Runtime.getRuntime().exec("top -b"); // top命令是Linux下常用的性能分析工具，能够实时显示系统中各个进程的资源使用情况。
            is = process.getInputStream(); // 可以读取新开启的程序的 System.out.print 输出的内容
            isr = new InputStreamReader(is); // 将字节流转换为字符流。
            brStat = new BufferedReader(isr); // BufferedReader 流能够读取文本行
            brStat.readLine();
            brStat.readLine();

            tokenStat = new StringTokenizer(brStat.readLine());
            tokenStat.nextToken();
            logger.info("用户空间占用CPU的百分比 : " + tokenStat.nextToken());
            tokenStat.nextToken();
            logger.info("内核空间占用CPU的百分比 : " + tokenStat.nextToken());
            tokenStat.nextToken();
            tokenStat.nextToken();
            tokenStat.nextToken();
            String cpuFree = tokenStat.nextToken();

            logger.info("空闲CPU百分比 : " + cpuFree);
            Float free = new Float(cpuFree);

            Float usage = 1 - free / 100;
            return usage;
        } catch (IOException e) {
            logger.info("获取CPU使用情况错误:{}" + e.getMessage());
        }
        return -1f;
    }


    /**
     * 获取内存使用情况
     */
    public static Float getMemeryUsage() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        // 总的物理内存+虚拟内存
        // long totalvirtualMemory = osmxb.getTotalSwapSpaceSize();
        // 物理内存（内存条）
        long physicalMemorySize = osmxb.getTotalPhysicalMemorySize();
        logger.info("物理内存：" + physicalMemorySize / 1024.0 / 1024.0 / 1024.0);
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        logger.info("总  的  内 存" + Math.round(physicalMemorySize / 1024.0 / 1024.0 / 1024.0));
        logger.info("使用的内存" + Math.round((physicalMemorySize - freePhysicalMemorySize) / 1024.0 / 1024.0 / 1024.0));
        Double compare = (1 - freePhysicalMemorySize * 1.0 / physicalMemorySize) * 100;
        return compare.floatValue();
    }


    /**
     * 获取硬盘使用情况
     */
    public static Float getDiskUsage() {
        try {
            Runtime rt = Runtime.getRuntime();
            // df -hl 查看硬盘空间
            Process p = rt.exec("df -hl");
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String str = null;
                String[] strArray = null;
                int line = 0;
                while ((str = in.readLine()) != null) {
                    line++;
                    if (line != 2) {
                        continue;
                    }
                    strArray = str.split(" ");
                    for (String para : strArray) {
                        if (para.trim().length() == 0) {
                            continue;
                        }
                        if (para.endsWith("%")) {
                            return Float.parseFloat(para);
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("获取硬盘使用情况错误:{}" + e.getMessage());
            } finally {
                in.close();
            }
        } catch (Exception e) {
            logger.info("获取硬盘使用情况错误:{}" + e.getMessage());
        }
        return -1f;
    }


    /**
     * 获取本地ip
     */
    public static String getLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();

                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
                        bFindIP = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }


    /**
     * 此方法描述的是：获得服务器的IP地址(多网卡)
     *
     * @author: zhangyang33@sinopharm.com
     * @version: 2014年9月5日 下午4:57:15
     */
    public static List<String> getLocalIPS() {
        InetAddress ip = null;
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                    .getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        ipList.add(ip.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipList;
    }


    /**
     * 此方法描述的是：获得服务器的MAC地址
     *
     * @author: zhangyang33@sinopharm.com
     * @version: 2014年9月5日 下午1:27:25
     */
    public static String getMacId() {
        String macId = "";
        InetAddress ip = null;
        NetworkInterface ni = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                    .getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                ni = (NetworkInterface) netInterfaces
                        .nextElement();
                // ----------特定情况，可以考虑用ni.getName判断
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (!ip.isLoopbackAddress() // 非127.0.0.1
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        bFindIP = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != ip) {
            try {
                macId = getMacFromBytes(ni.getHardwareAddress());
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return macId;
    }


    /**
     * 此方法描述的是：获得服务器的MAC地址(多网卡)
     *
     * @author: zhangyang33@sinopharm.com
     * @version: 2014年9月5日 下午1:27:25
     */
    public static List<String> getMacIds() {
        InetAddress ip = null;
        NetworkInterface ni = null;
        List<String> macList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                    .getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                ni = (NetworkInterface) netInterfaces
                        .nextElement();
                // ----------特定情况，可以考虑用ni.getName判断
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (!ip.isLoopbackAddress() // 非127.0.0.1
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        macList.add(getMacFromBytes(ni.getHardwareAddress()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macList;
    }


    /**
     * 从Bytes获取mac
     */
    private static String getMacFromBytes(byte[] bytes) {
        StringBuffer mac = new StringBuffer();
        byte currentByte;
        boolean first = false;
        for (byte b : bytes) {
            if (first) {
                mac.append("-");
            }
            currentByte = (byte) ((b & 240) >> 4);
            mac.append(Integer.toHexString(currentByte));
            currentByte = (byte) (b & 15);
            mac.append(Integer.toHexString(currentByte));
            first = true;
        }
        return mac.toString().toUpperCase();
    }
}
