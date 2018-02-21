#! /bin/sh

# 根据原代码路径构建APK安装包
# 同时将构建好的安装包拷贝到指定目录
# 可指定不同的构建类型

usage() {
    echo "usage: apk_builder [-s SOURCE_DIRECTORY]"
    echo "                   [-t TYPE]"
    echo "                   [-vn VERSION NAME]"
    echo "                   [-vc VERSION CODE]"
    echo "                   [-c CHANNEL_NUMBER]"
    echo "       apk_builder [-c]"
    echo
    echo "        -s,                 source code directory"
    echo "        -t,                 build type : release|test"
    echo "        -vn,                version name"
    echo "        -vc,                version code"
    echo "        -c,                 channel number"
}

if [ ${#} -lt 1 ]; then
    usage
    exit 1
fi

while getopts "s:t:m:c:n:" opt; do
    case ${opt} in
        s) source_dir=${OPTARG};;
        t) build_type=${OPTARG};;
        m) version_name=${OPTARG};;
        c) version_code=${OPTARG};;
        n) channel=${OPTARG};;
    esac
done

APK_BUILDER_FAILED="apk builder failed..."
APK_BUILDER_SUCCESS="apk builder success..."

echo "----- start build ------"

if [ -z ${source_dir} ]; then
    echo "${APK_BUILDER_FAILED} Please input the source code directory!"
    exit 1
fi

if [ -z ${build_type} ]; then
    echo "${APK_BUILDER_FAILED} Please input the build type!"
    exit 1
fi

if [ -z ${version_name} ]; then
    echo "${APK_BUILDER_FAILED} Please input the build app version name!"
    exit 1
fi

if [ -z ${version_code} ]; then
    echo "${APK_BUILDER_FAILED} Please input the build app version code!"
    exit 1
fi

source $(dirname $0)/build_utils.sh

# 生成文件路径
release_dir=${source_dir}/target
mapping_path=${release_dir}

# apk 文件名
apk_target="uid"
if [ -z ${channel} ]; then
    apk_target=uid_v${version_name}.${build_type}.apk
else
    apk_target=$(date +%Y%m%d)_uid_v${version_name}.${build_type}.${channel}.apk
fi

# apk 文件全路径
des_path=${release_dir}/${apk_target}
echo des_path = ${des_path}

## build general build version
build_general_version() {
	delete_dirs ${release_dir}
	create_dirs ${release_dir}
    process_version ${source_dir} ${version_name} ${version_code}
    build_app ${source_dir} ${build_type} ${des_path} ${mapping_path}
}

## build channel build version
build_channel_version() {
	echo "build Channel ${channel}"
	delete_dirs ${release_dir}
	create_dirs ${release_dir}
    process_version ${source_dir} ${version_name} ${version_code} ${channel}
    build_app ${source_dir} ${build_type} ${des_path} ${mapping_path} ${channel} ${version_name} ${version_code}
}

if [ -z ${channel} ]; then
	build_general_version
else
    build_channel_version
fi


#echo ${source_dir}
#echo ${build_type}
#echo ${app_version}
#echo ${channel}


