#! /bin/sh

# 更换 versionName 和 versionCode
# ${source_dir} ${version_name} ${version_code}
update_version(){
	echo  "update version, ${1} ${2} ${3}"

	gsed -i 's/\(versionName "\)[^"]*/\1'${2}'/g' ${1}/app/build.gradle
	gsed -i 's/\(versionCode \)[^$]*/\1'${3}'/g' ${1}/app/build.gradle
}

# 更换渠道号
# {source path} {channel number}
update_channel_number(){
    echo "update channel number ${2}"

    gsed -i 's/\(channel_id">\).*\(<\)/\1'${2}'\2/g' ${1}/app/src/main/res/values/strings.xml
}


#{source dir} {version name} {version code} {channel number}
source_dir=$1
version_name=$2
version_code=$3
channel_number=$4


echo "-----begin replace-----"

update_version ${source_dir} ${version_name} ${version_code}


if [ -z "$channel_number" ]
  then
     echo "is not the channel version"
else
     update_channel_number ${source_dir} ${channel_number}

     # 执行渠道特殊脚本
     channel_sh=${source_dir}/custom/${channel_number}/${channel_number}.sh
     if [ -f "$channel_sh" ]; then
     		sh ${channel_sh} ${source_dir} ${version_name} ${version_code}
     else
     		echo "not exist ${channel_sh}"
     fi
fi

echo "-----replace successfully-----"