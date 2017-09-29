# AndroidUID
android 设备使用 IMEI 或者 deviceID 可能存在需要权限或者获取为空的情况，为了解决这个实现了这个生成uid库，卸载 app 重新安装也是相同的 uid



#### 使用方式

1. 在 Application 中进行初始化:

   ```
   UidPersistenceHelper.init(this, "demo_app");
   ```

2. 在任意地方调用下面代码获取用户 uid：

   ```
   String uid = UidPersistenceHelper.getUid();
   ```



即使卸载了 app ，再次安装获取到的也是同一个uid