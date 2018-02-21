#!/bin/sh

echo "hello world"

# 定义变量
myText="hello1"
myNum=100

# 打印变量要用 ${} 否则是打印纯文本
echo ${myText}
echo ${myNum}

# 字符串运算符
# -z 字符串长度为0返回true
# -n 字符串长度不为0返回true
if [ -z ${myText} ]
then
    echo "null"
fi

if [ ! -z ${myText} ]
then
    echo "not null"
fi

if [ -n ${myText} ]
then
    echo "not null"
fi


# 四则运算
# 定义变量的时候“=”前后是不能有空格的，但是进行四则运算的时候运算符号前后一定要有空格，乘法的时候需要进行转义
a=3
b=5

val=`expr ${a} + ${b}`
echo "Result = $val"

val=`expr ${a} - ${b}`
echo "Result = $val"

val=`expr ${a} \* ${b}`
echo "Result = $val"

val=`expr ${a} / ${b}`
echo "Result = $val"


# == ， != 运算符
# if 语句 if [ ${a}==${b} ] , 每个变量或者操作符都要有空格
if [ ${a} == ${b} ]
then
    echo "a equal to b"
fi

if [ ${a} != ${b} ]
then
    echo "a not equal to b"
fi

if [ ${a} == ${b} ]
then
    echo "true"
else
    echo "false"
fi

# for 循环
for i in {1..5}
do
    echo ${i}
done

for i in 5 6 7
do
    echo ${i}
done

# 函数
sysout(){
    echo "hello world"
}

sysout

# 调用一个有参数的函数
testAdd(){
    num1=3
    num2=5
    return $((${num1} + ${num2}))
}
testAdd
resultNum=$?
echo ${resultNum}

# 函数接收参数
test2(){
    echo ${1}  #接收第一个参数
    echo ${2}  #接收第二个参数
    echo ${3}  #接收第三个参数
    echo ${#}  #接收到参数的个数
    echo ${*}  #接收到的所有参数
}

test2 aa bb cc

# 文件接收参数
echo ${0} # sh buildscript/demo.sh ，指这个文件名 buildscript/demo.sh
echo `dirname ${0}` # sh buildscript/demo.sh ，打印出 buildscript
echo ${1}
echo ${2}

# 路径相关
echo '----'
root_path=`dirname ${0}`
echo ${root_path}

root_path_whole=`pwd`${root_path}
echo ${root_path_whole}

root_path_pwd=`pwd`
echo ${root_path_pwd}

#mkdir -p ${root_path_pwd}/output/Build_AAA
#rm -rf ${root_path_pwd}/output/Build_AAA

${root_path_pwd}/gradlew --no-daemon clean assembleRelease -PBUILD_NUMBER=1 || exit -1