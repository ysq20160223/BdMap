//
// Created by Administrator on 2017/4/1.
//

// NOTE : #include <jni.h> 不能换行
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include <string.h>

// ------------------------------------------------------------------ 输出 String
/*
 * Class:     com_jni_MainActivity
 * Method:    getStringFromNDK
 * Signature: ()Ljava/lang/String;
 */
jstring Java_utils_JniUtil_getStringFromNDK(JNIEnv *env, jobject obj) {

//    return (*(*env)).NewStringUTF(env, "getStringFromNDK");
    return (*env)->NewStringUTF(env, "get String From NDK");

}





