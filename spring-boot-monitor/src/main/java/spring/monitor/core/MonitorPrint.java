package spring.monitor.core;

import com.alibaba.fastjson.JSONObject;

public class MonitorPrint {

    public static void print(String longName, long start, long end, Object ret, Object...args) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("======== 开始监控 ========\r\n");
        stringBuffer.append("方法: " + longName + "\r\n");
        stringBuffer.append("传参: " + JSONObject.toJSONString(args) + "\r\n");
        stringBuffer.append("返回: " + JSONObject.toJSONString(ret) + "\r\n");
        stringBuffer.append("耗时: " + (end - start) + "\r\n");
        stringBuffer.append("======== 结束监控 ========\r\n");

        System.out.println(stringBuffer.toString());
    }

    public static void print(String longName, Exception e) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("======== 开始监控 ========\r\n");
        stringBuffer.append("方法: " + longName + "\r\n");
        stringBuffer.append("异常: " + e.getMessage() + "\r\n");
        stringBuffer.append("======== 结束监控 ========\r\n");


    }
}
