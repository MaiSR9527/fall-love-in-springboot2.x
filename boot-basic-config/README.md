---
layout: springboot
title: SpringBoot基本配置
date: 2021-05-10 9:10:12
categories: java
tags: SpringBoot
---

# 一、配置启动日志Banner

在启动SpringBoot应用的时候我们可以在日志输出中看到Spring的输出：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.3.3.RELEASE)
```

其实SpringBoot的自动配置功能是我们可以很轻松的就可以使用自己想用的Banner，例如在江湖广为流传的Banner：

>  佛祖保佑		永无BUG

其实想要配置是很简单的，在resources文件夹下创建一个banner.txt的文件，启动之后即可。如下，运行后控制台中就会出现一个绿色的佛祖了。这里贴一个在百度上找到的一个制作网站[ASCII Generator (network-science.de)](http://www.network-science.de/ascii/)

**banner.txt**

```
${AnsiColor.GREEN}    设置字体颜色
					_ooOoo_
				   o8888888o
				   88\" . \"88
				   (| -_- |)
				   O\\  =  /O
				____/`---'\\____
			  .'  \\|     |//  `.
			 /  \\\\|||  :  |||//  \\
			/  _||||| -:- |||||-  \\
			|   | \\\\\\  -  /// |   |
			| \\_|  ''\\---/''  |   |
			\\  .-\\__  `-`  ___/-. /
		  ___`. .'  /--.--\\  `. . __
	   .\"\" '<  `.___\\_<|>_/___.'  >'\"\".
	  | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |
	  \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /
 ======`-.____`-.___\\_____/___.-`____.-'======
                    `=---='
 ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
			佛祖保佑		永无BUG
${spring-boot.formatted-version}    springboot版本
```

在SpringBoot的配置文件(application.yml/application.properties)中可以配置Banner相关的信息，可见当我们不想把Banner文件的名字叫做banner.txt是，可以通过`spring.banner.location=classpath:xxx.txt`来指定，但是既然都约定好了，我们就按照这个约定来配置就好。还可以看到其实还可以使用图片Banner。

![](https://cdn.jsdelivr.net/gh/MaiSR9527/blog-pic/spring/boot-banner-1.png)

这里使用一个网络的动图：[01e8fa5965991ba8012193a3195e5a.gif (800×800) (zcool.cn)](https://img.zcool.cn/community/01e8fa5965991ba8012193a3195e5a.gif)

然后改名为banner.gif，运行SpringBoot应用，这时候打印出来的Banner会特别多，因为gif有很多帧组成，每一帧都会被打印出来。其实使用图片Banner并不美观。如下用字符打印的小人行走的图中的一帧。

```
  ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  ::::::::::::::::oooooooooooooooooooooooooooooooooooooooooooo::::::::::::::::
  ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  :::::::::::::::::                                          :::::::::::::::::
  :::::::::::::::::                                          :::::::::::::::::
  :::::::::::::::::                                          :::::::::::::::::
  :::::::::::::::::                                          :::::::::::::::::
  :::::::::::::::::                                          :::::::::::::::::
  :::::::::::::::::                                          :::::::::::::::::
  :::::::::::::::::                    #: ##:                :::::::::::::::::
  :::::::::::::::::                       *###.              :::::::::::::::::
  :::::::::::::::::                            #o            :::::::::::::::::
  :::::::::::::::::                                          :::::::::::::::::
  :::::::::::::::::&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&&&.                &&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&.                    &&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&&:                 .&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&&&&&&         &&&&&&&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&&&&*        .&&&&&&&&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&&&         :&&&&&&&&&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&&         .&&&&&&&&&&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&&          &&&&&&&&&&&&&&&&&&&&&&:::::::::::::::::
  :::::::::::::::::&&&&&&&&&o         &&&&&&&&&&&&&&&&&&&&&&&:::::::::::::::::
  ::::::::::::::::*&&&&&&&&&:         &&&&&&&&&&&&&&&&&&&&&&&:::::::::::::::::
  ::::::::::::::::.**********          **********************:::::::::::::::::
  :::::::::::::::::***********.         *********************:::::::::::::::::
  :::::::::::::::::**************          .*****************:::::::::::::::::
  :::::::::::::::::*****************             ************:::::::::::::::::
  :::::::::::::::::**********************              ******:::::::::::::::::
  :::::::::::::::::::::         ::....*::::::::...   ...::::::::::::::::::::::
  :::::::::::::::::::::         :::::::::::::::*........*:::::::::::::::::::::
  :::::::::::::::::::::         ::::::::::::::::.........:::::::::::::::::::::
  :::::::::::::::::::::         :::::::::::::::::.........::::::::::::::::::::
  ::::::::::::::::::::.        *:::::::::::::::::*........*:::::::::::::::::::
  ::::::::::::::::::::         :::::::::::::::::::.........:::::::::::::::::::
  :::::::::::::::::::*        .::::::::::::::::::::.........::::::::::::::::::
  :::::::::::::::::::         :::::::::::::::::::::*........*:::::::::::::::::
  ::::::::::::::::::.        *::::::::::::::::::::::.........:::::::::::::::::
  :::::::::::::::::*        .::::::::::::::::::::::::.........::::::::::::::::
  :::::::::::::::::         :::::::::::::::::::::::::.........::::::::::::::::
  :::::::::::::::*         ::::::::::::::::::::::::::..........:::::::::::::::
  ::::::::::::::          ::::::::::::::::::::::::::::.........*::::::::::::::
  :::::::::::::         .:::::::::::::::::::::::::::::..........::::::::::::::
  :::::::::::.         ::::::::::::::::::::::::::::::::.........::::::::::::::
  :::::::::::::::::::::::::::::::::::::::::::::::::::::.........*:::::::::::::
  ::::::o&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&::::::
  :::::::&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&::::::

