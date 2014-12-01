//
//  DmfConfig.h
//  AVLibraries
//
//  Created by Alessandro Saccoia on 7/21/14.
//
//

#ifndef AVLibraries_DmfConfig_h
#define AVLibraries_DmfConfig_h

#define DMF_HAS_AUDIO 1
#define DMF_HAS_VIDEO 0

#if DMF_HAS_AUDIO
#include "AudioEngineConfig.hpp"
#endif

#endif
