SUMMARY = "Lightweight 3D Render Engine"
DESCRIPTION = "Filament is a real-time physically based rendering engine for Android, iOS, Windows, Linux, macOS, and WebGL2"
AUTHOR = "Filament Authors"
HOMEPAGE = "https://github.com/google/filament"
BUGTRACKER = "https://github.com/google/filament/issues"
SECTION = "graphics"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS:class-native += "\
    compiler-rt-native \
    libcxx-native \
    "

DEPENDS:class-target += "\
    compiler-rt \
    filament-native \
    libcxx \
    virtual/egl \
    "

REQUIRED_DISTRO_FEATURES = "opengl"

PV .= "+${SRCPV}"

SRC_URI = "git://github.com/google/filament;protocol=https;branch=release \
           file://0001-Wayland-FilamentApp.patch \
           file://0002-Remove-unused-code.patch \
           file://0003-Size-callback-prototype.patch \
           file://ImportExecutables-Release.cmake"

SRCREV = "867d4d44f5ef2a56f663f5d8a7ba984407adfcbe"

S = "${WORKDIR}/git"

RUNTIME:class-native = "llvm"
TOOLCHAIN:class-native = "clang"
PREFERRED_PROVIDER:class-native:libgcc = "compiler-rt"

RUNTIME:class-target = "llvm"
TOOLCHAIN:class-target = "clang"
PREFERRED_PROVIDER:class-target:libgcc = "compiler-rt"

PACKAGECONFIG:class-target ??= "${@bb.utils.filter('DISTRO_FEATURES', 'vulkan wayland', d)} samples"

PACKAGECONFIG[vulkan] = "-DFILAMENT_SUPPORTS_VULKAN=ON, -DFILAMENT_SUPPORTS_VULKAN=OFF, vulkan-loader"
PACKAGECONFIG[wayland] = "\
  -DFILAMENT_SUPPORTS_WAYLAND=ON -DFILAMENT_SUPPORTS_XCB=OFF -DFILAMENT_SUPPORTS_XLIB=OFF, \
  -DFILAMENT_SUPPORTS_WAYLAND=OFF, \
  wayland-native wayland wayland-protocols libxkbcommon"
PACKAGECONFIG[samples] = "-DFILAMENT_SKIP_SAMPLES=OFF, -DFILAMENT_SKIP_SAMPLES=ON"

inherit cmake pkgconfig features_check

# tool build pass
EXTRA_OECMAKE:class-native += " \
    -D CMAKE_BUILD_TYPE=Release \
    -D FILAMENT_BUILD_FILAMAT=OFF \
    -D FILAMENT_SKIP_SAMPLES=ON \
    -D FILAMENT_SKIP_SDL2=ON \
    -D FILAMENT_USE_SWIFTSHADER=OFF \
    -D FILAMENT_SUPPORTS_OPENGL=OFF \
    -D FILAMENT_SUPPORTS_EGL_ON_LINUX=OFF \
    -D FILAMENT_ENABLE_MATDBG=OFF \
    ${PACKAGECONFIG_CONFARGS} \
    "

# target build pass
EXTRA_OECMAKE:class-target += " \
    -D CMAKE_BUILD_TYPE=Release \
    -D IMPORT_EXECUTABLES_DIR=. \
    -D DIST_ARCH=${BUILD_ARCH} \
    -D FILAMENT_HOST_TOOLS_ROOT=${STAGING_BINDIR_NATIVE} \
    -D FILAMENT_USE_SWIFTSHADER=OFF \
    -D FILAMENT_SUPPORTS_OPENGL=OFF \
    -D FILAMENT_SUPPORTS_EGL_ON_LINUX=OFF \
    -D FILAMENT_ENABLE_MATDBG=OFF \
    ${PACKAGECONFIG_CONFARGS} \
    "

do_configure:prepend:class-target () {
    cp ${WORKDIR}/ImportExecutables-Release.cmake ${S}
}

do_install:append:class-target () {

    # static libs
    mv ${D}${libdir}/*/*.a ${D}${libdir}
    rm -rf ${D}${libdir}/${BUILD_ARCH}

    # user docs
    rm -rf ${D}/usr/docs

    # misc
    rm ${D}/usr/LICENSE
    rm ${D}/usr/README.md

    # install samples
    if ${@bb.utils.contains('PACKAGECONFIG', 'samples', 'true', 'false', d)}; then
        install -d ${D}${datadir}/filament/samples
        find ${B}/samples -executable -type f -exec cp {} ${D}${datadir}/filament/samples \;
    fi
}

FILES:${PN}-samples = "${datadir}"
FILES:${PN}-staticdev = "${libdir}"

BBCLASSEXTEND += "native nativesdk"
