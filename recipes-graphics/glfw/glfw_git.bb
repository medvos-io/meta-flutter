SUMMARY  = "A multi-platform library for OpenGL, OpenGL ES, Vulkan, window and input"
HOMEPAGE = "https://www.glfw.org/"
DESCRIPTION = "GLFW is an Open Source, multi-platform library for OpenGL, \
OpenGL ES and Vulkan application development. It provides a simple, \
platform-independent API for creating windows, contexts and surfaces, reading \
input, handling events, etc."
LICENSE  = "Zlib"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=98d93d1ddc537f9b9ea6def64e046b5f"
SECTION = "lib"

inherit pkgconfig cmake features_check

PV .= "+git${SRCPV}"
SRCREV = "dd8a678a66f1967372e5a5e3deac41ebf65ee127"
SRC_URI = "git://github.com/glfw/glfw.git;nobranch=1;protocol=https"

S = "${WORKDIR}/git"

EXTRA_OECMAKE += "\
    -DBUILD_SHARED_LIBS=ON \
    -DGLFW_BUILD_TESTS=OFF \
    -DGLFW_BUILD_DOCS=OFF \
    -DCMAKE_POSITION_INDEPENDENT_CODE=ON"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)} examples"

PACKAGECONFIG[wayland] = "\
    -DGLFW_BUILD_WAYLAND=ON -DGLFW_USE_WAYLAND=ON, \
    -DGLFW_BUILD_WAYLAND=OFF -DGLFW_USE_WAYLAND=OFF, \
    wayland-native wayland wayland-protocols libxkbcommon"
PACKAGECONFIG[x11] = "\
    -DGLFW_BUILD_X11=ON, \
    -DGLFW_BUILD_X11=OFF, \
    libxrandr libxinerama libxi libxcursor"
PACKAGECONFIG[examples] = "\
    -DGLFW_BUILD_EXAMPLES=ON, \
    -DGLFW_BUILD_EXAMPLES=OFF, \
    libpng libglu zlib"

REQUIRED_DISTRO_FEATURES = "opengl"

COMPATIBLE_HOST:libc-musl = "null"
