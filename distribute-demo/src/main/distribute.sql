/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : distribute

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 11/05/2020 23:43:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for distribute_lock
-- ----------------------------
DROP TABLE IF EXISTS `distribute_lock`;
CREATE TABLE `distribute_lock` (
  `id` int(11) NOT NULL,
  `business_code` varchar(255) DEFAULT NULL,
  `business_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分布式锁 字典表';

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_status` int(1) DEFAULT '1',
  `receiver_name` varchar(255) DEFAULT NULL,
  `receiver_mobile` varchar(255) DEFAULT NULL,
  `order_amount` varchar(255) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `update_time` time DEFAULT NULL,
  `update_user` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `purchase_price` decimal(10,2) DEFAULT NULL,
  `purchase_num` int(11) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `update_time` time DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `update_user` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `count` int(10) DEFAULT NULL,
  `product_desc` varchar(255) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `update_time` time DEFAULT NULL,
  `update_user` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100101 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
