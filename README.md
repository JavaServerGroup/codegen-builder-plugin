# codegen-builder-plugin

##第一步
在项目pom.xml添加新的插件repository

```xml
<pluginRepositories>
	<pluginRepository>
		<id>jtool-mvn-repository</id>
		<url>https://raw.github.com/JavaServerGroup/jtool-mvn-repository/master/releases</url>
	</pluginRepository>
	<pluginRepository>
		<id>jtool-mvn-snapshots</id>
		<url>https://raw.github.com/JavaServerGroup/jtool-mvn-snapshots/master/snapshots</url>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</pluginRepository>
</pluginRepositories>
```

##第二步
在项目pom.xml添加插件依赖，其中scanBasePackage是需要扫描的包名;projectName设置项目的名称，主要用户生成文档的标题和名字，
```xml
<build>
	<plugins>
	    <plugin>
	        <groupId>com.jtool</groupId>
	        <artifactId>codegen-builder-plugin</artifactId>
	        <version>0.0.1</version>
	        <configuration>
	            <projectName>自动代码生成demo</projectName>
	            <scanBasePackage>com.jtool.codegendemo</scanBasePackage>
	        </configuration>
	    </plugin>
	</plugins>
</build>
```

##第三步
在项目pom.xml添加生成代码注解的依赖：
```xml
<dependencies>
	<dependency>
	    <groupId>com.jtool</groupId>
	    <artifactId>codegen-annotation</artifactId>
	    <version>0.0.1</version>
	</dependency>
</dependencies>
```

##第四步
为controller添加注解
```java
    @CodeGenApi(name = "查找用户", description = "根据用户国家，年纪，身高，是否结婚等条件过滤查找用户")
    @CodeGenRequest(SearchUserApiRequest.class)
    @CodeGenResponse(SearchUserApiResponse.class)
    @RequestMapping(value = "/searchUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String searchUser(@RequestParam(required = false) String name) throws ParamException, BackEndException {

        System.out.println("-----" + name);

        return "HI " + name;
    }
```
