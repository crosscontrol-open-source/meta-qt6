require python3-pyside6.inc

DEPENDS += "qtbase clang-native python3-shiboken6-native"

OECMAKE_SOURCEPATH = "${S}/sources/shiboken6"

EXTRA_OECMAKE += "-DSHIBOKEN_BUILD_LIBS=ON \
                  -DPython_SOABI='cpython-${@ d.getVar('PYTHON_BASEVERSION').replace('.', '')}' \
                 "

EXTRA_OECMAKE:append:riscv32 = " -DPython_SOABI=cpython-313-riscv32-linux-musl"
# some 32bit arches do not have compiler provide 64bit atomics e.g.
# __atomic_load_8 resulting in configure errors like
LDFLAGS:append:riscv32 = " -latomic"

do_install:append() {
    # shiboken6.pc in package python3-shiboken6-dev contains reference to TMPDIR [buildpaths]
    sed -i ${D}${QT6_INSTALL_LIBDIR}/pkgconfig/shiboken6.pc \
        -e '/^python_/d' \
        -e 's|${RECIPE_SYSROOT}||'
}

FILES:${PN}-dev += "${prefix}/shiboken6/include"

SYSROOT_DIRS += "${prefix}/shiboken6/include"

BBCLASSEXTEND = "native nativesdk"
