/*
 * DmafAndroid.cpp
 *
 *  Created on: Oct 15, 2014
 *      Author: viejodani
 */
#include <map>
#include <stdlib.h>
#include "DmafAndroid.h"
#include "dmaf-core.h"

namespace
{
	struct cb_params
    {
        char *trigger_;
        JavaVM *vm;
        jobject jObj;
        jmethodID jMethod;
    };
	struct impl
	{
		DmafCore* dmaf;
		float** outputBuffer;
		float* outputBufferInterleaved;
		bool initialized;
	};
}

std::map<int, cb_params*> callbacks;

void baseCallback(const char* trigger_, float time_, DmParametersPOD params_, void* args_)
{
    int i = 0;
    for(std::map<int, cb_params*>::iterator it = callbacks.begin(); it != callbacks.end(); ++it)
    {
        if(strcmp(it->second->trigger_, trigger_) == 0)
        {
            break;
        }
        i++;
    }
    JNIEnv * g_env;
    int getEnvStat = callbacks[i]->vm->GetEnv((void **)&g_env, JNI_VERSION_1_6);
    if (getEnvStat == JNI_EDETACHED)
    {
		if (callbacks[i]->vm->AttachCurrentThread(&g_env, NULL) != 0)
		{
			//Failed to attach
		}
	}
    else if (getEnvStat == JNI_OK)
	{
		//
	}
    else if (getEnvStat == JNI_EVERSION)
	{
		// GetEnv: version not supported
	}
    jstring jTrigger = g_env->NewStringUTF(trigger_);
    g_env->CallVoidMethod(callbacks[i]->jObj, callbacks[i]->jMethod, jTrigger, time_, (jlong)params_, (jlong)args_);
}

char* ConvertJstring(JNIEnv *env, jstring str)
{
	const int len = env->GetStringUTFLength(str);
	char *strPtr = new char[len];
	env->GetStringUTFRegion(str, 0, len, strPtr);
	return strPtr;
}


JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmAlloc(JNIEnv * env, jobject  obj)
{
	impl *mImpl = new impl();
	mImpl->initialized = false;
	mImpl->dmaf = new DmafCore;
	mImpl->outputBuffer = new float*[2];
	for(int i = 0; i < 2; i++)
	{
		mImpl->outputBuffer[i] = new float[44100];
	}
	mImpl->outputBufferInterleaved = new float[2 * 44100];
	return (jlong)mImpl;
}