```

SpringBoot中定义Banner打印的类`SpringApplicationBannerPrinter`，支持"gif", "jpg", "png"这三种图片，从getBanner方法中可以看到，如果配置了图片banner会先打印图片Banner，如果同时也配置了文字banner，会在图片banner打印完之后打印文字banner。

```java
class SpringApplicationBannerPrinter {
    static final String BANNER_LOCATION_PROPERTY = "spring.banner.location";
    static final String BANNER_IMAGE_LOCATION_PROPERTY = "spring.banner.image.location";
    static final String DEFAULT_BANNER_LOCATION = "banner.txt";
    static final String[] IMAGE_EXTENSION = new String[]{"gif", "jpg", "png"};
    
    ...
        
    private Banner getBanner(Environment environment) {
        SpringApplicationBannerPrinter.Banners banners = new SpringApplicationBannerPrinter.Banners();
        banners.addIfNotNull(this.getImageBanner(environment));
        banners.addIfNotNull(this.getTextBanner(environment));
        if (banners.hasAtLeastOneBanner()) {
            return banners;
        } else {
            return this.fallbackBanner != null ? this.fallbackBanner : DEFAULT_BANNER;
        }
    }
    ...
}
```

如果想要关闭banner的打印，只需要在SpringBoot配置文件中配置即可。

> spring:
>   main:
>     banner-mode: off   # console  log  off  三种配置选项

# 二、Web容器配置

## 2.1 替换默认的Web容器和简单的配置

SpringBoot中内嵌了Tomcat，所以我们是可以轻松的把SpringBoot应用打成Jar包，独立运行。同时我们也可以不适用Tomcat，使用updertow或者jetty。如下使用jetty。

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
		<!-- 使用undertow -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jetty</artifactId>
        </dependency>
		<!-- 使用undertow -->
<!--        <dependency>-->
<!--            <groupId>org.springframework.boot</groupId>-->
<!--            <artifactId>spring-boot-starter-undertow</artifactId>-->
<!--        </dependency>-->
```

