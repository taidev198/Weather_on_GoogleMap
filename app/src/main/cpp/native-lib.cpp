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
extern "C" JNIEXPORT jstring
Java_com_taimar198_weatherongooglemap_utls_Methods_getFile(JNIEnv* env, jclass clazzthis) {
std::string api_key = "tao_khong_biet";
return env->NewStringUTF(api_key.c_str());
}
extern "C" JNIEXPORT jstring
Java_com_taimar198_weatherongooglemap_utls_Methods_getMapAPIKey(JNIEnv* env, jclass clazzthis) {
    std::string api_key = "e370756ec8af6d31ce5f25668bf0bee8";
    return env->NewStringUTF(api_key.c_str());
}
extern "C" JNIEXPORT jstring
Java_com_taimar198_weatherongooglemap_utls_Methods_getGEOAPIKey(JNIEnv* env, jclass clazzthis) {
    std::string api_key = "AIzaSyAqdRuDwUbXJTQ1WwdIIR6_F3k3etpb5Og";
    return env->NewStringUTF(api_key.c_str());
}

//JNIEXPORT jbyteArray JNICALL Java_com_manhnv_hidepassword_Secure_aes(
//        JNIEnv *env, jobject javaThis, jbyteArray jarray, jint jmode) {
//    //check input data
//    unsigned int len = (unsigned int) env->GetArrayLength(jarray);
//    if (len <= 0 || len >= MAX_LEN) {
//        return NULL;
//    }
//
//    unsigned char *data = (unsigned char*) env->GetByteArrayElements(jarray,
//                                                                     NULL);
//    if (!data) {
//        return NULL;
//    }
//
//    //(DESede/CBC/PKCS5Padding)
//    unsigned int mode = (unsigned int) jmode;
//    unsigned int rest_len = len % AES_BLOCK_SIZE;
//    unsigned int padding_len = (
//            (ENCRYPT == mode) ? (AES_BLOCK_SIZE - rest_len) : 0);
//    unsigned int src_len = len + padding_len;
//
//    unsigned char *input = (unsigned char *) malloc(src_len);
//    memset(input, 0, src_len);
//    memcpy(input, data, len);
//    if (padding_len > 0) {
//        memset(input + len, (unsigned char) padding_len, padding_len);
//    }
//
//    //env->ReleaseByteArrayElements(jarray, data, 0);
//
//    unsigned char * buff = (unsigned char*) malloc(src_len);
//    if (!buff) {
//        free(input);
//        return NULL;
//    }
//    memset(buff, src_len, 0);
//
//    //set key & iv
//    unsigned int key_schedule[AES_BLOCK_SIZE * 4] = { 0 }; //>=53(这里取64)
//    aes_key_setup(AES_KEY, key_schedule, AES_KEY_SIZE);
//
//    if (mode == ENCRYPT) {
//        aes_encrypt_cbc(input, src_len, buff, key_schedule, AES_KEY_SIZE,
//                        AES_IV);
//    } else {
//        aes_decrypt_cbc(input, src_len, buff, key_schedule, AES_KEY_SIZE,
//                        AES_IV);
//    }
//
//    if (ENCRYPT != mode) {
//        unsigned char * ptr = buff;
//        ptr += (src_len - 1);
//        padding_len = (unsigned int) *ptr;
//        if (padding_len > 0 && padding_len <= AES_BLOCK_SIZE) {
//            src_len -= padding_len;
//        }
//        ptr = NULL;
//    }
//
//    jbyteArray bytes = env->NewByteArray(src_len);
//    env->SetByteArrayRegion(bytes, 0, src_len, (jbyte*) buff);
//
//    free(input);
//    free(buff);
//
//    return bytes;
//}