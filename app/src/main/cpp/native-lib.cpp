//
// Created by traig on 1/9/2021.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring
Java_com_taimar198_weatherongooglemap_utls_Methods_stringFromJNI(JNIEnv* env, jclass clazzthis) {
    std::string api_key = "tao_khong_biet";
    return env->NewStringUTF(api_key.c_str());
}