关于http端口配置，`server.port`可以配置http端口，同时也有一些特殊值，例如0，-1。配置了0代表，每次启动都是随机端口，而-1则是表示不对外暴露端口，即禁止访问该web应用。

> **server.port=8080**
>
> 启动日志：
>
> INFO 888 --- [main] o.s.b.web.embedded.jetty.JettyWebServer  : Jetty started on port(s) 8000 (http/1.1) with context path '/'
>
>
>
> **server.port=0**
>
> 启动日志：
>
> INFO 9960 --- [main] o.s.b.web.embedded.jetty.JettyWebServer  : Jetty started on port(s) 5939 (http/1.1) with context path '/'
>
>
>
> server.port=-1
>
> 此时日志并没有显示端口，无法访问web应用

如果端口时固定时我们时可以通过，属性注入拿到启动端口，当我们配置了随机端口，则需要其他的手段拿到。

```java
@SpringBootApplication
public class BasicConfigApplication implements ApplicationListener<WebServerInitializedEvent> {

    private Logger log = LoggerFactory.getLogger(BasicConfigApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BasicConfigApplication.class, args);
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        log.info("随机启动端口：{}", event.getWebServer().getPort());
    }
}
```

**开启响应压缩**

默认是关闭的，通过`server.compression.enabled=true`开启，默认当响应大于2KB时，就会压缩传输，可以自主配置此值。默认支持以下这些相应类型进行压缩，当需要其他的类型，也可自主配置。

> "text/html", "text/xml", "text/plain", "text/css", "text/javascript", "application/javascript", "application/json", "application/xml"