JNIEXPORT jboolean JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmInitialize(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring settingsFilepath_)
{
	DmDictionary params_;
	params_.setStringValue("settingsFile", ConvertJstring(env, settingsFilepath_));
	((impl*)dmafPtr_)->initialized = ((impl*)dmafPtr_)->dmaf->initialize(params_);
	return ((impl*)dmafPtr_)->initialized;
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmDeinitialize(JNIEnv * env, jobject  obj, jlong dmafPtr_)
{
	((impl*)dmafPtr_)->dmaf->deinitialize();
	for (int i = 0; i < 2; ++i) {
		delete [] ((impl*)dmafPtr_)->outputBuffer[i];
	}
	delete [] ((impl*)dmafPtr_)->outputBuffer;
	delete [] ((impl*)dmafPtr_)->outputBufferInterleaved;
	delete ((impl*)dmafPtr_)->dmaf;
	delete (impl*)dmafPtr_;
	dmafPtr_ = 0;
	for(std::map<int, cb_params*>::iterator it = callbacks.begin(); it != callbacks.end(); ++it)
	{
		delete it->second->trigger_;
		it->second->trigger_ = NULL;
		delete it->second;
		it->second = NULL;
	}
	callbacks.clear();
}

JNIEXPORT jfloat JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmGetCurrentTime(JNIEnv * env, jobject  obj, jlong dmafPtr_)
{
	return((impl*)dmafPtr_)->dmaf->getCurrentTime();
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmTell(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring trigger_, jlong parameters_)
{
	impl* implementation = ((impl*)dmafPtr_);
	DmParametersPOD paramsCasted = (DmParametersPOD)parameters_;
	implementation->dmaf->tell(ConvertJstring(env, trigger_), paramsCasted);
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmTellTime(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring trigger_, jfloat time_, jlong parameters_)
{
	impl* implementation = ((impl*)dmafPtr_);
	DmParametersPOD paramsCasted = (DmParametersPOD)parameters_;
	implementation->dmaf->tell(ConvertJstring(env, trigger_), time_, paramsCasted);
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmLog(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring file_, jint line_, jstring message_)
{
	((impl*)dmafPtr_)->dmaf->log(ConvertJstring(env, file_), line_, ConvertJstring(env, message_));
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmProcess(JNIEnv * env, jobject  obj, jlong dmafPtr_, jfloat numSamples_, jint channels_, jfloatArray data_)
{
	if(dmafPtr_ == 0)
		return;

	impl *mImpl = ((impl*) dmafPtr_);

	if(mImpl->initialized)
	{
		mImpl->dmaf->process(0, NULL, channels_, mImpl->outputBuffer, numSamples_);
		int count = 0;
		int i = 0, j = 0;
		for(i = 0; i < numSamples_; ++i)
		{
			for(j = 0; j < channels_; ++j)
			{
				mImpl->outputBufferInterleaved[count++] = mImpl->outputBuffer[j][i];
			}
		}

		int len = env->GetArrayLength(data_);
		env->SetFloatArrayRegion(data_, 0, len, mImpl->outputBufferInterleaved);
	}
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_Dmaf_DmAddListener(JNIEnv * env, jobject  obj, jlong dmafPtr_, jstring trigger_, jstring methodName_, jlong args_)
{
	impl *mImpl = ((impl*) dmafPtr_);
	if(mImpl->initialized)
	{
		char *trigger = ConvertJstring(env, trigger_);
		mImpl->dmaf->addListener(trigger, baseCallback, (void*) args_);

		int i = 0;
		for(std::map<int, cb_params*>::iterator it = callbacks.begin(); it != callbacks.end(); ++it)
		{
			if(strcmp(it->second->trigger_, trigger) == 0)
			{
				delete it->second->trigger_;
				it->second->trigger_ = NULL;
				delete it->second;
				it->second = NULL;
				break;
			}
			i++;
		}
		callbacks[i] = new cb_params();
		const int len = strlen(trigger) + 1;
		callbacks[i]->trigger_ = new char[len];
		strcpy(callbacks[i]->trigger_, trigger);
		env->GetJavaVM(&callbacks[i]->vm);
		callbacks[i]->jObj = env->NewGlobalRef(obj);
		jclass g_clazz = env->GetObjectClass(callbacks[i]->jObj);
		if (g_clazz != NULL)
		{
			callbacks[i]->jMethod = env->GetMethodID(g_clazz, ConvertJstring(env, methodName_), "(Ljava/lang/String;FJJ)V");
		}
	}
}

JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_CreateDictionary(JNIEnv * env, jobject  obj)
{
	return ((jlong) new DmDictionary());
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_DestroyDictionary(JNIEnv * env, jobject  obj, jlong dict)
{
	delete (DmDictionary*)dict;
	dict = 0;
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetStringValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jstring value)
{
	((DmDictionary*)dict)->setStringValue(ConvertJstring(env, key), ConvertJstring(env, value));
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetFloatValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jfloat value)
{
	((DmDictionary*)dict)->setFloatValue(ConvertJstring(env, key), value);
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetIntValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jint value)
{
	((DmDictionary*)dict)->setIntValue(ConvertJstring(env, key), value);
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetVoidPtrValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jlong value)
{
	((DmDictionary*)dict)->setVoidPtrValue(ConvertJstring(env, key), (void*)value);
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_SetDictionaryValue(JNIEnv * env, jobject  obj, jlong dict, jstring key, jlong value)
{
	((DmDictionary*)dict)->setVoidPtrValue(ConvertJstring(env, key), (DmDictionary*)value);
}

JNIEXPORT jstring JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetStringValue(JNIEnv * env, jobject  obj, jlong dict, jstring key)
{
	return env->NewStringUTF(((DmDictionary*)dict)->getStringValue(ConvertJstring(env, key)));
}

JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetIntValue(JNIEnv * env, jobject  obj, jlong dict, jstring key)
{
	return ((DmDictionary*)dict)->getIntValue(ConvertJstring(env, key));
}

JNIEXPORT jfloat JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetFloatValue(JNIEnv * env, jobject  obj, jlong dict, jstring key)
{
	return ((DmDictionary*)dict)->getFloatValue(ConvertJstring(env, key));
}

JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetVoidPtrValue(JNIEnv * env, jobject  obj, jlong dict, jstring key)
{
	return (jlong)((DmDictionary*)dict)->getVoidPtrValue(ConvertJstring(env, key));
}

JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_GetDictionaryValue(JNIEnv * env, jobject  obj, jlong dict, jstring key)
{
	return (jlong)&((DmDictionary*)dict)->getDictionaryValue(ConvertJstring(env, key));
}

JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_Size(JNIEnv * env, jobject  obj, jlong dict)
{
	return ((DmDictionary*)dict)->size();
}

JNIEXPORT jstring JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_KeyAt(JNIEnv * env, jobject  obj, jlong dict, jint index)
{
	return env->NewStringUTF(((DmDictionary*)dict)->keyAt(index));
}

JNIEXPORT jboolean JNICALL Java_com_dinahmoe_dmaf_DmDictiorary_KeyExists(JNIEnv * env, jobject  obj, jlong dict, jstring key)
{
	return ((DmDictionary*)dict)->keyExists(ConvertJstring(env, key)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jlong JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmCreateParameters(JNIEnv * env, jobject  obj, jint size_)
{
	return (jlong)DmParamsCreate(size_);
}

JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetNumParameters(JNIEnv * env, jobject  obj, jlong ptr_)
{
	return DmParamsGetNumParams((void*)ptr_);
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmSetStringParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_, jstring value_)
{
	DmParamsSetString((void*)ptr_, index_, ConvertJstring(env, value_));
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmSetIntParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_, jint value_)
{
	DmParamsSetInt((void*)ptr_, index_, value_);
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmSetEnumParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_, jint value_)
{
	DmParamsSetEnum((void*)ptr_, index_, value_);
}

JNIEXPORT void JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmSetFloatParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_, jfloat value_)
{
	DmParamsSetFloat((void*)ptr_, index_, value_);
}

JNIEXPORT jstring JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetStringParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_)
{
	return env->NewStringUTF(DmParamsGetString((void*)ptr_, index_));
}

JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetIntParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_)
{
	return DmParamsGetInt((void*)ptr_, index_);
}

JNIEXPORT jint JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetEnumParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_)
{
	return DmParamsGetEnum((void*)ptr_, index_);
}

JNIEXPORT jfloat JNICALL Java_com_dinahmoe_dmaf_DmParametersPOD_DmGetFloatParameter(JNIEnv * env, jobject  obj, jlong ptr_, jint index_)
{
	return DmParamsGetFloat((void*)ptr_, index_);
}


