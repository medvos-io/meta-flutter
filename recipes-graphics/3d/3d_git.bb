SUMMARY = "3D Sample"
DESCRIPTION = ""
AUTHOR = ""
HOMEPAGE = ""
BUGTRACKER = ""
SECTION = "graphics"
LICENSE = "CLOSED"

LIC_FILES_CHKSUM = ""

DEPENDS += "\
    compiler-rt \
    filament \
    filament-native \
    libcxx \
    glfw \
    vulkan-loader \
    "

REQUIRED_DISTRO_FEATURES = "opengl"

PV .= "+${SRCPV}"

SRCREV = "dcc5286c75156526f0e4c1d503674208e6fb356a"

SRC_URI = "\
    git://github.com/jwinarske/ic3d-lib.git;protocol=git;branch=main \
    "

S = "${WORKDIR}/git"

RUNTIME = "llvm"
TOOLCHAIN = "clang"
PREFERRED_PROVIDER:libgcc = "compiler-rt"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland', d)}"

PACKAGECONFIG[wayland] = "\
    -DIC3D_USE_WAYLAND=ON, \
    -DIC3D_USE_WAYLAND=OFF, \
    wayland-native wayland wayland-protocols libxkbcommon"

inherit cmake pkgconfig features_check

# target build pass
EXTRA_OECMAKE += " \
    -DIC3D_LIB_ONLY=OFF \
    -DIC3D_FILAMENT_SOURCE_DIR=${STAGING_DIR_TARGET} \
    -DIC3D_FILAMENT_DIR=${STAGING_DIR_NATIVE} \
    ${PACKAGECONFIG_CONFARGS} \
    "
