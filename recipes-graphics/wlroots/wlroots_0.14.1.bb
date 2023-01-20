LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7578fad101710ea2d289ff5411f1b818"

SRC_URI = "https://gitlab.freedesktop.org/wlroots/wlroots/-/archive/${PV}/wlroots-${PV}.tar.gz"
SRC_URI[md5sum] = "3fbe8f81d2ba59362f041940869ed1e2"

DEPENDS += "\
    libinput \
    libxkbcommon \
    pixman \
    seatd \
    wayland-native \
    wayland \
    virtual/egl \
"

REQUIRED_DISTRO_FEATURES = "wayland opengl"

S = "${WORKDIR}/${PN}-${PV}"

inherit meson pkgconfig distro_features_check

do_configure:prepend() {
    export WLR_BACKENDS="drm,libinput,wayland"
}