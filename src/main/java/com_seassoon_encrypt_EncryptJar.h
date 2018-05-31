/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_seassoon_encrypt_EncryptJar */

#ifndef _Included_com_seassoon_encrypt_EncryptJar
#define _Included_com_seassoon_encrypt_EncryptJar
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_seassoon_encrypt_EncryptJar
 * Method:    genEncKey
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_seassoon_encrypt_EncryptJar_genEncKey
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     com_seassoon_encrypt_EncryptJar
 * Method:    encrypt
 * Signature: ([B[B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_seassoon_encrypt_EncryptJar_encrypt
  (JNIEnv *, jobject, jbyteArray, jbyteArray, jbyteArray);

/*
 * Class:     com_seassoon_encrypt_EncryptJar
 * Method:    decrypt
 * Signature: ([B[B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_seassoon_encrypt_EncryptJar_decrypt
  (JNIEnv *, jobject, jbyteArray, jbyteArray, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif