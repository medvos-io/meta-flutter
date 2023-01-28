SUMMARY = "A udev record/replay tool"
HOMEPAGE = "https://github.com/peet0r/rinput.git"
ISSUE_TRACKER = "https://github.com/peet0r/rinput/issues"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"

DEPENDS += "\
    systemd \
"

REQUIRED_DISTRO_FEATURES = "systemd"

SRCREV = "81795ebadd65522f8e4d5426982d4406c4241a77"

SRC_URI += " \
    git://github.com/peet0r/rinput.git;protocol=https;branch=main;lfs=0 \
    crate://crates.io/anyhow/1.0.66 \
    crate://crates.io/atty/0.2.14 \
    crate://crates.io/autocfg/1.1.0 \
    crate://crates.io/bitflags/1.3.2 \
    crate://crates.io/bitvec/1.0.1 \
    crate://crates.io/bytes/1.3.0 \
    crate://crates.io/cc/1.0.77 \
    crate://crates.io/cfg-if/1.0.0 \
    crate://crates.io/clap/4.0.26 \
    crate://crates.io/clap_derive/4.0.21 \
    crate://crates.io/clap_lex/0.3.0 \
    crate://crates.io/console/0.15.2 \
    crate://crates.io/dialoguer/0.10.2 \
    crate://crates.io/encode_unicode/0.3.6 \
    crate://crates.io/evdev/0.12.0 \
    crate://crates.io/fastrand/1.8.0 \
    crate://crates.io/funty/2.0.0 \
    crate://crates.io/futures/0.3.25 \
    crate://crates.io/futures-channel/0.3.25 \
    crate://crates.io/futures-core/0.3.25 \
    crate://crates.io/futures-executor/0.3.25 \
    crate://crates.io/futures-io/0.3.25 \
    crate://crates.io/futures-macro/0.3.25 \
    crate://crates.io/futures-sink/0.3.25 \
    crate://crates.io/futures-task/0.3.25 \
    crate://crates.io/futures-util/0.3.25 \
    crate://crates.io/heck/0.4.0 \
    crate://crates.io/hermit-abi/0.1.19 \
    crate://crates.io/indicatif/0.17.2 \
    crate://crates.io/input/0.7.1 \
    crate://crates.io/input-sys/1.16.1 \
    crate://crates.io/instant/0.1.12 \
    crate://crates.io/itoa/1.0.4 \
    crate://crates.io/lazy_static/1.4.0 \
    crate://crates.io/libc/0.2.137 \
    crate://crates.io/libudev-sys/0.1.4 \
    crate://crates.io/lock_api/0.4.9 \
    crate://crates.io/log/0.4.17 \
    crate://crates.io/memchr/2.5.0 \
    crate://crates.io/memoffset/0.6.5 \
    crate://crates.io/mio/0.8.5 \
    crate://crates.io/nix/0.23.1 \
    crate://crates.io/num_cpus/1.14.0 \
    crate://crates.io/number_prefix/0.4.0 \
    crate://crates.io/once_cell/1.16.0 \
    crate://crates.io/os_str_bytes/6.4.1 \
    crate://crates.io/parking_lot/0.12.1 \
    crate://crates.io/parking_lot_core/0.9.5 \
    crate://crates.io/pin-project-lite/0.2.9 \
    crate://crates.io/pin-utils/0.1.0 \
    crate://crates.io/pkg-config/0.3.26 \
    crate://crates.io/portable-atomic/0.3.18 \
    crate://crates.io/proc-macro-error/1.0.4 \
    crate://crates.io/proc-macro-error-attr/1.0.4 \
    crate://crates.io/proc-macro2/1.0.47 \
    crate://crates.io/quote/1.0.21 \
    crate://crates.io/radium/0.7.0 \
    crate://crates.io/redox_syscall/0.2.16 \
    crate://crates.io/remove_dir_all/0.5.3 \
    crate://crates.io/ryu/1.0.11 \
    crate://crates.io/same-file/1.0.6 \
    crate://crates.io/scopeguard/1.1.0 \
    crate://crates.io/serde/1.0.147 \
    crate://crates.io/serde_derive/1.0.147 \
    crate://crates.io/serde_json/1.0.89 \
    crate://crates.io/signal-hook-registry/1.4.0 \
    crate://crates.io/slab/0.4.7 \
    crate://crates.io/smallvec/1.10.0 \
    crate://crates.io/socket2/0.4.7 \
    crate://crates.io/strsim/0.10.0 \
    crate://crates.io/syn/1.0.103 \
    crate://crates.io/tap/1.0.1 \
    crate://crates.io/tempfile/3.3.0 \
    crate://crates.io/termcolor/1.1.3 \
    crate://crates.io/terminal_size/0.1.17 \
    crate://crates.io/thiserror/1.0.37 \
    crate://crates.io/thiserror-impl/1.0.37 \
    crate://crates.io/tokio/1.23.0 \
    crate://crates.io/tokio-macros/1.8.2 \
    crate://crates.io/udev/0.6.3 \
    crate://crates.io/unicode-ident/1.0.5 \
    crate://crates.io/unicode-width/0.1.10 \
    crate://crates.io/version_check/0.9.4 \
    crate://crates.io/walkdir/2.3.2 \
    crate://crates.io/wasi/0.11.0+wasi-snapshot-preview1 \
    crate://crates.io/winapi/0.3.9 \
    crate://crates.io/winapi-i686-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi-util/0.1.5 \
    crate://crates.io/winapi-x86_64-pc-windows-gnu/0.4.0 \
    crate://crates.io/windows-sys/0.42.0 \
    crate://crates.io/windows_aarch64_gnullvm/0.42.0 \
    crate://crates.io/windows_aarch64_msvc/0.42.0 \
    crate://crates.io/windows_i686_gnu/0.42.0 \
    crate://crates.io/windows_i686_msvc/0.42.0 \
    crate://crates.io/windows_x86_64_gnu/0.42.0 \
    crate://crates.io/windows_x86_64_gnullvm/0.42.0 \
    crate://crates.io/windows_x86_64_msvc/0.42.0 \
    crate://crates.io/wyz/0.5.1 \
    crate://crates.io/zeroize/1.5.7 \
"

S = "${WORKDIR}/git"

inherit cargo features_check
