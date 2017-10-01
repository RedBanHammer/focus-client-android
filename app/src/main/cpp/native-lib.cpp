#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_edu_usc_csci310_focus_focus_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello";
    return env->NewStringUTF(hello.c_str());
}
