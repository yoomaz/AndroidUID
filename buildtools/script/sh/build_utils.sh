#! /bin/sh

APK_BUILDER_FAILED="apk builder failed..."
APK_BUILDER_SUCCESS="apk builder success..."

print_log() {
    # -ne 两个数不相等
  	if [ "${1}" -ne "0" ] ; then
  		echo "$APK_BUILDER_FAILED ${2} end with error"
       	exit 1
  	else
       	echo "$APK_BUILDER_SUCCESS ${2} end with success"
  	fi
}

# 删除文件夹
delete_dirs() {
	echo "delete dir ${1}"
	if [ -d "${1}" ] ; then
		rm -rf ${1}
	fi
}

# 创建文件夹
create_dirs() {
	echo "create dir ${1}"
	if [ -d "${1}" ] ; then
		echo "${1} exist, need not create"
	else
		mkdir -m 777 -p ${1}
	fi
}

#{src dir} {dest dir}
copy_file() {
	echo "begin copy target from ${1} to ${2}"
	if [ -f ${1} ] ; then
      	cp -f ${1} ${2}
      	print_log $? "copy"
  	else
      	echo "the target file is not exist"
  	fi
}

#{source dir} {build_type} {mapping_path} {channel} ${version_name}
copy_mapping() {
    echo $1 $2 $3
    mapping_dir=${1}/app/build/outputs/mapping/release
    if [ -d ${mapping_dir} ]; then
        if [ ! -d ${3} ]; then
            mkdir -p ${3}
        fi
        prefix=""
        if [ ${4} ]; then
            prefix=$(date +%Y%m%d)_uid_v${5}.${2}.${4}
        fi
        cd ${mapping_dir} && ls | while read ls_entry
        do
            if [ ! -f ${ls_entry} ]; then
                    continue
            fi
            echo "mapping dir has : ${ls_entry}"
            mapping_name=$(basename "${ls_entry}")
            echo "mapping file name : ${mapping_name}"

            if [ "${2}" = "release" ]; then
                file_name=${prefix}${mapping_name}
            else
                file_name=${2}_${mapping_name}
            fi
            cp -f ${ls_entry} ${3}/${file_name}
        done
    fi
}

# 修改版本号
#{source dir} {version name} {version code} {channel number}
process_version() {
	echo "begin replace.sh version info"
	bash -c "sh ${1}/buildtools/script/sh/replace.sh ${1} ${2} ${3} ${4}"
}

#{source dir} {build_type} {des_path} {mapping_path} {channel} ${version_name} ${version_code}
build_app()	{
  	echo "begin build app"

	if [ "${2}" = "release" ]; then
	    echo "begin build app -- gradle assembleRelease"
	    echo ${1}
		${1}/gradlew clean assembleRelease --info --stacktrace
	else
	    echo "begin build app -- gradle assembleDebug"
#		${1}/gradle clean assembleDebug --info --stacktrace
	fi

    # 复制文件并改名
 	copy_file ${1}/app/build/outputs/apk/uid.apk ${3}
 	copy_mapping ${1} ${2} ${4} ${5} ${6}
}