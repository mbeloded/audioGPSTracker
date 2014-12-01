//
//  DmDictionary.hpp
//  Project
//
//  Created by Alessandro Saccoia on 7/29/13.
//
//

#ifndef Project_DmDictionary_hpp
#define Project_DmDictionary_hpp



class DmDictionary {
public:
  DmDictionary();
  ~DmDictionary();
  
  void setStringValue(const char* key, const char* value);
  void setFloatValue(const char* key, float value);
  void setIntValue(const char* key, int value);
  void setVoidPtrValue(const char* key, void* value);
  void setDictionaryValue(const char* key, DmDictionary& value);
  
  const char*  getStringValue(const char* key);
  int getIntValue(const char* key);
  float getFloatValue(const char* key);
  const void* getVoidPtrValue(const char* key);
  DmDictionary& getDictionaryValue(const char* key);
  
  int size();
  const char* keyAt(int index_);
  bool keyExists(const char* key);
private:
  void* pimpl;
};

#endif
