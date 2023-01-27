# Helper class for building Dart Application.
# Assumes that:
# - S is defined and points to source directory.
# - PUBSPEC_APPNAME is defined correctly.  This is the name value from pubspec.yml.
#

DEPENDS += " \
    ca-certificates-native \
    dart-sdk-native \
    ninja-native \
    unzip-native \
    "

B="${WORKDIR}/build"

DART_APP_DISABLE_NATIVE_PLUGINS ??= ""

DART_PUB_CMD ??= "get"

PUB_CACHE = "${WORKDIR}/pub_cache"
PUB_CACHE_ARCHIVE = "dart-pub-cache-${PUBSPEC_APPNAME}-${SRCREV}.tar.bz2"

DART_SDK = "${STAGING_DIR_NATIVE}/usr/share/dart-sdk"

DART_PREBUILD_CMD ??= ""
DART_APPLICATION_PATH ??= "."
DART_BUILD_ARGS ??= "${DART_SDK}/bin/snapshots/gen_kernel.dart.snapshot --platform ${DART_SDK}/lib/_internal/vm_platform_strong_product.dill --aot -Ddart.vm.product=true"
DART_APPLICATION_INSTALL_PREFIX ??= ""
DART_INSTALL_PREFIX = "${datadir}${DART_APPLICATION_INSTALL_PREFIX}/${PUBSPEC_APPNAME}"


#
# Archive Pub Cache
#

addtask archive_pub_cache before do_patch after do_unpack
do_archive_pub_cache[network] = "1"
do_archive_pub_cache[dirs] = "${WORKDIR} ${DL_DIR}"
do_archive_pub_cache[depends] += " \
    dart-sdk-native:do_populate_sysroot \
    pbzip2-native:do_populate_sysroot \
    tar-native:do_populate_sysroot \
    "
python do_archive_pub_cache() {

    import errno
    import multiprocessing
    from   bb.fetch2 import FetchError
    from   bb.fetch2 import runfetchcmd

    localfile = d.getVar("PUB_CACHE_ARCHIVE")
    localpath = os.path.join(d.getVar("DL_DIR"), localfile)
    
    if os.access(localpath, os.R_OK):
        return

    workdir = d.getVar("WORKDIR")
    pub_cache = d.getVar("PUB_CACHE")
    os.makedirs(pub_cache, exist_ok=True)

    dart_sdk = os.path.join(d.getVar("STAGING_DIR_NATIVE"), 'usr/share/dart-sdk')
    app_root = os.path.join(d.getVar("S"), d.getVar("DART_APPLICATION_PATH"))
    pub_cmd = d.getVar("DART_PUB_CMD")

    pub_cache_cmd = \
        'export PUB_CACHE=%s; ' \
        '%s/bin/dart pub get;' \
        '%s/bin/dart pub get --offline' % \
        (pub_cache, dart_sdk, dart_sdk)

    bb.note("Running %s in %s" % (pub_cache_cmd, app_root))
    runfetchcmd('%s' % (pub_cache_cmd), d, quiet=False, workdir=app_root)

    cp_cmd = \
        'mkdir -p %s/.project | true; ' \
        'cp -r .dart_tool %s/.project/ | true; ' \
        % (pub_cache, pub_cache)

    bb.note("Running %s in %s" % (cp_cmd, app_root))

    runfetchcmd('%s' % (cp_cmd), d, quiet=False, workdir=app_root)

    bb_number_threads = d.getVar("BB_NUMBER_THREADS", multiprocessing.cpu_count()).strip()
    pack_cmd = "tar -I \"pbzip2 -p%s\" -cf %s ./" % (bb_number_threads, localpath)

    bb.note("Running %s in %s" % (pack_cmd, pub_cache))
    runfetchcmd('%s' % (pack_cmd), d, quiet=False, workdir=pub_cache)

    if not os.path.exists(localpath):
        raise FetchError("The fetch command returned success for pub cache, but %s doesn't exist?!" % (localpath), localpath)

    if os.path.getsize(localpath) == 0:
        os.remove(localpath)
        raise FetchError("The fetch of %s resulted in a zero size file?! Deleting and failing since this isn't right." % (localpath), localpath)
}

