package utils;

// Created by root on 2017-04-06.


//
/*
    解决无法执行ndk -> toolchains -> llvm -> prebuilt -> darwin-x86_64 -> bin - clang
        : sudo spctl --master-disable

    1, cd ~/app/src/main/java (/Users/Apple/Desktop/github/android/BdMap/app/src/main/java)
        javah -d ../jni utils.JniUtil  // 生成.h 文件, 并保存

    2, 生成.so路径: ~/app/build/intermediates/cmake
 */

// /Users/Apple/Library/Android/sdk

import com.lib_sdk.utils.XLog;

public class JniUtil {

    static {
        try {
            System.loadLibrary("JniUtil");
        } catch (Exception e) {
            XLog.d(e);
        }
    }

    //
    public static native String getStringFromNDK();

}
