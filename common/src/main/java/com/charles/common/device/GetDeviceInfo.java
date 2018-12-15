package com.charles.common.device;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.charles.common.app.BaseApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author charles
 * @date 16/10/22
 */

public class GetDeviceInfo {

    public static DeviceInfo getDeviceInfo() {
        Context context = BaseApplication.getContext();

        String deviceId = getDeviceId();
        String model = Build.MODEL;
        String cpuName = getCpuName();
        String memory = String.valueOf(getTotalMemory());
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        String phoneSize = String.valueOf(dm.widthPixels) + "*" + String.valueOf(dm.heightPixels);
        return new DeviceInfo(deviceId, model, cpuName, memory, phoneSize);
    }

    /**
     * 获取CPU型号
     * @return
     */
    private static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机内存信息，单位为MB
     * @return
     */
    private static long getTotalMemory() {
        // 系统内存信息文件
        String memInfo;
        String[] strs;
        long memory = 0;

        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
            // 读取meminfo第一行，系统内存大小
            memInfo = bufferedReader.readLine();
            strs = memInfo.split("\\s+");
            // 获得系统总内存，单位KB
            memory = Integer.valueOf(strs[1]);
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // KB转位为MB
        return memory / 1024;
    }

    /**
     * 这里进行广义的“imei”表示的获取
     * 如果获取不到macAddress那么获取androidId
     * 如果获取不到imei那么获取macAddress
     *
     * @return 将获取到的“imei”的信息进行返回
     */
    public static String getDeviceId() {
        Context context = BaseApplication.getContext();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imei = tm.getImei();
            } else {
                imei = tm.getDeviceId();
            }
            if (!imei.isEmpty()) {
                return imei;
            }
        }
        String androidId = Settings.Secure.getString(BaseApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!androidId.isEmpty()) {
            return androidId;
        }
        String macAddress = getWifiMacAddress();
        if (!macAddress.isEmpty()) {
            return macAddress;
        }
        return "010000000001";
    }

    /**
     * 获取 macAddress
     * @return
     */
    private static String getWifiMacAddress() {
        // 如果获取不到，就返回 defaultWifiAddress
        // defaultWifiAddress 应该是这种格式：02:00:00:00:00:02
        // 为了统一格式返回以下
        String defaultWifiAddress = "020000000002";
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }
                // mac地址，以作者自己手机为例:byte[6]:[-12, 96, -30, -94, 67, -4]
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return defaultWifiAddress;
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                // 以作者自己手机为例:buf="F4:60:E2:A2:43:FC"
                return buf.toString().replace(":", "");
            }
        } catch (Exception ignored) {
            return defaultWifiAddress;
        }
        return defaultWifiAddress;
    }

    /*
    /proc目录，它是一个虚拟的目录，其下面的文件和目录也都是虚拟的，不占用实际的存储空间，而是存在于系统内存中。proc以文件系统的方式为访问系统内核的操作提供接口，它是动态从系统内核中读出所需信息的。

    /proc/cmdline：显示内核启动的命令行。
    /proc/cpuinfo：显示系统cpu的信息。
    /proc/filesystems，显示当前注册了的文件系统列表，nodev表示为虚拟文件系统 
    /proc/interrupts:显示当前系统的中断信息
    /proc/ioports：被占用的输入/输出地址范围列表
    /proc/kmsg：输出内核消息日志。
    /proc/loadavg：监控cpu平均负载，其数值为所有核上cpu占用的累加值，前三个分别表示最近1、5、15分钟的平均负载，第四个表示当前运行进程数和进程总数，最后一个表示最近运行的进程id。
    /proc/locks:打开文件上的加锁信息。
    /proc/meminfo：显示物理及虚拟内存使用情况。
    /proc/misc：内核函数misc_register登记的设备驱动程序。
    /proc/modules：加载的内核模块列表。
    /proc/mounts：当前系统所安装的文件系统信息（包括手动安装的）。
    /proc/stat:系统简要信息。 
    /proc/uptime：分别表示系统启动时间和系统空闲时间。
    /proc/version：系统内核版本。
    /proc/net:其实际挂载点是/proc/self/net，能够显示当前各种网络情况，例如通过tcp文件可以查看tcp连接数及连接情况。
    /proc/sys 报告各种不同的内核参数，某些参数能在root的情况下进行修改。
    /proc/devices 当前挂载的所有软硬件设备(字符设备和块设备)，包括主设备号和设备名称。
    /proc/asound：声卡相关的信息。
    /proc/buddyinfo：每个内存区中每个order有多少块可用，和内存碎片问题有关。
    /proc/bus：输入设备信息。
    /proc/cgroups：查看cgroups子系统信息。
    /proc/diskstats：用于显示磁盘、分区和统计信息。
    /proc/execdomains：安全相关的信息。
    /proc/fb：帧缓冲设备信息。
    /proc/iomem：记录物理地址的分配情况。
    /proc/kallsyms：内核符号表信息。
    /proc/pagetypeinfo：内存分页信息。
    /proc/partitions：分区信息
    /proc/sched_debug：cpu调度信息。
    /proc/softirqs：软中断情况。
    /proc/vmallocinfo：vmalloc内存分配信息。
    /proc/vmstat：统计虚拟内存信息。
    /proc/pid：显示进程相关的所有信息。
     */
}
