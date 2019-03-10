#ADA 配置中心设计

###总体架构

![](https://github.com/longofsky/documents/blob/master/ADA/picture/ADA%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83%E8%AE%BE%E8%AE%A1.jpg?raw=true)

配置中心接入指南

####常规接入

#####常规配置需求接入

1. 引入sky-cfgcenter.jar

2. 添加JVM配置

   ```text
   -Dapp.id=AppId
   -Denv=DEV
   -Dfile_config_path=加载环境路径
   ```

   

3. application-spring.xml加入需要的namespaces，例：

   ```xml
   <apollo:config namespaces="test-namespace"/>
   ```

   

4. 注入BaseGenericConfig.class

   ```java
   @Autowired
   private BaseGenericConfig baseGenericConfig;
   ```

   

5. 根据key获取value 例：

   ```java
   String  value = baseGenericConfig.getProperty("key");
   ```

#####特殊配置需求接入--支持热更新

1. 添加ada-context 例：

   ```xml
   xmlns:ada-context="http://www.ada.com/schema/ada-context"
   http://www.ada.com/schema/ada-context http://www.ada.com/schema/ada-context-1.0.0.xsd
   ```

2. 添加配置 例：

   ```xml
   <ada-context:config files="config/ada-context-config.properties,ada-context.properties"/>
   ```

3. 根据key获取value 例：

   ```xml
   String  value = baseGenericConfig.getProperty("key");
   ```
   
####配置中心加载顺序

![](https://github.com/longofsky/documents/blob/master/ADA/picture/ADA%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83%E5%8A%A0%E8%BD%BD%E9%A1%BA%E5%BA%8F.jpg?raw=true)



####Apollo Meta Server加载逻辑

![](https://github.com/longofsky/documents/blob/master/ADA/picture/Apollo%20Meta%20Server%E5%9C%B0%E5%9D%80%E5%AE%9A%E4%BD%8D%E9%80%BB%E8%BE%91.jpg?raw=true)

```
AdaMetaServerProvider 加载 file_config_path下的apollo-env.properties
```

####ada-context加载逻辑

![](https://github.com/longofsky/documents/blob/master/ADA/picture/ada-context%E5%8A%A0%E8%BD%BD%E9%80%BB%E8%BE%91.jpg?raw=true)

