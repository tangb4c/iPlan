package me.plan.core;

import android.util.Log;

import java.util.Arrays;
import java.util.Formatter;

/**
 * Created by tangb4c on 2015/1/27.
 */
public class TLog {
    private static final String SEPARATOR = ",";
    public static void tag(){
        writeLog(Log.INFO, "enter this line");
    }
    public static void i(String fmt, Object...args){
        writeLog(Log.INFO, fmt, args);
    }
    public static void d(String fmt, Object...args){
        writeLog(Log.DEBUG, fmt, args);
    }
    public static void e(String fmt, Object...args){
        writeLog(Log.ERROR, fmt, args);
    }
    private static void writeLog(int logLevel, String fmt, Object...args) {
        String _tag = "iPlan", _stackinfo = null;
        StringBuilder logInfoStringBuilder = new StringBuilder(256);
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if(sts != null && sts.length > 4){
            StackTraceElement logStack = sts[4];
            //_tag = logStack.getFileName().split("\\.")[0];
            getLogInfo(logInfoStringBuilder, logStack);
        }
        String _msg = null;
        if(fmt != null) {
            Formatter f = new Formatter(logInfoStringBuilder);
            f.format(fmt, args);
            _msg = logInfoStringBuilder.toString();
        }else {
            _msg = new String();
        }
        Log.println(logLevel, _tag, _msg);
    }
    /**
     * 输出日志所包含的信息
     */
    public static void getLogInfo(StringBuilder logInfoStringBuilder,StackTraceElement stackTraceElement) {
        // 获取线程名
        String threadName = Thread.currentThread().getName();
        // 获取线程ID
        long threadID = Thread.currentThread().getId();
        // 获取文件名.即xxx.java
//        String fileName = stackTraceElement.getFileName();
        // 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取生日输出行数
        int lineNumber = stackTraceElement.getLineNumber();

        logInfoStringBuilder.append("[");
  /*      logInfoStringBuilder.append("threadID=" + threadID).append(SEPARATOR);
        logInfoStringBuilder.append("threadName=" + threadName).append(SEPARATOR);
        //logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
        //logInfoStringBuilder.append("className=" + className).append(SEPARATOR);
        logInfoStringBuilder.append("methodName=" + methodName).append(SEPARATOR);
        logInfoStringBuilder.append("lineNumber=" + lineNumber);*/
        logInfoStringBuilder.append(threadID).append(SEPARATOR);
        logInfoStringBuilder.append(threadName).append(SEPARATOR);
        //logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
        logInfoStringBuilder.append(className).append(SEPARATOR);
        logInfoStringBuilder.append(methodName).append(":");
        logInfoStringBuilder.append(lineNumber);
        logInfoStringBuilder.append("] ");
    }
    /**
     * 获取函数名称
     */
    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return null;
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }

            return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId()
                    + "): " + st.getFileName() + ":" + st.getLineNumber() + "]";
        }

        return null;
    }
}
