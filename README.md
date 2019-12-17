# JavaServerUtils
Java utils for convenience in development server app, especially using spring. Many libs such as javax.servlet-api, org.mongodb.bson, spring-data-redis, fastjson,jackson, msgpack, lombok, commons-lang3, org.slf4j  are involved and are depended.



## compile & install
```
mvn package install
```

## add dependency
in your pom.xml
```
<dependency>
    <groupId>com.github.rwsbillyang</groupId>
    <artifactId>serverUtils</artifactId>
    <version>1.0.0</version>
</dependency>
```

or build.gradle:
```
 compile 'com.github.rwsbillyang:serverUtils:1.0.0'
```
## class included
### FastDateUtil
format Timestamp, Date, DateTime,  parse String using org.apache.commons.lang3.time.FastDateFormat

### IPUtil
get address(province, city etc.) by querying `http://ip.taobao.com/service/getIpInfo.php` , and mutual convert between ip address and long number.

### JsonUtil
json util class using jackson

### RequestUtil
 use HttpServletRequest to check browser type , url/uri with query parameters, and ip support reverse proxy

### MiniProgramUtil
 Wechat mini program

### redis tool
redis serializer such as NullValue, ObjectId 





