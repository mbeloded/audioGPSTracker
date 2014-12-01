LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS) 
LOCAL_MODULE := libdmaf_android 
LOCAL_SRC_FILES := libdmaf_android.a 
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)
include $(PREBUILT_STATIC_LIBRARY) 

include $(CLEAR_VARS)
LOCAL_CPPFLAGS  := -std=c++11
LOCAL_LDLIBS := -llog -lOpenSLES
LOCAL_MODULE    := DmafAndroid
LOCAL_SRC_FILES := DmafAndroid.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_WHOLE_STATIC_LIBRARIES := libdmaf_android

include $(BUILD_SHARED_LIBRARY)
