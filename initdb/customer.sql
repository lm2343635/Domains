/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 127.0.0.1:3306
 Source Schema         : customer

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 28/07/2019 12:54:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer_area
-- ----------------------------
DROP TABLE IF EXISTS `customer_area`;
CREATE TABLE `customer_area` (
  `aid` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `customers` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_assign
-- ----------------------------
DROP TABLE IF EXISTS `customer_assign`;
CREATE TABLE `customer_assign` (
  `aid` varchar(255) NOT NULL,
  `assign` bit(1) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `d` bit(1) NOT NULL,
  `r` bit(1) NOT NULL,
  `w` bit(1) NOT NULL,
  `cid` varchar(255) NOT NULL,
  `eid` varchar(255) NOT NULL,
  PRIMARY KEY (`aid`),
  KEY `FK_drmljo5ocl2fsm2nitcekpvgb` (`cid`),
  KEY `FK_mhyruuc88d1wbb8jt57mmjkln` (`eid`),
  CONSTRAINT `FK_drmljo5ocl2fsm2nitcekpvgb` FOREIGN KEY (`cid`) REFERENCES `customer_customer` (`cid`),
  CONSTRAINT `FK_mhyruuc88d1wbb8jt57mmjkln` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_bulletin
-- ----------------------------
DROP TABLE IF EXISTS `customer_bulletin`;
CREATE TABLE `customer_bulletin` (
  `bid` varchar(255) NOT NULL,
  `content` longtext NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `top` bit(1) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  `eid` varchar(255) NOT NULL,
  PRIMARY KEY (`bid`),
  KEY `FK_ff2tarbm7kqv7cp6ad7l89koe` (`eid`),
  CONSTRAINT `FK_ff2tarbm7kqv7cp6ad7l89koe` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_check
-- ----------------------------
DROP TABLE IF EXISTS `customer_check`;
CREATE TABLE `customer_check` (
  `cid` varchar(255) NOT NULL,
  `checkAt` bigint(20) NOT NULL,
  `replaced` bit(1) NOT NULL,
  `similarity` double NOT NULL,
  `url` varchar(255) NOT NULL,
  `did` varchar(255) NOT NULL,
  PRIMARY KEY (`cid`),
  KEY `FK_hhjufgq49wyr7v3x1exac3qb4` (`did`),
  CONSTRAINT `FK_hhjufgq49wyr7v3x1exac3qb4` FOREIGN KEY (`did`) REFERENCES `customer_domain` (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_config
-- ----------------------------
DROP TABLE IF EXISTS `customer_config`;
CREATE TABLE `customer_config` (
  `cid` varchar(255) NOT NULL,
  `clazz` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_config
-- ----------------------------
BEGIN;
INSERT INTO `customer_config` VALUES ('40289f0a6c369fd5016c369fdccf0000', 'com.xwkj.customer.component.config.AliyunOSS', '', 'enable');
INSERT INTO `customer_config` VALUES ('40289f0a6c369fd5016c369fdce40001', 'com.xwkj.customer.component.config.AliyunOSS', '', 'endpoint');
INSERT INTO `customer_config` VALUES ('40289f0a6c369fd5016c369fdce50002', 'com.xwkj.customer.component.config.AliyunOSS', '', 'accessKeyId');
INSERT INTO `customer_config` VALUES ('40289f0a6c369fd5016c369fdce60003', 'com.xwkj.customer.component.config.AliyunOSS', '', 'accessKeySecret');
INSERT INTO `customer_config` VALUES ('40289f0a6c369fd5016c369fdce70004', 'com.xwkj.customer.component.config.AliyunOSS', '', 'bucketName');
INSERT INTO `customer_config` VALUES ('8a406622671a7dab01671a7dd0710000', 'com.xwkj.customer.component.config.Admin', 'admin', 'username');
INSERT INTO `customer_config` VALUES ('8a406622671a7dab01671a7dd0830001', 'com.xwkj.customer.component.config.Admin', '202cb962ac59075b964b07152d234b70', 'password');
COMMIT;

-- ----------------------------
-- Table structure for customer_customer
-- ----------------------------
DROP TABLE IF EXISTS `customer_customer`;
CREATE TABLE `customer_customer` (
  `cid` varchar(255) NOT NULL,
  `capital` int(11) NOT NULL,
  `contact` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `document` longtext,
  `items` varchar(255) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `remark` longtext,
  `state` int(11) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  `aid` varchar(255) NOT NULL,
  `iid` varchar(255) NOT NULL,
  `eid` varchar(255) NOT NULL,
  PRIMARY KEY (`cid`),
  KEY `FK_jyxv2xpr4nqtlphvyogxti2bh` (`aid`),
  KEY `FK_72m1j15mj2wvsv9kh2a1oigol` (`iid`),
  KEY `FK_ha3h3f4k8ux0iid3qwtew6aou` (`eid`),
  CONSTRAINT `FK_72m1j15mj2wvsv9kh2a1oigol` FOREIGN KEY (`iid`) REFERENCES `customer_industry` (`iid`),
  CONSTRAINT `FK_ha3h3f4k8ux0iid3qwtew6aou` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`),
  CONSTRAINT `FK_jyxv2xpr4nqtlphvyogxti2bh` FOREIGN KEY (`aid`) REFERENCES `customer_area` (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_device
-- ----------------------------
DROP TABLE IF EXISTS `customer_device`;
CREATE TABLE `customer_device` (
  `did` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `deviceToken` varchar(255) DEFAULT NULL,
  `identifier` varchar(255) NOT NULL,
  `ip` varchar(255) NOT NULL,
  `lan` varchar(255) DEFAULT NULL,
  `os` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  `version` varchar(255) DEFAULT NULL,
  `eid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`did`),
  UNIQUE KEY `UK_aivw3uh71er0mnwou13ebeffp` (`identifier`),
  KEY `FK_ikwxuireitfqh4uyhp2i2rgtt` (`eid`),
  CONSTRAINT `FK_ikwxuireitfqh4uyhp2i2rgtt` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_document
-- ----------------------------
DROP TABLE IF EXISTS `customer_document`;
CREATE TABLE `customer_document` (
  `did` varchar(255) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `oss` bit(1) NOT NULL,
  `size` bigint(20) NOT NULL,
  `store` varchar(255) NOT NULL,
  `uploadAt` bigint(20) NOT NULL,
  `cid` varchar(255) DEFAULT NULL,
  `eid` varchar(255) DEFAULT NULL,
  `tid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`did`),
  KEY `FK_rpsytmxhtwq6k5vwexbhlw7hy` (`cid`),
  KEY `FK_mokxeqosqrpfm485rtvcufifl` (`eid`),
  KEY `FK_6n37tabxmjld2gagome7wy18u` (`tid`),
  CONSTRAINT `FK_6n37tabxmjld2gagome7wy18u` FOREIGN KEY (`tid`) REFERENCES `customer_type` (`tid`),
  CONSTRAINT `FK_mokxeqosqrpfm485rtvcufifl` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`),
  CONSTRAINT `FK_rpsytmxhtwq6k5vwexbhlw7hy` FOREIGN KEY (`cid`) REFERENCES `customer_customer` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_domain
-- ----------------------------
DROP TABLE IF EXISTS `customer_domain`;
CREATE TABLE `customer_domain` (
  `did` varchar(255) NOT NULL,
  `alert` bit(1) NOT NULL,
  `charset` varchar(255) DEFAULT NULL,
  `checkAt` bigint(20) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `domains` varchar(255) NOT NULL,
  `frequency` int(11) NOT NULL,
  `grabbed` bit(1) NOT NULL,
  `highlight` bit(1) NOT NULL,
  `language` varchar(255) NOT NULL,
  `monitoring` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `page` longtext,
  `path` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `resolution` varchar(255) NOT NULL,
  `similarity` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  `cid` varchar(255) NOT NULL,
  `sid` varchar(255) NOT NULL,
  PRIMARY KEY (`did`),
  KEY `FK_kgj1ksyw12wshexyoyi5r9v62` (`cid`),
  KEY `FK_oeg4y94fi9xpsc7mw4yqoelq` (`sid`),
  CONSTRAINT `FK_kgj1ksyw12wshexyoyi5r9v62` FOREIGN KEY (`cid`) REFERENCES `customer_customer` (`cid`),
  CONSTRAINT `FK_oeg4y94fi9xpsc7mw4yqoelq` FOREIGN KEY (`sid`) REFERENCES `customer_server` (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_employee
-- ----------------------------
DROP TABLE IF EXISTS `customer_employee`;
CREATE TABLE `customer_employee` (
  `eid` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `enable` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  `rid` varchar(255) NOT NULL,
  PRIMARY KEY (`eid`),
  KEY `FK_62xj3r34i289ihn6lw4e5kqbd` (`rid`),
  CONSTRAINT `FK_62xj3r34i289ihn6lw4e5kqbd` FOREIGN KEY (`rid`) REFERENCES `customer_role` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_expiration
-- ----------------------------
DROP TABLE IF EXISTS `customer_expiration`;
CREATE TABLE `customer_expiration` (
  `eid` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `expireAt` bigint(20) NOT NULL,
  `money` int(11) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  `cid` varchar(255) NOT NULL,
  `tid` varchar(255) NOT NULL,
  PRIMARY KEY (`eid`),
  KEY `FK_n50gd5l6y759xr23ytg4q1kv3` (`cid`),
  KEY `FK_80gfpkxn54fmdrufv3kla9dq4` (`tid`),
  CONSTRAINT `FK_80gfpkxn54fmdrufv3kla9dq4` FOREIGN KEY (`tid`) REFERENCES `customer_type` (`tid`),
  CONSTRAINT `FK_n50gd5l6y759xr23ytg4q1kv3` FOREIGN KEY (`cid`) REFERENCES `customer_customer` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_hacker
-- ----------------------------
DROP TABLE IF EXISTS `customer_hacker`;
CREATE TABLE `customer_hacker` (
  `eid` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `ip` varchar(255) NOT NULL,
  `times` bigint(20) NOT NULL,
  `type` int(11) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  PRIMARY KEY (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_industry
-- ----------------------------
DROP TABLE IF EXISTS `customer_industry`;
CREATE TABLE `customer_industry` (
  `iid` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `customers` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`iid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_log
-- ----------------------------
DROP TABLE IF EXISTS `customer_log`;
CREATE TABLE `customer_log` (
  `lid` varchar(255) NOT NULL,
  `content` longtext,
  `createAt` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  `cid` varchar(255) NOT NULL,
  `eid` varchar(255) NOT NULL,
  PRIMARY KEY (`lid`),
  KEY `FK_3vhp3bdmwplhoqoj623rvhrb9` (`cid`),
  KEY `FK_e448d7qjrj06m2ovxtpi6rsaf` (`eid`),
  CONSTRAINT `FK_3vhp3bdmwplhoqoj623rvhrb9` FOREIGN KEY (`cid`) REFERENCES `customer_customer` (`cid`),
  CONSTRAINT `FK_e448d7qjrj06m2ovxtpi6rsaf` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_reply
-- ----------------------------
DROP TABLE IF EXISTS `customer_reply`;
CREATE TABLE `customer_reply` (
  `rid` varchar(255) NOT NULL,
  `content` longtext,
  `createAt` bigint(20) NOT NULL,
  `eid` varchar(255) NOT NULL,
  `wid` varchar(255) NOT NULL,
  PRIMARY KEY (`rid`),
  KEY `FK_goajiqvdhnqgg1m3u19hifhcb` (`eid`),
  KEY `FK_5jbrjpoerw64i0kf70kiubgqf` (`wid`),
  CONSTRAINT `FK_5jbrjpoerw64i0kf70kiubgqf` FOREIGN KEY (`wid`) REFERENCES `customer_work` (`wid`),
  CONSTRAINT `FK_goajiqvdhnqgg1m3u19hifhcb` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_report
-- ----------------------------
DROP TABLE IF EXISTS `customer_report`;
CREATE TABLE `customer_report` (
  `rid` varchar(255) NOT NULL,
  `content` longtext,
  `createAt` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `updateAt` bigint(20) NOT NULL,
  `eid` varchar(255) NOT NULL,
  `tid` varchar(255) NOT NULL,
  PRIMARY KEY (`rid`),
  KEY `FK_mfwjp0rl09p68v5dg21xx70uq` (`eid`),
  KEY `FK_r05qxe965xnjaw4lgdv52sull` (`tid`),
  CONSTRAINT `FK_mfwjp0rl09p68v5dg21xx70uq` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`),
  CONSTRAINT `FK_r05qxe965xnjaw4lgdv52sull` FOREIGN KEY (`tid`) REFERENCES `customer_type` (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_role
-- ----------------------------
DROP TABLE IF EXISTS `customer_role`;
CREATE TABLE `customer_role` (
  `rid` varchar(255) NOT NULL,
  `assign` int(11) NOT NULL,
  `bulletin` int(11) DEFAULT NULL,
  `develop` int(11) NOT NULL,
  `developedD` int(11) NOT NULL,
  `developedR` int(11) NOT NULL,
  `developedW` int(11) NOT NULL,
  `developingD` int(11) NOT NULL,
  `developingR` int(11) NOT NULL,
  `developingW` int(11) NOT NULL,
  `domain` int(11) NOT NULL,
  `employee` int(11) NOT NULL,
  `employees` int(11) NOT NULL,
  `expiration` int(11) DEFAULT NULL,
  `finish` int(11) NOT NULL,
  `lostD` int(11) NOT NULL,
  `lostR` int(11) NOT NULL,
  `lostW` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `recover` int(11) NOT NULL,
  `ruin` int(11) NOT NULL,
  `server` int(11) NOT NULL,
  `undevelopedD` int(11) NOT NULL,
  `undevelopedR` int(11) NOT NULL,
  `undevelopedW` int(11) NOT NULL,
  `work` int(11) DEFAULT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_salary
-- ----------------------------
DROP TABLE IF EXISTS `customer_salary`;
CREATE TABLE `customer_salary` (
  `sid` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `detail` varchar(255) NOT NULL,
  `money` int(11) NOT NULL,
  `remark` varchar(255) NOT NULL,
  `eid` varchar(255) NOT NULL,
  PRIMARY KEY (`sid`),
  KEY `FK_qd6c4kdgyqarlvnoxywuryllt` (`eid`),
  CONSTRAINT `FK_qd6c4kdgyqarlvnoxywuryllt` FOREIGN KEY (`eid`) REFERENCES `customer_employee` (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_server
-- ----------------------------
DROP TABLE IF EXISTS `customer_server`;
CREATE TABLE `customer_server` (
  `sid` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `credential` longtext,
  `domains` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `updateAt` bigint(20) NOT NULL,
  `user` varchar(255) DEFAULT NULL,
  `usingPublicKey` bit(1) DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_type
-- ----------------------------
DROP TABLE IF EXISTS `customer_type`;
CREATE TABLE `customer_type` (
  `tid` varchar(255) NOT NULL,
  `category` int(11) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_work
-- ----------------------------
DROP TABLE IF EXISTS `customer_work`;
CREATE TABLE `customer_work` (
  `wid` varchar(255) NOT NULL,
  `active` bit(1) NOT NULL,
  `createAt` bigint(20) NOT NULL,
  `replys` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `executor_uid` varchar(255) NOT NULL,
  `sponsor_uid` varchar(255) NOT NULL,
  PRIMARY KEY (`wid`),
  KEY `FK_s52lcgkvrvauqpl9kbhk5i4hj` (`executor_uid`),
  KEY `FK_h358uvr49qobc22qoucsw5auk` (`sponsor_uid`),
  CONSTRAINT `FK_h358uvr49qobc22qoucsw5auk` FOREIGN KEY (`sponsor_uid`) REFERENCES `customer_employee` (`eid`),
  CONSTRAINT `FK_s52lcgkvrvauqpl9kbhk5i4hj` FOREIGN KEY (`executor_uid`) REFERENCES `customer_employee` (`eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
