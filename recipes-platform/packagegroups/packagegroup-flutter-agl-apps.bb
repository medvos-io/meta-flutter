SUMMARY = "Package of Flutter AGL Apps"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} += " \
    flutter-app-igalia-homescreen \
    flutter-app-pumped-fuel-ped \
"
