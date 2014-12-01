/** \file dmaf-core.h
 * This is the main interface file to the dmaf library.
 *
 * @author Alessandro Saccoia
 * @address
 */

#ifndef dmaf_core_hpp_
#define dmaf_core_hpp_

#include "DmfConfig.h"
#include "DmParametersPOD.h"
#include "DmDictionary.h"


/** Signature for the callback to be attached to some internal event.
 *  @warning The callback is notified on an internal dmaf thread. It's up to the user
 *           code to handle this situation by scheduling the commmand on the main thread
 *           if necessary.
 *  @param trigger_ The trigger that fired this event
 *  @param time_ The firing time of the event
 *  @param params_ The parameters associated with this event
 *  @param args_ User supplied void*, normal use is to cast it back to a DmafCore
 */
typedef void(*dmaf_cb_pod)(const char* trigger_, float time_, DmParametersPOD params_, void* args);

/** @brief     Main structure of the dmaf library
 *  @details   This class keeps the state of the audio engine.
 *  @author    Alessandro Saccoia
 *  @version   1.5.0
 *  @date      2012-2014
 *  @warning   Improper use can crash your application. The class is not reentrant and the calls can't
 *             arrive from different threads
 *  @copyright Copyright 2014 Dinahmoe Sweden
 */
struct DmafCore {
 /** Initializes the structure
  *  @param params_ One parameter of type string, with the path of the settings file
  *  @return Wether the initialization was successful or not
  */
  bool initialize(DmDictionary& params_);
  
 /** 
  *  Deinitializes the structure
  */
  void deinitialize();
  
 /**
  *  Returns the time passed from the initialization of the structure
  */
  float getCurrentTime();
  
 /** Method for interacting with dmaf. All the interaction from the user code
  *  to dmaf happens through this method.
  *
  * @param trigger_ The name of the event that one wants to fire.
  * @param params_ The parameters to be associated when firing this event.
  */
  void tell(const char* trigger_, DmParametersPOD params_ = NULL);
  
 /** Method for interacting with dmaf. Overloaded accepting time.
  *
  * @param trigger_ The name of the event that one wants to fire.
  * @param params_ The time execution time for this command
  * @param params_ The parameters to be associated when firing this event.
  */
  void tell(const char* trigger_, float time_, DmParametersPOD params_ = NULL);

 /** Adds a listener to a specific trigger
  *
  * @param trigger_ The name of the event to listen to
  * @param params_ A pointer to function that will be called when the event is fired
  * @param args_ The user supplied void* arguments will be provided to the callback
  * to be interpreted accordingly
  * @return A long integer to be used as a receipt when one wants to unsubscribe from
  * this particular trigger
  */
  long addListener(const char* trigger_, dmaf_cb_pod callback_, void* args_);
  
  /** Removes a listener from a specific trigger
  *
  * @param trigger_ The name of the event to listen to
  * @param The long integer that was returned by addListener when subscribing
  */
  void removeListener(const char* trigger_, long receipt_);
  
#if DMF_HAS_AUDIO
  #if DMF_USES_EXTERNAL_AUDIO
   /** Audio callback. Pushes input samples into the audio engine and retrieves the
    *  output samples.
    *
    * @param channelsIn_ The number of input channels
    * @param buffersIn_ Bidimensional array of non-interleaved audio samples, input
    * @param channelsOut_ The number of output channels
    * @param buffersOut_ Bidimensional array of non-interleaved audio samples, output
    * @param nSamples_ The number of samples for this callback
    * @return the number of samples processed. the user can assert nSamples_ == return value
    *
    * @warning The correct allocation of a number of samples equal to at least nSamples_
    *           is up to the user code.
    */
    int process(int channelsIn_, float** buffersIn_, int channelsOut_, float** buffersOut_, int nSamples_);
  #else
    void toggleRendering(bool startStop_);
  #endif
#endif

  /** Adds a log message in the dmaf logging system
  *
  * @param file_ The name of the current file, in gcc use __FILE__
  * @param line_ The number of the current line, in gcc use __LINE__
  * @param message_ The message to log
  */
  void log(const char *file_, int line_, const char* message_);
  
  /** Gets the dmaf version number as a string
  *
  * @return A string containing the version number of dmaf
  */
  static const char* getVersionString();
  
  void* pimpl;
};

#endif
