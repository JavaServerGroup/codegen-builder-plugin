# codegen-builder-plugin

##第一步
在项目pom.xml添加新的插件repository

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
