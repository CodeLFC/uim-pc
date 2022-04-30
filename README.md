
# 西风吹瘦马
&emsp;基于**ubtp**(udp binary translate protocol)协议开发的即时通信桌面版demo。可以在ubuntu和windows下运行

**一些特性：**
- **跨平台**。客户端使用优化过的swing编写的，虽然几乎没人使用swing，但因其具有跨平台特性，且使用java开发，不用再对协议实现其他语言的c端sdk版本，且此demo仅用于演示，所以被我们选择用于开发demo。
- **微服务**。后端部分使用微服务+集群方式。基于springCloud的用户微服务+即时通信注册中心微服务 + 即时通信服务集群。
- **二进制协议**。ubtp协议的目的是基于udp提供一个不可靠的高效二进制传输协议。我们希望在demo中实现p2p模式的实时音视频通信的开发。
## 开发西风吹瘦马的原因
- 个人爱好
- 项目实战

## 项目架构图

- 微服务方式可以简单的实现业务的水平扩展与纵向更新
- 即时通信采用类微服务形式可根据需求弹性部署
- 网关部分暂时没做，目前各个微服务是暴露状态，后面会更新的

![架构图](https://img-blog.csdnimg.cn/13b9f11a7a3a4a3094cfbc8d3ded2185.png)
## 数据库设计

```sql
/*
SQLyog Community v13.1.7 (64 bit)
MySQL - 8.0.28 : Database - uim
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`uim` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `uim`;

/*Table structure for table `friend` */

DROP TABLE IF EXISTS `friend`;

CREATE TABLE `friend` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint DEFAULT '0' COMMENT '用户id',
  `friend_id` bigint DEFAULT '0' COMMENT '朋友id',
  `remark` varchar(40) DEFAULT NULL COMMENT '备注',
  `time` bigint DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `token` */

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `userid` bigint NOT NULL COMMENT '用户账号',
  `device` int NOT NULL COMMENT '设备类型码',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'token校验码',
  `validate_time` bigint DEFAULT NULL COMMENT '有效截至时间',
  PRIMARY KEY (`userid`,`device`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `u_server` */

DROP TABLE IF EXISTS `u_server`;

CREATE TABLE `u_server` (
  `id` int NOT NULL COMMENT '唯一id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注，注释',
  `host` varchar(1024) DEFAULT NULL COMMENT '域名地址',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  `port` int DEFAULT NULL COMMENT '端口号',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `user_auth` */

DROP TABLE IF EXISTS `user_auth`;

CREATE TABLE `user_auth` (
  `userid` bigint NOT NULL COMMENT '用户id标识',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'phone' COMMENT '登录类型',
  `account` varchar(255) DEFAULT NULL COMMENT '账号',
  `pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '凭证',
  `create_time` bigint DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint DEFAULT '0' COMMENT '更新时间'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户账号，如果需要正常使用即时通信服务，需满足：id>Integer.MAX_VALUE',
  `head_url` varchar(2083) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像地址',
  `nick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '西风吹瘦马新用户' COMMENT '昵称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '快乐p2p' COMMENT '备注',
  `gender` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'male' COMMENT '性别（male/female）',
  `birth` bigint DEFAULT '0' COMMENT '出生日期',
  `gps` varchar(255) DEFAULT NULL COMMENT 'gps坐标',
  `cell_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '电话',
  `wechat` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信号',
  `qq` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'qq号',
  `email` varchar(320) DEFAULT NULL COMMENT '邮箱',
  `visible` varchar(1000) DEFAULT NULL COMMENT '各个属性是否可见的json串',
  `create_time` bigint DEFAULT '0' COMMENT '信息创建时间',
  `update_time` bigint DEFAULT '0' COMMENT '信息更新时间',
  `ban_time` bigint DEFAULT '0' COMMENT '用户封禁到期时间',
  `status` int DEFAULT '0' COMMENT '用户状态',
  `vip` int DEFAULT '0' COMMENT 'vip',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2147483652 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `verify_code` */

DROP TABLE IF EXISTS `verify_code`;

CREATE TABLE `verify_code` (
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `code` varchar(10) DEFAULT NULL COMMENT '验证码',
  `type` varchar(20) DEFAULT NULL COMMENT '验证码类型',
  `validate_time` bigint DEFAULT NULL COMMENT '有效时间',
  `daily_count` int DEFAULT NULL COMMENT '当天发送验证码数量',
  PRIMARY KEY (`phone`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

```

## 相关的实现项目地址（Github）
### 基础依赖
1. [springBoot基础模板（spring-base）](https://github.com/CodeLFC/spring-base)
2. [swing界面库优化（ugui）](https://github.com/CodeLFC/ugui)
3. [ubtp协议的核心部分（ubtp-core）](https://github.com/CodeLFC/ubtp-core)
4. [ubtp协议的服务端部分（ubtp-im）](https://github.com/CodeLFC/ubtp-im) 。依赖项目1、项目3
### 应用部分
5. **[注册中心（uim-eureka）](https://github.com/CodeLFC/uim-eureka)**
6. **用户微服务（ubtp-user）**。依赖项目1
7. **ubtp即时通信注册中心微服务（ubtp-center）**。依赖项目1、项目3
### demo实现（java）
- [pc端demo（uim-pc）,可以下载源码编译使用](https://github.com/CodeLFC/uim-pc) 依赖项目2、项目3
# 运行环境
- JDK版本>=11
- 缺少的maven依赖需要在**相关的项目地址**中下载安装到本地maven仓库
# 部分demo运行示例
&emsp;所有的控件和窗口都是经过重绘的swing控件
**1. 登陆**![登陆页面](https://img-blog.csdnimg.cn/5043f531cc604997b13de4f3a94920ed.png)
**2. 注册**
![注册页面](https://img-blog.csdnimg.cn/63b2e0f4e9f54f29a6a172bd62d3ded5.png)
**3.修改密码**
![修改密码](https://img-blog.csdnimg.cn/7bab9095392b4077b89c6048e9fa7978.png)
**4.主页**
![主页](https://img-blog.csdnimg.cn/3638eeb0db084a0b844ed1de74797249.png)
**5.个人信息**
![用户信息](https://img-blog.csdnimg.cn/f9ca3e3807b74c1b87bfe81a5f655244.png)
**6.通信**
![测试](https://img-blog.csdnimg.cn/fe1308ca6ada43b1b522b42f8874e25e.png)
#
# 交流QQ群：532685220