![](https://cdn.jsdelivr.net/gh/MaiSR9527/blog-pic/spring/boot-gzip.png)

# 三、Tomcat日志配置

Tomcat日志分为两种日志：

* access log：访问日志，记录外部请求
* server log：服务器内部日志，记录服务内部运行的日志

```yaml
server:
  port: 8000
  compression:
    enabled: true
  tomcat:
    accesslog:
      # 开始访问日志
      enabled: true
      # 访问日志文件名称的时间格式化
      file-date-format: yyyyMMdd
      # 访问日志名称的前缀
      prefix: tomcat_access_log_
      # 访问日志名称的后缀
      suffix: .log
      # 访问日志的输出内容
      # %h 访问的客户端IP
      # %l 用户的身份
      # %u 用户名
      # %t 请求时间
      # %r 请求地址
      # %s 响应的状态码
      # %b 响应的大小
      pattern: %h %l %u %t \"%r\" %s %b
    # 访问日志的存放的路径
    basedir: access-log
logging:
  level:
    # com.msr.better 包下的日志级别设置为debug
    com.msr.better: debug
    # org.springframework 包下的日志级别设置为warn
    org.springframework: warn
```

配置：

![](https://cdn.jsdelivr.net/gh/MaiSR9527/blog-pic/spring/boot-tomcat-log.png)

# 四、Tomcat Https证书配置

**1、生成证书**

```shell
keytool -genkey -alias boot_https -keyalg RSA -keysize 2048 -keystore boot_https.p12 -validity 365
```

**2、把生成的证书放到resources下**

**3、在SpringBoot配置文件中配置**

```yaml
server:
  ssl:
    key-alias: boot_https
    key-store: classpath:boot_https.p12
    # 生成证书时输入的password
    key-store-password: 123456
```

启动SpringBoot应用之后，在日志中可以看到已经使用https了

```
INFO 7504 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8000 (https)
```

因为已经启用了Https，当使用Http请求应用时，会报错。这对于用户来说体验并不是很好，因此我们可以可以配置一下，将http装成

```
Bad Request
This combination of host and port requires TLS.
```

Tomcat配置http转发到https

```java
@Configuration
public class WebConfig {

    @Bean
    public TomcatServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory webServerFactory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection securityCollection = new SecurityCollection();
                securityCollection.addPattern("/*");
                securityConstraint.addCollection(securityCollection);
                context.addConstraint(securityConstraint);
            }
        };
        webServerFactory.addAdditionalTomcatConnectors(myConnectors());
        return webServerFactory;
    }

    private Connector myConnectors() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8000);
        return connector;
    }
}
```

再次启动应用就会打印出一个日志，8080端口时http协议，8000端口时https协议，上面的代码配置中，访问8080端口的会被重定向转发到8000端口。

```
INFO 3980 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8000 (https) 8080 (http) with context path ''
```

# 五、读取应用配置

读取应用的配置文件application.yml或application.properties，Spring Boot提供了三种方式，通用的Environment类，可以通过key-value方式获取到配置文件中的值。也可以通过@value注解，自动注入属性，还可以将一组属性自动注入到一个配置类。

1、Environment类

```java
@Component
public class EnvComponent {

    @Autowired
    private Environment environment;

    public int getPort() {
        Integer port = environment.getProperty("server.port", Integer.class);
        System.out.println(port);
        return port;
    }
}
```

Environment时SpringBoot中最早初始化的一个类，因此可以在Spring Boot应用中任何地方使用。

2、@value自动注入属性

```java
@GetMapping("/port")
public int getPort(@Value("${server.port}")Integer port) {
    return port;
}
```

@value并不能在任何Spring管理的Bean中使用，因为@value本身是通过`org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor`实现的，他是BeanPostProcessor接口的实现类，因此任何BeanPostProcessor和BeanFactoryPostProcessor的子类中都不能使用@value来注入属性，因为那时候@value还没有被处理。

3、@ConfigurationProperties注解

通常情况下，将一组同样类型的配置属性映射为一个类更加方便，比如服务器配置：

yaml：

```yaml
server:
  port: 9090
  context-path: /api
```

properties：

```properties
server.port=9090
server.context-path=/api
```

不过在使用@ConfigurationProperties注解时，需要加入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

在配置类中配置好，就可以获取直接用了。在处理ConfigurationProperties注解的类的时候，会自动将"-"或"_"去掉，换成Java命名规范，例如将context-path转成contextPath。

```java
@Configuration
@ConfigurationProperties("server")
public class ServerConfig {

    private Integer port;
    private String contextPath;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
```

ConfiguationProperties和value的功能差不多，在配置类中使用的属性都是同一组管理的，可以使用ConfiguationProperties注解更为方便。@value的优点时支持SpEL表达式，但事实SqEL表达式不易于调试和重构。

# 六、自动装配

## 6.1 @Configutation注解和@Bean注解

Spring的Java配置的核心就是可以使用@Configuration作用在类上，并在联合在该类里面一个或多个的@Bean注解修饰的方法，声明Spring管理的Bean。@Configuration修饰的类相当于以前的一个XML配置文件，@Bean注解对应XML里面的bean标签。

```java
@Configuration
public class MyConfig {
    @Bean
    public Student student(){
        Student student = new Student();
        student.setName = "tom";
        student.setAge = 20;
        return student;
    }
}
```

上面代码中的MyConfig类使用了@Configuration注解修饰，向Spring表明该类是个配饰类，类里面带有@Bean注解的方法都会被Spring调用，但会对象作为一个Spring容器管理的Bean。@Bean还可以通过它的name属性设置Bean的名称，默认时方法名。配置类可以获取外部属性和注入其他的Bean。

## 6.2 Bean条件装配

在Spring Boot中还可以通过指定当前Spring容器中是否含有另外一个Bean来决定该Bean是否配置。使用@ConditionalOnBean，在当前上下文中是否存在某个对象时，才会实例化一个Bean。使用@ConditonalOnMissingBean，在当前上下文中不存在某个对象时，才会去实例化一个Bean。

@ConditionalOnBean的使用场景是一个Bean依赖与另外一个Bean，所以另外一个Bean必须得存在，才能实例化。例如，一个数据库数据连接池需要数据源的Bean已经装载了，才去创建数据库连接池的配置。

```java
@Configuration
@ConditionalOnBean(DataSource.class)
public class DataSourcePoolConfig{
    
}
```

@ConditonalOnMissBean的使用场景，我需要一个Ban，如果不存在，那就去初始化出来。例如：当前上下文环境不存在数据源Bean，那就调用druidDataSource方法初始化一个出来。

```java
@Configuration
public class DataSourceConfig{
    @Bean
    @ConditonalOnMissingBean(DataSource.class)
    public DataSource druidDataSource(){
        ...
    }
}
```

## 6.3 Class条件装配

Class条件装配是按照某个类是否在Classpath中来决定是否配置Bean。@ConditionalOnClass当给定的类名在类路径上存在，则实例化当前Bean。@ConditionalOnMissingClass 当给定的类名在类路径上不存在，则实例化当前Bean。例如以下是SpringBoot中数据源的自动配置的类。数据源自动注入的条件之一就是当前类路径下存在DataSource.class类。

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
...
public class DataSourceAutoConfiguration {
	...
}
```

## 6.4 Environment装配

可以根据SpringBoot的Environment属性来决定配置是否生效。例如在Spring Boot配置文件中配置`datasource.multiple.enable `来决定是否开启动态数据源，如果没有指定havingValue，只要属性不为false，配置都生效。

matchIfMissing表示配置文件中没有配置`datasource.multiple.enable `，配置也能生效，matchIfMissing默认值是false。

```
@ConditionalOnProperty(name = "datasource.dynamic.enable",havingValue = "true",matchIfMissing = true)
public class DynamicDataSourceConfig{
   ...
}
```

## 6.5 其他条件装配

@ConditionalOnExpression，当表达式为true时，才会实例化一个Bean，支持SqEL表达式，比如根据配置文件中的某个值来决定配置是否生效。

@ConditionalOnJava，当存在指定的Java版本的时候。

> @ConditionalOnJava(range = ConditionalOnJava.Range.EQUAL_OR_NEWER,value = JavaVersion.EIGHT)

## 6.6 联合多个条件装配

前面所说的各种条件装配是可以联合使用的。例如`org.springframework.boot.autoconfigure.cache`包下的Redis缓存的配置就是多个条件装配。

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(RedisConnectionFactory.class)
@ConditionalOnMissingBean(CacheManager.class)
@Conditional(CacheCondition.class)
class RedisCacheConfiguration {
   ...
}
```

## 6.7 Condition接口

当SpringBoot内置的ConditionalOnBean、ConditionalOnMissingBean等无法满足需求时，可以自建构建一个Condition实现，使用@COnditional注解来引用自己的实现。Spring中关于Condition接口的：

```java
@FunctionalInterface
public interface Condition {
    boolean matches(ConditionContext var1, AnnotatedTypeMetadata var2);
}
```

ConditionContext类可以得到用于帮助条件判断的辅助类，例如Environment，读取系统属性、环境变量、配置参数等作为条件判断；ResourceLoader，一个Spring类，用于加载和判断资源文件，比如某个配置文件存在时配置才生效；ConfigurationListableBeanFactory，Spring容器。

一下配置了一个对消息进行加密的类，存在message.txt资源文件并且`message.encrypt.enable`生效。

```java
@Configuration
public class EncryptConfig {
    @Bean
    @Conditional(MessageEncryptCondition.class)
    public MessageEncryptBean messageEncryptBean() {
        return new MessageEncryptBean();
    }

    static class MessageEncryptCondition implements Condition {
        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            Resource resource = conditionContext.getResourceLoader().getResource("message.txt");
            Environment environment = conditionContext.getEnvironment();
            return resource.exists() && environment.containsProperty("message.encrypt.enable");
        }
    }
}
```

