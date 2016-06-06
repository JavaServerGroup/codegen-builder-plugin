# codegen-builder-plugin

如果你正新开始一个服务器端API项目，使用的正是maven, Spring MVC技术，并使用JSON作为服务器返回的格式，那么codegen-builder-plugin将让你免费获得自动生成文档，自动生成Android客户端代码等功能。例子代码<a href="https://github.com/JavaServerGroup/CodeGenDemo" target="_blank">CodeGenDemo</a>

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
在项目pom.xml添加插件依赖。
* scanBasePackage是需要扫描的包名;
* projectName设置项目的名称，主要用户生成文档的标题和名字，
```xml
<build>
	<plugins>
	    <plugin>
	        <groupId>com.jtool</groupId>
	        <artifactId>codegen-builder-plugin</artifactId>
	        <version>${latest-releases}</version>
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
为controller添加注解。
```java
@CodeGenApi(name = "查找用户", description = "根据用户国家，年纪，身高，是否结婚等条件过滤查找用户")
@CodeGenRequest(SearchUserApiRequest.class)
@CodeGenResponse(SearchUserApiResponse.class)
@RequestMapping(value = "/searchUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
@ResponseBody
public String searchUser(SearchUserApiRequest searchUserApiRequest) throws ParamException, BackEndException {
    return JSON.toJSONString(searchUserApiResponse);
}
```

##第五步
为请求/返回pojo添加注解。
```java
public class SearchUserApiRequest {
    @NotNull
    @Size(min = 1, max = 20)
    @CodeGenField("用户所在国家")
    private String country;

    @NotNull
    @Min(0)
    @Max(120)
    @Digits(integer = 3, fraction = 0)
    @CodeGenField("年龄")
    private Integer age;

    @NotNull
    @Min(0)
    @Max(250)
    @Digits(integer = 3, fraction = 2)
    @CodeGenField("身高")
    private Double height;

    @CodeGenField("是否已婚, 0代表没结婚，1代表结婚")
    @AvailableValues(values={"0", "1"})
    private String isMarried;

    ...getter and setter and toString
}
```

##第六步
运行命令生成文档，文档输出在target下
```shell
com.jtool:codegen-builder-plugin:build
```