#
# Restore Pub Cache
#

addtask restore_pub_cache before do_patch after do_archive_pub_cache
do_restore_pub_cache[dirs] = "${WORKDIR} ${DL_DIR}"
do_restore_pub_cache[depends] += " \
    pbzip2-native:do_populate_sysroot \
    tar-native:do_populate_sysroot \
    "
python do_restore_pub_cache() {

    import multiprocessing
    import shutil
    import subprocess
    from   bb.fetch2 import subprocess_setup
    from   bb.fetch2 import UnpackError
    
    localfile = d.getVar("PUB_CACHE_ARCHIVE")
    localpath = os.path.join(d.getVar("DL_DIR"), localfile)

    bb_number_threads = d.getVar("BB_NUMBER_THREADS", multiprocessing.cpu_count()).strip()
    cmd = 'pbzip2 -dc -p%s %s | tar x --no-same-owner -f -' % (bb_number_threads, localpath)
    unpackdir = d.getVar("PUB_CACHE")
    shutil.rmtree(unpackdir, ignore_errors=True)
    bb.utils.mkdirhier(unpackdir)
    path = d.getVar('PATH')
    if path: cmd = 'PATH=\"%s\" %s' % (path, cmd)
    bb.note("Running %s in %s" % (cmd, unpackdir))
    ret = subprocess.call(cmd, preexec_fn=subprocess_setup, shell=True, cwd=unpackdir)

    if ret != 0:
        raise UnpackError("Unpack command %s failed with return value %s" % (cmd, ret), localpath)

    # restore dart pub get artifacts
    app_root = os.path.join(d.getVar("S"), d.getVar("DART_APPLICATION_PATH"))
    cmd = \
        'mv .project/.dart_tool %s/ | true; ' % (app_root)
    bb.note("Running %s in %s" % (cmd, unpackdir))
    ret = subprocess.call(cmd, preexec_fn=subprocess_setup, shell=True, cwd=unpackdir)

    if ret != 0:
        raise UnpackError("Restore .dart_tool command %s failed with return value %s" % (cmd, ret), localpath)
}

#
# Build AOT
#
do_compile() {

    export PATH=${DART_SDK}/bin:$PATH
    export PUB_CACHE=${PUB_CACHE}
    export PKG_CONFIG_PATH=${STAGING_DIR_TARGET}/usr/lib/pkgconfig:${STAGING_DIR_TARGET}/usr/share/pkgconfig:${PKG_CONFIG_PATH}

    rm -rf "${B}" | true
    mkdir -p "${B}" | true

    bbnote `env`

    cd ${S}/${DART_APPLICATION_PATH}

    ${DART_PREBUILD_CMD}

    bbnote "Compile kernel for AOT: Starting"

    DART_APP_AOT_CMD="${DART_SDK}/bin/dart \
        ${DART_BUILD_ARGS} \
        -o ${B}/${DART_TARGET}.dill \
        ${DART_FILE_PATH}/${DART_TARGET}.dart"

    bbnote "${DART_APP_AOT_CMD}"

    $DART_APP_AOT_CMD

    bbnote "Compile kernel for AOT: Complete"


    bbnote "gen_snapshot AOT: Started"

    #
    # Create ${DART_TARGET}.aot
    #
    ${DART_SDK}/bin/utils/gen_snapshot \
        --deterministic \
        --snapshot_kind=app-aot-elf \
        --elf=${B}/${DART_TARGET}.aot \
        --strip \
        ${B}/${DART_TARGET}.dill

    bbnote "gen_snapshot AOT: Complete"
}

do_install() {

    install -d ${D}${bindir}/dart/${DART_INSTALL_PREFIX}

    cp ${B}/${DART_TARGET}.aot ${D}${bindir}/dart/${DART_INSTALL_PREFIX}
}

INSANE_SKIP:${PN} += " ldflags libdir dev-so"

RDEPENDS:${PN} = "dart-sdk"
