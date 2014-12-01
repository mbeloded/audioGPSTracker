//
//  DmParametersPOD.h
//  Project
//
//  Created by Alessandro Saccoia on 6/20/14.
//
//

#ifndef Project_DmParametersPOD_h
#define Project_DmParametersPOD_h

#include <stddef.h>

typedef void* DmParametersPOD;

extern "C" {
  DmParametersPOD DmParamsCreate(size_t size_ = 0);
  int DmParamsGetNumParams(DmParametersPOD ptr_);
  //Int,Bool,Float,Enum,String,VoidPtr
  int DmParamsGetParameterType(DmParametersPOD ptr_, size_t index_);

  void DmParamsSetString(DmParametersPOD ptr_, size_t index_, const char* value_);
  void DmParamsSetInt(DmParametersPOD ptr_, size_t index_, int value_);
  void DmParamsSetEnum(DmParametersPOD ptr_, size_t index_, unsigned int value_);
  void DmParamsSetFloat(DmParametersPOD ptr_, size_t index_, float value_);

  const char* DmParamsGetString(DmParametersPOD ptr_, size_t index_);
  int DmParamsGetInt(DmParametersPOD ptr_, size_t index_);
  unsigned int DmParamsGetEnum(DmParametersPOD ptr_, size_t index_);
  const float DmParamsGetFloat(DmParametersPOD ptr_, size_t index_);
}

#endif
