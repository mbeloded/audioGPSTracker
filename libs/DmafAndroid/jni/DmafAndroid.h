/*
 * DmafAndroid.h
 *
 *  Created on: Oct 15, 2014
 *      Author: viejodani
 */

#include <jni.h>

#ifndef DMAFANDROID_H_
#define DMAFANDROID_H_

typedef void(*dm_cb)(const char* trigger_, float time_, void* params_, void* args);

//#ifdef __cplusplus
extern "C"
{
//#endif
	// bridge to dmaf
	JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmAlloc(JNIEnv * env, jobject  obj);
	JNIEXPORT jboolean JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmInitialize(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring settingsFilepath_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmDeinitialize(JNIEnv * env, jobject  obj, jlong dmafPtr_);
	JNIEXPORT jfloat JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmGetCurrentTime(JNIEnv * env, jobject  obj, jlong dmafPtr_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmTell(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring trigger_, jlong parameters_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmTellTime(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring trigger_, jfloat time_, jlong parameters_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmLog(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring file_, jint line_, jstring message_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmProcess(JNIEnv * env, jobject  obj, jlong dmafPtr_, jfloat numSamples_, jint channels_, jfloatArray data_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmAddListener(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring trigger_, jstring methodName_, jlong args_);

	// bridge to dmDictionary
	JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_CreateDictionary(JNIEnv * env, jobject  obj);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_DestroyDictionary(JNIEnv * env, jobject  obj, jlong dict);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetStringValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jstring value);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetFloatValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jfloat value);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetIntValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jint value);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetVoidPtrValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jlong value);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetDictionaryValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jlong value);
	JNIEXPORT jstring JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetStringValue(JNIEnv * env, jobject  obj, jlong dict, jstring key);
	JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetIntValue(JNIEnv * env, jobject  obj, jlong dict, jstring key);
	JNIEXPORT jfloat JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetFloatValue(JNIEnv * env, jobject  obj, jlong dict, jstring key);
	JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetVoidPtrValue(JNIEnv * env, jobject  obj, jlong dict, jstring key);
	JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetDictionaryValue(JNIEnv * env, jobject  obj, jlong dict, jstring key);
	JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_Size(JNIEnv * env, jobject  obj, jlong dict);
	JNIEXPORT jstring JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_KeyAt(JNIEnv * env, jobject  obj, jlong dict, jint index);
	JNIEXPORT jboolean JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_KeyExists(JNIEnv * env, jobject  obj, jlong dict, jstring key);

	// bridge to dmParameters
	JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmCreateParameters(JNIEnv * env, jobject  obj, jint size_);
	JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetNumParameters(JNIEnv * env, jobject  obj, jlong ptr_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmSetStringParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_, jstring value_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmSetIntParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_, jint value_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmSetEnumParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_, jint value_);
	JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmSetFloatParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_, jfloat value_);
	JNIEXPORT jstring JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetStringParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_);
	JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetIntParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_);
	JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetEnumParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_);
	JNIEXPORT jfloat JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetFloatParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_);
//#ifdef __cplusplus
}
//#endif

#endif /* DMAFANDROID_H_ */
