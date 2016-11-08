cmake_minimum_required(VERSION 3.6)

set(PD_CORE ${CMAKE_CURRENT_LIST_DIR}/../../PdCore)

add_library(pd SHARED IMPORTED)
set_target_properties(pd PROPERTIES IMPORTED_LOCATION
  ${PD_CORE}/build/intermediates/cmake/release/obj/${ANDROID_ABI}/libpd.so)

include_directories(${PD_CORE}/jni/libpd/pure-data/src)

add_library(helloworld SHARED helloworld.c)

set(CMAKE_C_FLAGS "-DPD")
target_link_libraries(helloworld pd)
