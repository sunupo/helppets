/*
 Navicat Premium Data Transfer

 Source Server         : TT
 Source Server Type    : MySQL
 Source Server Version : 50559
 Source Host           : localhost:3306
 Source Schema         : tt

 Target Server Type    : MySQL
 Target Server Version : 50559
 File Encoding         : 65001

 Date: 30/03/2019 20:07:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `accountid` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`accountid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('00000001', 'fxs', 'E10ADC3949BA59ABBE56E057F20F883E');
INSERT INTO `account` VALUES ('00000002', 'aaa', '47bce5c74f589f4867dbd57e9ca9f808');
INSERT INTO `account` VALUES ('00000003', 'bbb', 'bbb');
INSERT INTO `account` VALUES ('00000004', 'ccc', 'ccc');
INSERT INTO `account` VALUES ('00000005', 'a', 'a');
INSERT INTO `account` VALUES ('00000024', 'ndkjdnd', '41C9B1D2A19C1C6372104894A9903481');
INSERT INTO `account` VALUES ('00000025', 'ndkjdndhj', '41C9B1D2A19C1C6372104894A9903481');
INSERT INTO `account` VALUES ('00000026', 'aaaaa', '594F803B380A41396ED63DCA39503542');
INSERT INTO `account` VALUES ('00000027', 'hsjksmd', '359425BF96E8BE990145E403524AFC72');
INSERT INTO `account` VALUES ('00000028', 'bbbbb', 'A21075A36EEDDD084E17611A238C7101');
INSERT INTO `account` VALUES ('00000029', 'aaaaaa', '0B4E7A0E5FE84AD35FB5F95B9CEEAC79');
INSERT INTO `account` VALUES ('00000030', 'b', '92EB5FFEE6AE2FEC3AD71C777531578F');
INSERT INTO `account` VALUES ('00000031', 'cccc', '41FCBA09F2BDCDF315BA4119DC7978DD');
INSERT INTO `account` VALUES ('00000032', 'ddd', '77963B7A931377AD4AB5AD6A9CD718AA');
INSERT INTO `account` VALUES ('00000033', 'dddd', '11DDBAF3386AEA1F2974EEE984542152');
INSERT INTO `account` VALUES ('00000034', '往前', '202CB962AC59075B964B07152D234B70');
INSERT INTO `account` VALUES ('00000035', 'mmm', 'C4EFD5020CB49B9D3257FFA0FBCCC0AE');
INSERT INTO `account` VALUES ('00000036', 'sjq', 'E10ADC3949BA59ABBE56E057F20F883E');
INSERT INTO `account` VALUES ('00000037', 'sunjingqin', 'E10ADC3949BA59ABBE56E057F20F883E');
INSERT INTO `account` VALUES ('00000038', 'ldh', '670B14728AD9902AECBA32E22FA4F6BD');
INSERT INTO `account` VALUES ('00000039', 'chongqing', '670B14728AD9902AECBA32E22FA4F6BD');

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album`  (
  `albumid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '相册描述',
  `level` int(11) DEFAULT NULL COMMENT '相册公开等级：都可见0/普通好友可见1/VIP好友可见2/付费用户可见3',
  `price` double DEFAULT NULL COMMENT '如果付费，其付费价格由此决定。不用付费的情况下此字段为0',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`albumid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of album
-- ----------------------------
INSERT INTO `album` VALUES ('0', 'me', '00000003', '这是一个好相册', 3, 0, '201903010201');
INSERT INTO `album` VALUES ('1', 'me', '00000003', '这是一个免费相册', 0, 0, '201903010201');
INSERT INTO `album` VALUES ('2', 'else', '00000003', '这是一个VIP免费相册', 2, 0, '201903010201');

-- ----------------------------
-- Table structure for album_authority
-- ----------------------------
DROP TABLE IF EXISTS `album_authority`;
CREATE TABLE `album_authority`  (
  `account_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `authority` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`account_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bill
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill`  (
  `account_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `date` datetime DEFAULT NULL,
  `income` int(11) DEFAULT NULL,
  `category` int(15) DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of bill
-- ----------------------------
INSERT INTO `bill` VALUES ('00000001', '2018-04-24 11:33:47', -1000, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-04-04 15:01:39', 500, 1);
INSERT INTO `bill` VALUES ('00000001', '2018-05-23 12:38:36', -100, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-05-25 14:45:45', -100, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-05-31 19:10:29', -100, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-05-31 19:10:58', 10, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-02 22:31:13', 2000, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-02 22:53:16', 2000, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-02 22:55:00', 3, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-02 23:11:34', 3, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-02 23:12:55', 3, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-03 21:40:56', 3, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-03 21:46:51', 3, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-06 11:10:36', -100, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-06-06 11:15:06', -100, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-06-07 14:56:32', -100, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-06-07 14:59:07', -150, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-06-10 15:41:05', -100, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-06-10 15:42:33', 3, 0);
INSERT INTO `bill` VALUES ('00000001', '2018-06-10 15:42:52', 100, 1);
INSERT INTO `bill` VALUES ('00000001', '2018-06-17 14:24:20', 100, 1);
INSERT INTO `bill` VALUES ('00000001', '2018-06-17 14:24:34', -100, 3);
INSERT INTO `bill` VALUES ('00000001', '2018-06-17 14:26:09', 3, 0);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `account` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `comment_account` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `dynaimc_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `releasedate` datetime DEFAULT NULL COMMENT '评论时间',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `support` int(11) DEFAULT NULL,
  `unlike` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('00000001', '00000002', '000000010001', '2018-05-23 16:05:21', '喜欢你的生活态度', '00000001', 0, 0);
INSERT INTO `comment` VALUES ('00000001', '00000003', '000000010001', NULL, '喜欢你', '00000002', 0, 0);
INSERT INTO `comment` VALUES ('00000002', '00000001', '000000020001', '2018-06-01 16:06:01', '666666', '00000003', 0, 0);
INSERT INTO `comment` VALUES ('00000005', '00000001', '000000050001', '2018-06-02 16:05:28', '哇！好美', '00000004', 5, 3);
INSERT INTO `comment` VALUES ('00000005', '00000004', '000000050001', NULL, '注意休息', '00000005', 9, 2);
INSERT INTO `comment` VALUES ('00000005', '00000005', '000000050001', NULL, '陈东阳逗比一般的存在', '00000006', 9, 2);
INSERT INTO `comment` VALUES ('00000005', '00000002', '000000050001', NULL, '这么漂亮怎么还单身？', '00000007', 11, 1);
INSERT INTO `comment` VALUES ('00000003', '00000003', '61f7bf95-4001', NULL, 'fhj', '5e842e62-a87b', 0, 0);
INSERT INTO `comment` VALUES ('00000003', '00000003', '63d79a43-e46a', NULL, '哈哈', '7bad02cc-a263', 0, 0);
INSERT INTO `comment` VALUES ('00000003', '00000003', '63d79a43-e46a', NULL, '哈哈', 'b8021d95-8c8c', 0, 0);

-- ----------------------------
-- Table structure for date
-- ----------------------------
DROP TABLE IF EXISTS `date`;
CREATE TABLE `date`  (
  `invite_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `receive_Id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `theme` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `gift` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL COMMENT '发表时间',
  `datefortime` datetime DEFAULT NULL COMMENT '约会时间',
  `location` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `integral` int(11) DEFAULT 0 COMMENT '查看图片需要积分，默认0',
  `sincirity` int(11) DEFAULT 0 COMMENT '报名支付诚意分',
  `d_id` int(15) NOT NULL AUTO_INCREMENT,
  `maintext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `mainpicture` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sex` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报名性别',
  PRIMARY KEY (`d_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of date
-- ----------------------------
INSERT INTO `date` VALUES ('00000001', '00000036', '一辆汽车,200元红包,露营,限女生', NULL, '2018-04-02 15:36:33', '2018-06-08 20:04:36', '成都', 0, 0, 1, '期待和你一起看场电影，你呢', '/00/00/00/01/dy0001.jpg', NULL);
INSERT INTO `date` VALUES ('00000002', '-1', '一辆汽车,200元红包,露营,限女生', NULL, '2018-03-06 15:48:37', '2018-07-19 20:04:38', '重庆', 0, 10, 2, '哈哈哈你好', '/00/00/00/02/dy002.jpg', NULL);
INSERT INTO `date` VALUES ('00000001', '-1', '一辆汽车,200元红包,露营,限女生', NULL, '2018-06-04 16:59:51', '2018-06-04 16:59:55', '上海', 0, 0, 4, '哈哈哈你好', '/00/00/00/02/dy002.jpg', NULL);
INSERT INTO `date` VALUES ('00000002', '00000001', '一辆汽车,200元红包,露营,限女生', NULL, '2018-06-04 16:59:51', '2018-06-04 16:59:55', '上海', 0, 10, 5, '哈哈哈你好', '/00/00/00/02/dy002.jpg', '');

-- ----------------------------
-- Table structure for dynamic
-- ----------------------------
DROP TABLE IF EXISTS `dynamic`;
CREATE TABLE `dynamic`  (
  `d_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `d_category` int(11) NOT NULL,
  `d_content` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `d_releasetime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `support` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `unlike` int(11) NOT NULL DEFAULT 0,
  `gift` int(11) NOT NULL DEFAULT 0,
  `comment` int(15) NOT NULL DEFAULT 0,
  `share` int(11) NOT NULL DEFAULT 0,
  `dyima` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`d_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dynamic
-- ----------------------------
INSERT INTO `dynamic` VALUES ('', '', 0, NULL, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('000000010001', '00000001', 1, '一起K歌呀', '201803031020', 2, 3, 4, 4, 1, '/00/00/00/01/1803031020.png');
INSERT INTO `dynamic` VALUES ('000000010002', '00000001', 1, '无聊自嗨', '201803311620', 2, 3, 3, 4, 6, '/00/00/00/01/1803311620.jpg');
INSERT INTO `dynamic` VALUES ('000000020001', '00000002', 1, '晚上一起看电影', '201803301200', 5, 3, 3, 4, 9, '/00/00/00/01/1803311620.jpg');
INSERT INTO `dynamic` VALUES ('000000030001', '00000003', 1, '一起春游呀', '201803031025', 2, 3, 4, 4, 1, NULL);
INSERT INTO `dynamic` VALUES ('000000050001', '00000005', 1, '小李师傅拍的,下午还要赶一场活动,好想有个男朋友', '201804041015', 1, 3, 0, 6, 9, '/00/00/00/05/1804041015.jpg');
INSERT INTO `dynamic` VALUES ('069f9340-9316', '00000003', 1, '发个呵呵', '201805222153', 0, 0, 0, 0, 0, '/00/00/00/8136711480996.jpg,/00/00/00/8136810284870.jpg,/00/00/00/8136904426902.jpg');
INSERT INTO `dynamic` VALUES ('28c4b28b-8d69', '00000001', 1, 'sjq发布的文字', '201808031629', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('397a21ea-e91e', '00000001', 1, '111', '201806101615', 0, 0, 0, 0, 0, '/00/00/00/18624352428257.png');
INSERT INTO `dynamic` VALUES ('589ceba1-2490', '00000001', 1, 'bdbbdbdb', '201808031628', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('61f7bf95-4001', '00000003', 1, '晚上一起看电影', '201805121840', 1, 1, 3, 1, 0, 'null');
INSERT INTO `dynamic` VALUES ('63d79a43-e46a', '00000003', 1, 'a', '201805291438', 1, 1, 0, 1, 0, '/00/00/00/13293987745291.jpg/00/00/00/13307801514852.jpg');
INSERT INTO `dynamic` VALUES ('65f9f7d9-f3fe', '00000001', 1, '999999', '201806101613', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('71f82728-d25d', '00000001', 1, '41564165', '201806101604', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('83dbd454-b5ee', '00000003', 1, 'a', '201805291438', 0, 0, 0, 0, 0, '/00/00/00/13293987745291.jpg');
INSERT INTO `dynamic` VALUES ('90cf1247-c4dc', '00000036', 1, '今天2018-12-20智能信息处理汇报', '201812201433', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('a8b4262d-fa9f', '00000001', 1, '41564165', '201806101604', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('afc15402-f4a8', '00000001', 1, '999', '201806101614', 0, 0, 0, 0, 0, '/00/00/00/18543422100733.png');
INSERT INTO `dynamic` VALUES ('b036a0e7-75fe', '00000003', 1, '无聊自嗨', '201805121840', 0, 0, 0, 0, 0, '/00/00/00/01/1803311620.jpg');
INSERT INTO `dynamic` VALUES ('cee1c4e6-c795', '00000036', 1, '今天2018-12-20智能信息处理汇报', '201812201433', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('f2e6eed2-8964', '00000001', 1, 'bdbbdbdb', '201808031628', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('f33f9a06-dccc', '00000003', 1, '啊', '201805171625', 0, 0, 0, 0, 0, '/00/00/00/20185657696018.png,/00/00/00/20185778878183.png,/00/00/00/20186041097841.png');
INSERT INTO `dynamic` VALUES ('f9192191-eb91', '00000036', 1, '今天2018-12-20智能信息处理汇报', '201812201433', 0, 0, 0, 0, 0, NULL);
INSERT INTO `dynamic` VALUES ('fa4296f6-8748', '00000001', 1, '999999', '201806101613', 0, 0, 0, 0, 0, NULL);

-- ----------------------------
-- Table structure for file_trade
-- ----------------------------
DROP TABLE IF EXISTS `file_trade`;
CREATE TABLE `file_trade`  (
  `tradeid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `accountid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '付款方ID',
  `acceptside` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收款方的ID',
  `fileid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '可以是相册ID，也可以是声音ID，以及视频ID。具体分类由file_type字段决定',
  `file_type` int(11) DEFAULT NULL COMMENT '相册：0，声音文件：1，视频文件：2，其它文件：3',
  `status` int(11) DEFAULT NULL COMMENT '交易是否完成，交易未完成0/交易已完成1',
  `start_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交易发起时间',
  `end_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交易完成时间',
  `charge` double DEFAULT NULL COMMENT '缴纳的钱数',
  PRIMARY KEY (`tradeid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of file_trade
-- ----------------------------
INSERT INTO `file_trade` VALUES ('35b442cb-5c42-4c5f-9311-5f950bf308a0', '00000004', '00000003', '0', 0, 1, NULL, NULL, 100);
INSERT INTO `file_trade` VALUES ('6b919350-082b-4c1d-b472-febb7923cb8d', '00000001', '00000003', '0', 0, 1, '13666995447236', '13667003737043', 0);
INSERT INTO `file_trade` VALUES ('850985cc-3515-41ca-b681-0d84ef9f5c13', '00000005', '00000003', '0', 0, 1, NULL, NULL, 52);
INSERT INTO `file_trade` VALUES ('8f5f6e81-e4e3-4701-8bdc-2ab2f5c7ad3f', '00000003', '00000005', '0', 0, 1, NULL, NULL, 52);

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`  (
  `accountid` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `friendid` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `frienditem` int(1) NOT NULL COMMENT 'frienditem:好友为0，关注为1，粉丝为2',
  `type` int(11) DEFAULT 0,
  PRIMARY KEY (`accountid`, `friendid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of friends
-- ----------------------------
INSERT INTO `friends` VALUES ('00000001', '00000002', 0, 0);
INSERT INTO `friends` VALUES ('00000001', '00000003', 1, 0);
INSERT INTO `friends` VALUES ('00000001', '00000004', 0, NULL);
INSERT INTO `friends` VALUES ('00000001', '00000005', 0, 0);
INSERT INTO `friends` VALUES ('00000001', '00000024', 1, NULL);
INSERT INTO `friends` VALUES ('00000001', '00000025', 1, NULL);
INSERT INTO `friends` VALUES ('00000001', '00000027', 1, NULL);
INSERT INTO `friends` VALUES ('00000001', '00000028', 1, NULL);
INSERT INTO `friends` VALUES ('00000001', '00000034', 1, NULL);
INSERT INTO `friends` VALUES ('00000002', '00000001', 0, 0);
INSERT INTO `friends` VALUES ('00000003', '00000001', 0, 0);
INSERT INTO `friends` VALUES ('00000004', '00000001', 0, 0);

-- ----------------------------
-- Table structure for gift
-- ----------------------------
DROP TABLE IF EXISTS `gift`;
CREATE TABLE `gift`  (
  `g_id` int(11) NOT NULL,
  `g_category` int(11) NOT NULL,
  `g_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pictureurl` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `integral` int(11) DEFAULT NULL,
  `sincererity` int(11) DEFAULT NULL,
  PRIMARY KEY (`g_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of gift
-- ----------------------------
INSERT INTO `gift` VALUES (1, 1, '小熊', '/bear.png', 100, 200);
INSERT INTO `gift` VALUES (2, 1, '汽车', '/redcar.png', 150, 250);
INSERT INTO `gift` VALUES (3, 1, '蛋糕', '/cake.png', 200, 250);
INSERT INTO `gift` VALUES (4, 1, '咖啡', '/coffee.png', 300, 350);
INSERT INTO `gift` VALUES (5, 1, '钻戒', '/ring.png', 400, 450);
INSERT INTO `gift` VALUES (6, 1, '火箭', '/rocket.png', 500, 550);
INSERT INTO `gift` VALUES (7, 1, '玫瑰', '/rose.png', 600, 650);

-- ----------------------------
-- Table structure for img_info
-- ----------------------------
DROP TABLE IF EXISTS `img_info`;
CREATE TABLE `img_info`  (
  `imgid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `albumid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '相册ID',
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片名',
  `imgurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片路径',
  `upload_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`imgid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of img_info
-- ----------------------------
INSERT INTO `img_info` VALUES ('1', '0', '1.jpg', '/00000003/0/1.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('10', '0', '10.jpg', '/00000003/0/10.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('11', '0', '11.jpg', '/00000003/0/11.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('12', '0', '12.jpg', '/00000003/0/12.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('13', '0', '13.jpg', '/00000003/0/13.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('14', '0', '14.jpg', '/00000003/0/14.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('15', '0', '15.jpg', '/00000003/0/15.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('16', '0', '16.jpg', '/00000003/0/16.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('17', '0', '17.jpg', '/00000003/0/17.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('2', '0', '2.jpg', '/00000003/0/2.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('21', '1', '21.jpg', '/00000003/1/21.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('22', '1', '22.jpg', '/00000003/1/22.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('23', '1', '23.jpg', '/00000003/1/23.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('24', '1', '24.jpg', '/00000003/1/24.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('25', '1', '25.jpg', '/00000003/1/25.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('26', '1', '26.jpg', '/00000003/1/26.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('27', '1', '27.jpg', '/00000003/1/27.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('28', '1', '28.jpg', '/00000003/1/28.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('29', '1', '29.jpg', '/00000003/1/29.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('3', '0', '3.jpg', '/00000003/0/3.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('30', '1', '30.jpg', '/00000003/1/30.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('31', '2', '31.jpg', '/00000003/2/31.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('32', '2', '32.jpg', '/00000003/2/32.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('33', '2', '33.jpg', '/00000003/2/33.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('34', '2', '34.jpg', '/00000003/2/34.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('35', '2', '35.jpg', '/00000003/2/35.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('36', '2', '36.jpg', '/00000003/2/36.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('37', '2', '37.jpg', '/00000003/2/37.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('38', '2', '38.jpg', '/00000003/2/38.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('39', '2', '39.jpg', '/00000003/2/39.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('4', '0', '4.jpg', '/00000003/0/4.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('40', '2', '40.jpg', '/00000003/2/40.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('5', '0', '5.jpg', '/00000003/0/5.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('6', '0', '6.jpg', '/00000003/0/6.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('7', '0', '7.jpg', '/00000003/0/7.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('8', '0', '8.jpg', '/00000003/0/8.jpg', '20180301030202');
INSERT INTO `img_info` VALUES ('9', '0', '9.jpg', '/00000003/0/9.jpg', '20180301030202');

-- ----------------------------
-- Table structure for information
-- ----------------------------
DROP TABLE IF EXISTS `information`;
CREATE TABLE `information`  (
  `actorid` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `toid` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` int(11) DEFAULT NULL COMMENT '事件类型，1是关注事件',
  `releasedate` datetime DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of information
-- ----------------------------
INSERT INTO `information` VALUES ('00000001', '00000028', 1, '2018-06-04 17:00:40');
INSERT INTO `information` VALUES ('00000001', '00000024', 1, '2018-06-04 17:00:56');
INSERT INTO `information` VALUES ('00000003', '00000001', 1, '2018-06-04 17:15:27');
INSERT INTO `information` VALUES ('00000004', '00000001', 1, '2018-06-04 17:15:37');
INSERT INTO `information` VALUES ('00000005', '00000001', 2, '2018-06-08 17:15:46');

-- ----------------------------
-- Table structure for sign
-- ----------------------------
DROP TABLE IF EXISTS `sign`;
CREATE TABLE `sign`  (
  `account_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `begin_sign` date DEFAULT NULL,
  `end_sign` date DEFAULT NULL,
  PRIMARY KEY (`account_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_gift
-- ----------------------------
DROP TABLE IF EXISTS `user_gift`;
CREATE TABLE `user_gift`  (
  `account_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `gift_id` int(11) NOT NULL,
  `count` int(15) DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_gift
-- ----------------------------
INSERT INTO `user_gift` VALUES ('00000001', 1, 6);
INSERT INTO `user_gift` VALUES ('00000003', 1, 38);
INSERT INTO `user_gift` VALUES ('00000002', 2, 12);
INSERT INTO `user_gift` VALUES ('00000002', 1, 4);
INSERT INTO `user_gift` VALUES ('00000004', 2, 1);
INSERT INTO `user_gift` VALUES ('00000003', 3, 1);
INSERT INTO `user_gift` VALUES ('00000005', 4, 1);
INSERT INTO `user_gift` VALUES ('00000003', 2, 2);
INSERT INTO `user_gift` VALUES ('00000002', 3, 1);
INSERT INTO `user_gift` VALUES ('00000001', 2, 1);
INSERT INTO `user_gift` VALUES ('00000003', 4, 1);
INSERT INTO `user_gift` VALUES ('00000003', 7, 1);
INSERT INTO `user_gift` VALUES ('00000028', 1, 1);
INSERT INTO `user_gift` VALUES ('00000028', 5, 1);
INSERT INTO `user_gift` VALUES ('00000034', 2, 1);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `account_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  `constellation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `city` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `hight` int(11) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `qq` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `phone_num` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `like_sports` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sex_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `merriage` tinyint(4) DEFAULT NULL,
  `friendcount` int(11) UNSIGNED DEFAULT 0,
  `follwcount` int(11) UNSIGNED DEFAULT 0,
  `fanscount` int(11) UNSIGNED DEFAULT 0,
  `giftcount` int(11) UNSIGNED DEFAULT 0,
  `profile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `relation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`account_id`) USING BTREE,
  INDEX `accountid`(`account_id`) USING BTREE,
  CONSTRAINT `accountid` FOREIGN KEY (`account_id`) REFERENCES `account` (`accountid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('00000001', 1, NULL, '陈东阳001', '北京市', 180, 50, '9999@qq.com', '12345678', '唱歌，爬山', '希望有人能陪我聊聊天', 0, 10, 20, 5, 1, '/00/00/00/01/profile.jpg', NULL, '朋友', 22);
INSERT INTO `user_info` VALUES ('00000002', 1, NULL, '王伟', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, 0, 2, 4, 2, 2, '/00/00/00/02/profile.jpg', NULL, '知己', 21);
INSERT INTO `user_info` VALUES ('00000003', 2, NULL, '黎明', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 4, 2, 2, '/00/00/00/03/profile.jpg', NULL, '知己', 24);
INSERT INTO `user_info` VALUES ('00000004', 2, NULL, '小明', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/04/profile.jpg', NULL, '知己', 23);
INSERT INTO `user_info` VALUES ('00000005', 2, NULL, '华华', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/05/profile.jpeg', NULL, '知己', 20);
INSERT INTO `user_info` VALUES ('00000024', 2, NULL, '摸', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/24/profile.jpg', NULL, '朋友', 22);
INSERT INTO `user_info` VALUES ('00000025', 2, NULL, '哈哈', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/25/profile.jpg', NULL, '朋友', 22);
INSERT INTO `user_info` VALUES ('00000026', 1, NULL, '', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ' /00/00/00/01/profile.jpg', NULL, '朋友', 22);
INSERT INTO `user_info` VALUES ('00000027', 1, NULL, '哈哈', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/27/profile.jpg', NULL, '朋友', 22);
INSERT INTO `user_info` VALUES ('00000028', 1, NULL, '哈哈', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/28/profile.jpg', NULL, '朋友', 22);
INSERT INTO `user_info` VALUES ('00000029', 2, NULL, '你好啊', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/29/profile.jpg', NULL, '知己', 22);
INSERT INTO `user_info` VALUES ('00000030', 2, NULL, 'hh', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/30/profile.jpg', NULL, '亲密关系', 22);
INSERT INTO `user_info` VALUES ('00000031', 2, NULL, '你好', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/31/profile.jpg', NULL, '朋友', 30);
INSERT INTO `user_info` VALUES ('00000032', 2, NULL, 'nj', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/32/profile.jpg', NULL, '朋友', 30);
INSERT INTO `user_info` VALUES ('00000033', 1, NULL, 'jj', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/00/00/00/33/profile.jpg', NULL, '知己', 30);
INSERT INTO `user_info` VALUES ('00000034', 2, NULL, '问问', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, '/00/00/00/01/profile.jpg', NULL, '朋友', 30);
INSERT INTO `user_info` VALUES ('00000035', 1, NULL, '哈哈', '重庆市', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, '/00/00/00/35/profile.jpg', NULL, '知己', 30);
INSERT INTO `user_info` VALUES ('00000036', 1, NULL, 'xiaomi', '广安市', 170, 60, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, '/00/00/00/36/profile.jpg', NULL, '朋友', 22);
INSERT INTO `user_info` VALUES ('00000037', 2, NULL, 'honor', '重庆市', 188, 77, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, '/00/00/00/37/profile.jpg', NULL, '朋友', 33);
INSERT INTO `user_info` VALUES ('00000038', 1, NULL, '刘德华', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, '/00/00/00/38/profile.jpg', NULL, '亲密关系', 18);
INSERT INTO `user_info` VALUES ('00000039', 1, NULL, '重庆大学', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 0, '/00/00/00/39/profile.jpg', NULL, '朋友', 70);

-- ----------------------------
-- Table structure for voicedy
-- ----------------------------
DROP TABLE IF EXISTS `voicedy`;
CREATE TABLE `voicedy`  (
  `v_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `v_category` int(11) NOT NULL,
  `v_time` int(11) NOT NULL,
  `v_peroflisen` int(11) NOT NULL,
  `v_rate` int(11) NOT NULL DEFAULT 0,
  `support` int(11) NOT NULL DEFAULT 0,
  `unlike` int(11) NOT NULL DEFAULT 0,
  `gift` int(11) NOT NULL,
  `comment` int(11) NOT NULL,
  `share` int(11) NOT NULL,
  `voice_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `voice_pubtime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `level` int(11) DEFAULT 0,
  `price` double DEFAULT 0,
  PRIMARY KEY (`v_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of voicedy
-- ----------------------------
INSERT INTO `voicedy` VALUES ('110d7774-dd12', '00000003', 2, 1, 100, 0, 2, 1, 0, 1, 1, '/00/00/00/27348603777024.m4a', '201805121838', 0, 0);
INSERT INTO `voicedy` VALUES ('6b63fa01-8ded', '00000001', 2, 1, 72, 0, 1, 0, 0, 0, 0, '/00/00/00/18584406424282.m4a', '201806101614', NULL, NULL);
INSERT INTO `voicedy` VALUES ('844d3316-4b2e', '00000003', 2, 1, 100, 0, 1, 2, 2, 1, 0, '/00/00/00/27348603777024.m4a', '201805121839', 0, 0);

-- ----------------------------
-- Table structure for voicomment
-- ----------------------------
DROP TABLE IF EXISTS `voicomment`;
CREATE TABLE `voicomment`  (
  `vc_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `commnet_account` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `voicedy_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `support` int(11) NOT NULL DEFAULT 0,
  `unlike` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`vc_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of voicomment
-- ----------------------------
INSERT INTO `voicomment` VALUES ('59f86bf0-8722', '00000003', '00000003', '110d7774-dd12', 'dfhhh', 0, 0);
INSERT INTO `voicomment` VALUES ('789f6e2a-9b8d', '00000003', '00000003', '844d3316-4b2e', '(⊙o⊙)哇', 0, 0);
INSERT INTO `voicomment` VALUES ('83bcfcee-da83', '00000003', '00000003', '844d3316-4b2e', '(⊙o⊙)哇', 0, 0);

-- ----------------------------
-- Table structure for wallet
-- ----------------------------
DROP TABLE IF EXISTS `wallet`;
CREATE TABLE `wallet`  (
  `account_id` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `integral` int(11) UNSIGNED ZEROFILL DEFAULT 00000000000,
  `Sincerity` int(11) UNSIGNED DEFAULT 0,
  `signcount` int(11) UNSIGNED DEFAULT 0,
  `rewardcount` int(11) UNSIGNED DEFAULT 0,
  `chatcount` int(11) UNSIGNED DEFAULT 0,
  `sharecount` int(11) UNSIGNED DEFAULT 0,
  `vip` tinyint(4) DEFAULT NULL,
  `expire` date DEFAULT NULL,
  PRIMARY KEY (`account_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wallet
-- ----------------------------
INSERT INTO `wallet` VALUES ('00000001', 00000005133, 2553, 0, 0, 100, 0, 1, NULL);
INSERT INTO `wallet` VALUES ('00000002', 00000000200, 250, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000003', 00000000280, 200, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000031', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000032', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000033', 00000000000, 0, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000034', 00000000000, 0, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000035', 00000000000, 0, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000036', 00000000967, 1130, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000037', 00000001111, 1111, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000038', 00000000000, 0, 0, 0, 0, 0, NULL, NULL);
INSERT INTO `wallet` VALUES ('00000039', 00000000000, 0, 0, 0, 0, 0, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
