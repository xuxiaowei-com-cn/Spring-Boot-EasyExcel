/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : spring_boot_easyexcel

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 25/01/2020 23:11:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `sex` int(1) NULL DEFAULT NULL COMMENT '性别，0 未知，1 男， 2 女',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，1 已删除，0 未删除，默认为 0，数据库类型为 tinyint，长度为 1，对应实体类为 Boolean，0 为 false，1 为 true',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'xxw', '123', NULL, 0);
INSERT INTO `user` VALUES (2, 'lsy', '456', NULL, 1);
INSERT INTO `user` VALUES (100, 'zhangguorong', '207a79167afc40c9998f6af568d2885b', 1, 0);
INSERT INTO `user` VALUES (101, 'liuyifei', '10510ab2c7a44766959ecc77f98319c6', 1, 0);
INSERT INTO `user` VALUES (102, 'yangmi', '566240ca86404eaab866bae79fdeba97', 0, 1);
INSERT INTO `user` VALUES (103, 'zhaohaitang', '04d75e07de614fcb9226e11e3caa1722', 1, 0);
INSERT INTO `user` VALUES (104, 'huyifei', '5f3bb5b92678456e9727752abfbabd29', 2, 0);
INSERT INTO `user` VALUES (105, 'zhangwei', 'a93885f5513c4b969e5d2bc4689921c1', 2, 0);
INSERT INTO `user` VALUES (106, 'liushishi', 'cd068af1d6c24c0f849107688dd5994d', 2, 1);
INSERT INTO `user` VALUES (107, 'zhaoliying', 'e007b34bb7b64f70853eed9667252400', 2, 0);
INSERT INTO `user` VALUES (108, 'tangyan', 'b0fe5e5473ea42e183062b652e9559e3', 2, 0);
INSERT INTO `user` VALUES (109, 'dengchao', '4bb357d208da4d22b478cdcf9c5aa7fc', 1, 0);
INSERT INTO `user` VALUES (110, 'sunli', 'f4674683aa574b0aacec227a03a84d00', 2, 0);
INSERT INTO `user` VALUES (111, 'caixukun', 'b2d40b24567c4c739701cd3a7e8f5cf5', 2, 0);
INSERT INTO `user` VALUES (112, 'gaoyuanyuan', '64bcd0e4e51a4553ab4484085632fcde', 2, 1);
INSERT INTO `user` VALUES (113, 'wuyifan', 'f97f63b801fd49c9941ff43171aa0143', 2, 0);
INSERT INTO `user` VALUES (114, 'baobeier', '3bf76480f23d46da8b142a8f117d3a09', 1, 0);
INSERT INTO `user` VALUES (115, 'chenzihan', '05587c40560b45b4b443bafdb7072d11', 2, 0);
INSERT INTO `user` VALUES (116, 'tanke', 'a64d579825c54a6db4d2e6c64c370758', 2, 0);
INSERT INTO `user` VALUES (117, 'nigulasizhaosi', 'af5d37ca3cc04743bda0bd860225bd08', 1, 0);
INSERT INTO `user` VALUES (118, 'geyou', 'b96dce888046440cbeafb6a3548c1496', 2, 0);
INSERT INTO `user` VALUES (119, 'zahngzimo', '2cf5f5a3c3da4e31880427de926cb70a', 0, 1);
INSERT INTO `user` VALUES (120, 'huojianhua', 'fafd6d3bc5fb4aab91c8a1005e2958ae', 2, 0);
INSERT INTO `user` VALUES (121, 'huangxiaoming', '154f51a5ee3f425db1d966aa08d853e4', 1, 0);
INSERT INTO `user` VALUES (122, 'jingboran', 'c340bb0ca6534de6b645c413a927c826', 2, 1);
INSERT INTO `user` VALUES (123, 'linzhiying', '273278388b72433fb6522c1c9ca9a321', 2, 0);

SET FOREIGN_KEY_CHECKS = 1;
