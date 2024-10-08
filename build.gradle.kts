<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>net.glowstone</groupId>
    <artifactId>bukkit2sponge</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0-SNAPSHOT</version>
    <name>Bukkit2Sponge</name>
    <url>https://github.com/GlowstoneMC/Bukkit2Sponge</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <bukkit.version>1.21-R0.1-SNAPSHOT</bukkit.version>
        <sponge.version>6.1.0</sponge.version>
        <maven.build.timestamp.format>yyyyMMdd-HHmm</maven.build.timestamp.format>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <parent>
        <groupId>org.sonatype.oss</groupId>
        <artifactId>oss-parent</artifactId>
        <version>9</version>
        <relativePath></relativePath>
    </parent>

    <repositories>
        <repository>
            <id>glowstone-repo</id>
            <url>https://repo.papermc.io/</url>
        </repository>
        <repository>
            <id>sponge-repo</id>
            <url>https://repo.spongepowered.org/maven</url>
        </repository>
    </repositories>

    <distributionManagement>
        <repository>
            <id>glowstone-upstream</id>
            <url>https://repo.glowstone.net/content/repositories/releases/</url>
        </repository>
        <snapshotRepository>
            <id>glowstone-upstream</id>
            <url>https://repo.glowstone.net/content/repositories/snapshots/</url>
        </snapshotRepository>
    </distributionManagement>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.14.8</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>io.papermc</groupId>
            <artifactId>paper</artifactId>
            <version>${bukkit.version}</version>
            <type>jar</type>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.spongepowered</groupId>
            <artifactId>spongeapi</artifactId>
            <version>${sponge.version}</version>
            <type>jar</type>
        </dependency>
        <dependency>
            <groupId>com.google.inject</groupId>
            <artifactId>guice</artifactId>
            <version>4.0</version>
        </dependency>
        <dependency>
            <groupId>ninja.leaping.configurate</groupId>
            <artifactId>configurate-hocon</artifactId>
            <version>3.3</version>
        </dependency>
        <dependency>
            <groupId>org.ow2.asm</groupId>
            <artifactId>asm</artifactId>
            <version>5.0.3</version>
        </dependency>
        <dependency>
            <groupId>org.ow2.asm</groupId>
            <artifactId>asm-commons</artifactId>
            <version>5.0.3</version>
        </dependency>
    </dependencies>

    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.0.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <artifactSet>
                                <excludes>
                                    <exclude>junit:junit</exclude>
                                </excludes>
                            </artifactSet>
                            <relocations>
                                <!-- SpongeAPI library relocations - note: must match plugin relocations in ShinyClassLoader -->
                                <relocation>
                                    <pattern>com.google.common</pattern>
                                    <shadedPattern>net.glowstone.bukkit2sponge.libs.guava17.com.google.common
                                    </shadedPattern>
                                </relocation>
                                <relocation>
                                    <pattern>org.objectweb.asm</pattern>
                                    <shadedPattern>net.glowstone.bukkit2sponge.libs.asm5.org.objectweb.asm
                                    </shadedPattern>
                                </relocation>
                            </relocations>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.0.2</version>
                <configuration>
                    <archive>
                        <manifestEntries>
                            <Implementation-Title>${project.name}</Implementation-Title>
                            <Implementation-Version>${project.version}</Implementation-Version>
                            <Specification-Version>${sponge.version}</Specification-Version>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
