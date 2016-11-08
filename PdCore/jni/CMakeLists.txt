cmake_minimum_required(VERSION 3.6)

set(LIBPD ${CMAKE_CURRENT_LIST_DIR}/libpd)
set(PD ${LIBPD}/pure-data/src)
set(LIBPD_WRAPPER ${LIBPD}/libpd_wrapper)

include_directories(${PD})
include_directories(${LIBPD_WRAPPER})
include_directories(${LIBPD_WRAPPER}/util)
include_directories(${LIBPD}/jni)

add_library(pd SHARED
  ${PD}/d_arithmetic.c
  ${PD}/d_array.c
  ${PD}/d_ctl.c
  ${PD}/d_dac.c
  ${PD}/d_delay.c
  ${PD}/d_fft.c
  ${PD}/d_fft_fftsg.c
  ${PD}/d_filter.c
  ${PD}/d_global.c
  ${PD}/d_math.c
  ${PD}/d_misc.c ${PD}/d_osc.c
  ${PD}/d_resample.c
  ${PD}/d_soundfile.c
  ${PD}/d_ugen.c
  ${PD}/g_all_guis.c
  ${PD}/g_array.c
  ${PD}/g_bang.c
  ${PD}/g_canvas.c
  ${PD}/g_clone.c
  ${PD}/g_editor.c
  ${PD}/g_graph.c
  ${PD}/g_guiconnect.c
  ${PD}/g_hdial.c
  ${PD}/g_hslider.c
  ${PD}/g_io.c
  ${PD}/g_mycanvas.c
  ${PD}/g_numbox.c
  ${PD}/g_readwrite.c
  ${PD}/g_rtext.c
  ${PD}/g_scalar.c
  ${PD}/g_template.c
  ${PD}/g_text.c
  ${PD}/g_toggle.c
  ${PD}/g_traversal.c
  ${PD}/g_vdial.c
  ${PD}/g_vslider.c
  ${PD}/g_vumeter.c
  ${PD}/m_atom.c
  ${PD}/m_binbuf.c
  ${PD}/m_class.c
  ${PD}/m_conf.c
  ${PD}/m_glob.c
  ${PD}/m_memory.c
  ${PD}/m_obj.c
  ${PD}/m_pd.c
  ${PD}/m_sched.c
  ${PD}/s_audio.c
  ${PD}/s_audio_dummy.c
  ${PD}/s_file.c
  ${PD}/s_inter.c
  ${PD}/s_loader.c
  ${PD}/s_main.c
  ${PD}/s_path.c
  ${PD}/s_print.c
  ${PD}/s_utf8.c
  ${PD}/x_acoustics.c
  ${PD}/x_arithmetic.c
  ${PD}/x_connective.c
  ${PD}/x_gui.c
  ${PD}/x_list.c
  ${PD}/x_midi.c
  ${PD}/x_misc.c
  ${PD}/x_net.c
  ${PD}/x_array.c
  ${PD}/x_time.c
  ${PD}/x_interface.c
  ${PD}/x_scalar.c
  ${PD}/x_text.c
  ${PD}/x_vexp.c
  ${PD}/x_vexp_if.c
  ${PD}/x_vexp_fun.c
  ${LIBPD_WRAPPER}/s_libpdmidi.c
  ${LIBPD_WRAPPER}/x_libpdreceive.c
  ${LIBPD_WRAPPER}/z_libpd.c
  ${LIBPD_WRAPPER}/util/ringbuffer.c
  ${LIBPD_WRAPPER}/util/z_queued.c
  ${LIBPD_WRAPPER}/z_hooks.c
  ${LIBPD_WRAPPER}/util/z_print_util.c
  )

set(CMAKE_C_FLAGS "-DPD -DHAVE_UNISTD_H -DHAVE_LIBDL -DUSEAPI_DUMMY -w")

add_library(pdnative SHARED ${LIBPD}/jni/z_jni_plain.c)
target_link_libraries(pdnative pd)

add_library(pdnativeopensl SHARED ${LIBPD}/jni/opensl_stream/opensl_stream.c
  ${LIBPD}/jni/z_jni_opensl.c)
target_link_libraries(pdnativeopensl pd log OpenSLES)

add_library(sigmund_tilde SHARED ${LIBPD}/pure-data/extra/sigmund~/sigmund~.c)
target_link_libraries(sigmund_tilde pd)