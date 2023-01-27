SUMMARY = "Membrane dart example test"
AUTHOR = "Jerel Unruh"
HOMEPAGE = "https://github.com/jerel/membrane_template"
BUGTRACKER = "https://github.com/jerel/membrane_template/issues"
SECTION = "general"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=47da47e9ceb852b136a5566db4ebec53"

DEPENDS += "\
   membrane-example-native \
"

RDEPENDS:${PN} += "\
   membrane-example \
   dart-sdk \
"

S = "${STAGING_LIBDIR_TARGET}/membrane"

PUBSPEC_APPNAME = "membrane_dart_example"
DART_APPLICATION_PATH = "dart_example"
DART_APPLICATION_INSTALL_PREFIX = "/dart"

DART_FILE_PATH = "bin"
DART_TARGET = "dart_example"

do_compile:prepend() {

   cd ${S}/${DART_APPLICATION_PATH}

   sed -i "s/name: dart_example/name: ${PUBSPEC_APPNAME}/g" pubspec.yaml
}

inherit dart-app
