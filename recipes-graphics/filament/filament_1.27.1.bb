SUMMARY = "Lightweight 3D Render Engine"
DESCRIPTION = "Filament is a real-time physically based rendering engine for Android, iOS, Windows, Linux, macOS, and WebGL2"
AUTHOR = "Filament Authors"
HOMEPAGE = "https://github.com/google/filament"
BUGTRACKER = "https://github.com/google/filament/issues"
SECTION = "graphics"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += "\
    compiler-rt \
    libcxx \
    "

DEPENDS:class-target += "\
    filament-native \
    libxkbcommon \
    "

PV .= "+${SRCPV}"

SRC_URI = "git://github.com/google/filament;protocol=https;branch=release \
           file://ImportExecutables-Release.cmake"

SRCREV = "867d4d44f5ef2a56f663f5d8a7ba984407adfcbe"

S = "${WORKDIR}/git"

RUNTIME:class-native = "llvm"
TOOLCHAIN:class-native = "clang"
PREFERRED_PROVIDER:class-native:libgcc = "compiler-rt"

RUNTIME:class-target = "llvm"
TOOLCHAIN:class-target = "clang"
PREFERRED_PROVIDER:class-target:libgcc = "compiler-rt"

PACKAGECONFIG:class-target ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland opengl vulkan', d)} samples"

PACKAGECONFIG[opengl] = "-DFILAMENT_SUPPORTS_OPENGL=ON, -DFILAMENT_SUPPORTS_OPENGL=OFF, virtual/egl"
PACKAGECONFIG[vulkan] = "-DFILAMENT_SUPPORTS_VULKAN=ON, -DFILAMENT_SUPPORTS_VULKAN=OFF, vulkan-loader"
PACKAGECONFIG[wayland] = "-DFILAMENT_SUPPORTS_WAYLAND=ON, -DFILAMENT_SUPPORTS_WAYLAND=OFF, wayland wayland-native wayland-protocols"
PACKAGECONFIG[x11] = "-DFILAMENT_SUPPORTS_X11=ON, -DFILAMENT_SUPPORTS_X11=OFF, libxcb libx11 libxi libxrandr"
PACKAGECONFIG[mobile] = "-DFILAMENT_LINUX_IS_MOBILE=ON, -DFILAMENT_LINUX_IS_MOBILE=OFF"
PACKAGECONFIG[samples] = "-DFILAMENT_SKIP_SAMPLES=OFF, -DFILAMENT_SKIP_SAMPLES=ON"

inherit cmake pkgconfig

EXTRA_OECMAKE:class-native += " \
    -D CMAKE_BUILD_TYPE=Release \
    -D FILAMENT_BUILD_FILAMAT=OFF \
    -D FILAMENT_SKIP_SAMPLES=ON \
    -D FILAMENT_SKIP_SDL2=ON \
    ${PACKAGECONFIG_CONFARGS} \
    "

EXTRA_OECMAKE:class-target += " \
    -D CMAKE_BUILD_TYPE=Release \
    -D FILAMENT_HOST_TOOLS_ROOT=${STAGING_BINDIR_NATIVE} \
    -D IMPORT_EXECUTABLES_DIR=. \
    -D DIST_ARCH=${BUILD_ARCH} \
    ${PACKAGECONFIG_CONFARGS} \
    "

do_configure:prepend:class-target () {
    cp ${WORKDIR}/ImportExecutables-Release.cmake ${S}
}

do_install:append:class-target () {
    mv ${D}${libdir}/*/*.a ${D}${libdir}
    rm -rf ${D}${libdir}/${BUILD_ARCH}

    rm -rf ${D}/usr/docs

    rm ${D}/usr/LICENSE
    rm ${D}/usr/README.md
}

FILES:${PN}-staticdev = "${libdir}"

BBCLASSEXTEND += "native nativesdk"
