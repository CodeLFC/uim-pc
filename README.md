
# 西风吹瘦马
&emsp;基于自研**ubtp**(udp binary translate protocol)协议开发的即时通信桌面版demo。

**一些特性：**
- **跨平台**。客户端使用优化过的swing编写的，swing的优化是比较麻烦的，所以我们只优化了部分窗体和控件，并暂时使用了表现并不算很好的substance界面优化库。swing的优化需要长期的工作，但因其具有跨平台特性，且使用java开发，所以被我们选择用于开发demo。
- **微服务**。后端部分使用微服务+集群方式。基于springCloud的用户微服务+即时通信注册中心微服务 + 即时通信服务集群。
- **二进制协议**。ubtp协议的目的是基于udp提供一个不可靠的高效二进制传输协议。我们希望在demo中实现p2p模式的实时音视频通信的开发，所以我暂时不打算关注消息是否真正送达。
- **开源**。我们将持续维护并提交相关代码。

## 开发西风吹瘦马的原因
- 在使用ubuntu的过程中，很难获得良好的通信体验，国外的通信软件都需要翻墙，QQ在ubuntu下的表现一言难尽。
- 很早就使用qq，对于开发一款即时通信软件有很强的内在动力驱使。
- 第三方的即时通信库都需要收费，而且超贵，比如融云等。
## 相关的项目地址
### 基础依赖
1. springBoot基础模板（spring-base）：https://github.com/CodeLFC/spring-base
2. swing界面库优化（ugui）：https://github.com/CodeLFC/ugui
3. ubtp协议的核心部分（ubtp-core）：https://github.com/CodeLFC/ubtp-core
4. ubtp协议的服务端部分（ubtp-im）：https://github.com/CodeLFC/ubtp-im
### 应用部分
1. **注册中心（uim-eureka）**：https://github.com/CodeLFC/uim-eureka
2. **用户微服务（ubtp-user）**：有服务器地址等重要信息，放在私有仓库，请联系获取
3. **ubtp即时通信注册中心微服务（ubtp-center）**：有服务器地址等重要信息，放在私有仓库，请联系获取
4. 基于ubtp协议开发的pc端demo（uim-pc）：https://github.com/CodeLFC/uim-pc
# 运行环境
- JDK版本>=11
- 缺少的maven依赖需要在**相关的项目地址**中下载安装到本地maven仓库
# 部分运行截图
![在这里插入图片描述](https://img-blog.csdnimg.cn/4db9d3ec0fec48c19d2655b8bc7c9e4e.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5LiA6LW35Y675bGx6aG255yL55yL,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/acee74f448184a2099e64e0aaf7b40b2.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5LiA6LW35Y675bGx6aG255yL55yL,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/b9d6e9fb9a2447d6934acd8f965e4e80.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5LiA6LW35Y675bGx6aG255yL55yL,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/da8d8f161e594ad391d9aaeec1783407.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5LiA6LW35Y675bGx6aG255yL55yL,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)

# 交流QQ群：532685220