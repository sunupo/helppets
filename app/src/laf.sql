/*
 Navicat Premium Data Transfer

 Source Server         : TT
 Source Server Type    : MySQL
 Source Server Version : 50559
 Source Host           : localhost:3306
 Source Schema         : laf

 Target Server Type    : MySQL
 Target Server Version : 50559
 File Encoding         : 65001

 Date: 26/03/2019 22:51:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apply
-- ----------------------------
DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply`  (
  `applyuid` int(255) NOT NULL,
  `touid` int(255) NOT NULL,
  `dynamicid` int(255) NOT NULL,
  `applyTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `applyStatus` int(1) DEFAULT 1 COMMENT '1：正在申请；2：申请成功；3：申请失败',
  `applyContent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '申请内容',
  `responseTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '同意/拒绝申请的时间',
  `responseContent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '响应内容',
  PRIMARY KEY (`applyuid`, `touid`, `dynamicid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of apply
-- ----------------------------
INSERT INTO `apply` VALUES (1, 2, 1, '2019-03-12-12-20-20', 2, '我孙敬钦，家住沙坪坝沙正街，想要申请领养你的小猫咪，帮你照看一周', '2019-3-25-23-9-21', '可以，我也是重庆江北区的，抽个时间我送过来吧，咱们后面私聊一会儿');
INSERT INTO `apply` VALUES (1, 2, 2, '2019-03-14-12-13-32', 1, '我想申请您家的宠物领养，请不要联系其他人了', '2019-3-25-23-13-52', '好的好的，我需要出过一周，谢谢你帮我照看我的宠物');
INSERT INTO `apply` VALUES (2, 1, 6, '2019-3-25-23-18-57', 1, '我是张翰，我想帮你找看一个月宠物，我也是龙脊小区的', '2019-3-26-14-45-59', '等待回应');
INSERT INTO `apply` VALUES (2, 3, 1, '2019-03-22-9-44-34', 1, '我想申请', '2019-03-23-10-22-33', '等待回应');
INSERT INTO `apply` VALUES (5, 4, 1, '2019-3-25-3-21-31', 1, '我想要', '1970-1-1-0-0-0', '等待回应');

-- ----------------------------
-- Table structure for collectfavorite
-- ----------------------------
DROP TABLE IF EXISTS `collectfavorite`;
CREATE TABLE `collectfavorite`  (
  `fromuid` int(255) NOT NULL,
  `touid` int(255) NOT NULL,
  `dynamicid` int(255) NOT NULL,
  `iscollected` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '未收藏' COMMENT '是否收藏',
  `isfavorite` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '未点赞' COMMENT '是否点赞',
  PRIMARY KEY (`fromuid`, `touid`, `dynamicid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of collectfavorite
-- ----------------------------
INSERT INTO `collectfavorite` VALUES (0, 1, 1, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (0, 1, 2, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (0, 1, 3, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (0, 1, 4, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (0, 1, 5, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (0, 2, 1, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (0, 2, 2, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (0, 2, 28, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (0, 3, 1, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (1, 1, 1, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 1, 1, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 1, 2, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 1, 3, '未收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (2, 1, 4, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 1, 5, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 1, 6, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 2, 1, '未收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (2, 2, 2, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 2, 5, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (2, 2, 28, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 3, 1, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (2, 4, 1, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (3, 1, 1, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (4, 1, 1, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (4, 1, 5, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (4, 2, 5, '未收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (4, 2, 7, '已收藏', '已点赞');
INSERT INTO `collectfavorite` VALUES (4, 2, 8, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (4, 4, 1, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (6, 1, 1, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (6, 1, 3, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (6, 2, 1, '已收藏', '未点赞');
INSERT INTO `collectfavorite` VALUES (6, 4, 1, '已收藏', '未点赞');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `index` int(255) NOT NULL,
  `user0` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`index`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 'sunjingqin', 'sunjingqin', '放松放松');
INSERT INTO `comment` VALUES (2, 'sunjingqin', 'qqq', 'HHHDJDJHSJSH');
INSERT INTO `comment` VALUES (3, 'sunjingqin', 'qqq', 'hdhehudhshehe');
INSERT INTO `comment` VALUES (4, 'sunjingqin', 'qqq', '哎哎哎');

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `fromuid` int(255) NOT NULL,
  `touid` int(255) NOT NULL,
  `dynamicid` int(255) NOT NULL,
  `commentid` int(255) NOT NULL,
  `commentcontent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '评论内容',
  `iscollected` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '未收藏' COMMENT '是否收藏',
  `isfavorite` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '未点赞' COMMENT '是否点赞',
  `views` int(255) DEFAULT 0 COMMENT '浏览次数，默认是0',
  `commenttime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`fromuid`, `touid`, `dynamicid`, `commentid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (1, 2, 1, 1, '用户1对用户2的第1条动态的第1条评论', '已收藏', '已点赞', 10, '2019-03-12-12-20-10');
INSERT INTO `comments` VALUES (1, 2, 1, 2, '用户1对用户2的第1条动态的第2条评论', '已收藏', '已点赞', 10, '2019-03-12-12-20-20');
INSERT INTO `comments` VALUES (1, 2, 2, 1, '用户1对用户2的第2条动态的第1条评论', '已收藏', '已点赞', 20, '2019-03-12-12-30-1');
INSERT INTO `comments` VALUES (2, 1, 1, 1, '你好', '未收藏', '未点赞', 0, '2019-3-22 10:0:40');
INSERT INTO `comments` VALUES (2, 1, 1, 2, '天气不错', '未收藏', '未点赞', 0, '2019-3-22 10:0:57');
INSERT INTO `comments` VALUES (2, 1, 2, 1, '酷兔兔天', '未收藏', '未点赞', 0, '2019-3-22 3:37:29');
INSERT INTO `comments` VALUES (2, 1, 3, 1, '健健康康健健康康', '未收藏', '未点赞', 0, '2019-3-22 18:44:19');
INSERT INTO `comments` VALUES (2, 1, 6, 1, '六级看了看', '未收藏', '未点赞', 0, '2019-3-24 23:18:48');
INSERT INTO `comments` VALUES (2, 1, 6, 2, '你好', '未收藏', '未点赞', 0, '2019-3-24 23:22:35');
INSERT INTO `comments` VALUES (2, 1, 6, 3, 'njjndndndndnd嘻嘻心', '未收藏', '未点赞', 0, '2019-3-26_16-10-30');
INSERT INTO `comments` VALUES (2, 2, 1, 1, '天气这么好，出去遛狗吧', '未收藏', '未点赞', 0, '2019-3-20 22:25:56');
INSERT INTO `comments` VALUES (2, 2, 1, 2, '愿意学习一下', '未收藏', '未点赞', 0, '2019-3-20 22:37:4');
INSERT INTO `comments` VALUES (2, 2, 1, 3, '踏踏不团', '未收藏', '未点赞', 0, '2019-3-20 22:37:33');
INSERT INTO `comments` VALUES (2, 2, 1, 4, 'KKK路来咯', '未收藏', '未点赞', 0, '2019-3-20 22:54:58');
INSERT INTO `comments` VALUES (2, 2, 1, 5, '团', '未收藏', '未点赞', 0, '2019-3-20 22:56:43');
INSERT INTO `comments` VALUES (2, 2, 1, 6, '涂涂乐KKK', '未收藏', '未点赞', 0, '2019-3-20 22:57:59');
INSERT INTO `comments` VALUES (2, 2, 1, 7, 'hhhhh', '未收藏', '未点赞', 0, '2019-3-20 23:49:11');
INSERT INTO `comments` VALUES (2, 2, 1, 8, 'ghhhh', '未收藏', '未点赞', 0, '2019-3-20 23:49:52');
INSERT INTO `comments` VALUES (2, 2, 1, 9, 'bbbbbbnn', '未收藏', '未点赞', 0, '2019-3-21 2:32:5');
INSERT INTO `comments` VALUES (2, 2, 2, 1, '行吧', '未收藏', '未点赞', 0, '2019-3-22 2:54:55');
INSERT INTO `comments` VALUES (2, 4, 1, 1, '好的', '未收藏', '未点赞', 0, '2019-3-24 17:59:2');
INSERT INTO `comments` VALUES (2, 4, 1, 2, '这么憨憨的狗狗', '未收藏', '未点赞', 0, '2019-3-24 17:59:14');
INSERT INTO `comments` VALUES (3, 2, 2, 1, 'gggggvbjjnnb', '未收藏', '未点赞', 0, '2019-3-20 23:57:54');
INSERT INTO `comments` VALUES (3, 2, 2, 2, 'bbbbbbvvbnn', '未收藏', '未点赞', 0, '2019-3-20 23:58:0');
INSERT INTO `comments` VALUES (3, 2, 2, 3, 'hjjnjj', '未收藏', '未点赞', 0, '2019-3-21 0:2:34');
INSERT INTO `comments` VALUES (3, 2, 2, 4, '没理我要美丽', '未收藏', '未点赞', 0, '2019-3-21 0:8:7');

-- ----------------------------
-- Table structure for comments_copy1
-- ----------------------------
DROP TABLE IF EXISTS `comments_copy1`;
CREATE TABLE `comments_copy1`  (
  `fromuid` int(255) NOT NULL,
  `touid` int(255) NOT NULL,
  `dynamicid` int(255) NOT NULL,
  `commentid` int(255) NOT NULL,
  `commentcontent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `iscollected` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '未收藏' COMMENT '是否收藏',
  `isfavorite` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '未点赞' COMMENT '是否点赞',
  `views` int(255) DEFAULT 0 COMMENT '浏览次数，默认是0',
  `commenttime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`fromuid`, `touid`, `dynamicid`, `commentid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of comments_copy1
-- ----------------------------
INSERT INTO `comments_copy1` VALUES (1, 2, 1, 1, '用户1对用户2的第1条动态的第1条评论', '已收藏', '已点赞', 10, '2019-03-12-12-20-10');
INSERT INTO `comments_copy1` VALUES (1, 2, 1, 2, '用户1对用户2的第1条动态的第2条评论', '已收藏', '已点赞', 10, '2019-03-12-12-20-20');
INSERT INTO `comments_copy1` VALUES (1, 2, 2, 1, '用户1对用户2的第2条动态的第1条评论', '已收藏', '已点赞', 20, '2019-03-12-12-30-1');

-- ----------------------------
-- Table structure for context
-- ----------------------------
DROP TABLE IF EXISTS `context`;
CREATE TABLE `context`  (
  `num` int(255) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `text` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `islost` int(1) NOT NULL,
  `isdelete` int(1) UNSIGNED ZEROFILL NOT NULL,
  PRIMARY KEY (`num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of context
-- ----------------------------
INSERT INTO `context` VALUES (1, 'bbbb', 'bbbbb', 'sunjingqin', 1, 0);
INSERT INTO `context` VALUES (2, 'ç®?å??ç??ç??', 'æ??ä¸?æ?¡é»?è?²ç??ç?°å?­ç?¬', 'sunjingqin', 0, 0);
INSERT INTO `context` VALUES (3, 'I have a cat', 'l have  got a cute cat in  last  night', 'qqq', 0, 0);
INSERT INTO `context` VALUES (4, 'Chinese char test', 'this is ä¸­æ??', 'qqq', 1, 1);
INSERT INTO `context` VALUES (5, 'sunupo titlt', 'sunupotext', 'sunupo', 1, 0);
INSERT INTO `context` VALUES (6, 'dgxgxgh', 'chchcu', 'zhangsan', 0, 0);
INSERT INTO `context` VALUES (7, 'NEW TITLE', 'sssssssssssss', 'zhangsan', 1, 0);
INSERT INTO `context` VALUES (8, 'NEW TITLE', 'sssssssssssss', 'zhangsan', 1, 0);
INSERT INTO `context` VALUES (9, 'NEW TITLE', 'sssssssssssss', 'zhangsan', 1, 0);

-- ----------------------------
-- Table structure for dynamic
-- ----------------------------
DROP TABLE IF EXISTS `dynamic`;
CREATE TABLE `dynamic`  (
  `userid` int(255) NOT NULL,
  `dynamicid` int(255) NOT NULL,
  `issend` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type4` int(255) NOT NULL,
  `type5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type6` int(255) NOT NULL,
  `collected` int(255) NOT NULL,
  `favorite` int(255) NOT NULL,
  `views` int(255) NOT NULL,
  `createtime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `picture` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'dynamicpicture/default.bmp' COMMENT '发布动态图片的地址',
  PRIMARY KEY (`userid`, `dynamicid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dynamic
-- ----------------------------
INSERT INTO `dynamic` VALUES (1, 1, '送养', '上班没有时间，像找个重庆的朋友代养一下', '哺乳类', '猫', '加菲猫', 5, '褐色', 1, 2, 3, 86, '2018-03-11-18-30-15', 'dynamicpicture/image_1_1.bmp');
INSERT INTO `dynamic` VALUES (1, 2, '送养', '怕它拆家，家人有队狗斗过敏的。没办法样了，希望找个爱狗人士照顾它', '哺乳类', '狗', '哈士奇', 5, '褐色', 1, 2, 3, 36, '2018-03-11-18-30-15', 'dynamicpicture/image_1_2.bmp');
INSERT INTO `dynamic` VALUES (1, 3, '送养', '非常可爱的小仓鼠，希望有人可以代养一周。由于加班太忙了，怕照顾不好它', '哺乳类', '鼠', '仓鼠', 5, '褐色', 1, 2, 2, 40, '2018-03-11-18-30-15', 'dynamicpicture/image_1_3.bmp');
INSERT INTO `dynamic` VALUES (1, 4, '送养', '上班去了，没时间照顾它。', '哺乳类', '猫', '加菲猫', 10, '黄色', 3, 2, 1, 18, '2019-3-18-19-57-15', 'dynamicpicture/image_1_4.bmp');
INSERT INTO `dynamic` VALUES (1, 5, '送养', '出去旅游，偶遇的蜈蚣没有咩有朋友喜欢的', '节肢类', '昆虫', '蜈蚣', 1, '白色', 11, 2, 3, 22, '2019-3-18-19-59-24', 'dynamicpicture/image_1_5.bmp');
INSERT INTO `dynamic` VALUES (2, 1, '收养', '想找一个两个月大的小猫咪，女朋友喜欢养猫', '哺乳类', '猫', '加菲猫', 5, '褐色', 1, 4, 4, 113, '2018-03-11-18-30-15', 'dynamicpicture/image_2_1.bmp');
INSERT INTO `dynamic` VALUES (2, 2, '收养', '想领养一条“纯正”（手动滑稽）的哈士奇，不怕他拆家，就怕他嫌我笨', '哺乳类', '狗', '哈士奇', 5, '褐色', 1, 3, 3, 42, '2018-03-11-18-30-15', 'dynamicpicture/image_2_2.bmp');
INSERT INTO `dynamic` VALUES (2, 3, '收养', '闲着没事，个人癖好', '节肢类', '昆虫', '蜈蚣', 1, '白色', 1, 0, 0, 3, '2019-3-22-19-10-18', 'dynamicpicture/image_2_3.bmp');
INSERT INTO `dynamic` VALUES (2, 4, '送养', '上班没有时间了', '哺乳类', '狗', '柯基', 1, '白色', 1, 1, 0, 4, '2019-3-22-19-10-45', 'dynamicpicture/image_2_4.bmp');
INSERT INTO `dynamic` VALUES (2, 5, '送养', '即将出国，陪伴了两年的哈士奇想送人代养一下', '哺乳类', '狗', '哈士奇', 10, '白色', 2, 0, 1, 3, '2019-3-23-19-4-1', 'dynamicpicture/image_2_5.bmp');
INSERT INTO `dynamic` VALUES (2, 6, '送养', '九号', '哺乳类', '狗', '来福', 15, '白色', 3, 0, 0, 2, '2019-3-24-17-54-2', 'dynamicpicture/image_2_6.bmp');
INSERT INTO `dynamic` VALUES (2, 7, '送养', '找一只狗狗，在白云路走丢了', '哺乳类', '狗', '哈士奇', 1, '白色', 1, 1, 0, 1, '2019-3-24-18-10-18', 'dynamicpicture/image_2_7.bmp');
INSERT INTO `dynamic` VALUES (2, 8, '送养', '找一只猪，昨晚没回家，在天麒家园', '哺乳类', '猫', '加菲猫', 1, '黑色', 1, 1, 0, 0, '2019-3-24-18-14-44', 'dynamicpicture/image_2_8.bmp');
INSERT INTO `dynamic` VALUES (2, 9, '送养', '有仓鼠嘛', '哺乳类', '猫', '加菲猫', 1, '白色', 1, 0, 0, 0, '2019-3-24-18-17-48', 'dynamicpicture/image_2_9.bmp');
INSERT INTO `dynamic` VALUES (3, 1, '收养', '喜欢斗鹦鹉', '哺乳类', '狗', '鹦鹉', 5, '褐色', 1, 3, 2, 33, '2018-03-11-18-30-15', 'dynamicpicture/image_3_1.bmp');
INSERT INTO `dynamic` VALUES (4, 1, '送养', '旅游的时候遇见一只流浪狗，希望有人能领养它', '哺乳类', '狗', '狗', 10, '黑色', 2, 2, 2, 7, '2019-03-15-18-22-15', 'dynamicpicture/image_4_1.bmp');
INSERT INTO `dynamic` VALUES (5, 2, '送养', '哦哦', '哺乳类', '猫', '加菲猫', 1, '白色', 1, 0, 0, 2, '2019-3-25-12-55-9', 'dynamicpicture/image_5_2.bmp');
INSERT INTO `dynamic` VALUES (5, 3, '送养', '好', '哺乳类', '猫', '加菲猫', 1, '白色', 2, 0, 0, 0, '2019-3-25-13-13-51', 'dynamicpicture/image_5_3.bmp');

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `fromuid` int(255) NOT NULL,
  `touid` int(255) NOT NULL,
  `isfollow` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '已关注' COMMENT '关注表',
  PRIMARY KEY (`fromuid`, `touid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (0, 0, '已关注');
INSERT INTO `follow` VALUES (0, 1, '已关注');
INSERT INTO `follow` VALUES (1, 0, '已关注');
INSERT INTO `follow` VALUES (1, 2, '已关注');
INSERT INTO `follow` VALUES (1, 3, '已关注');
INSERT INTO `follow` VALUES (2, 1, '已关注');
INSERT INTO `follow` VALUES (2, 3, '已关注');
INSERT INTO `follow` VALUES (2, 4, '已关注');
INSERT INTO `follow` VALUES (4, 2, '已关注');
INSERT INTO `follow` VALUES (6, 1, '已关注');
INSERT INTO `follow` VALUES (9, 0, '已关注');
INSERT INTO `follow` VALUES (10, 0, '已关注');

-- ----------------------------
-- Table structure for mailbox
-- ----------------------------
DROP TABLE IF EXISTS `mailbox`;
CREATE TABLE `mailbox`  (
  `num` int(11) NOT NULL,
  `sender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `recipient` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of mailbox
-- ----------------------------
INSERT INTO `mailbox` VALUES (1, 'qqq', 'qqq', 'nihao');

-- ----------------------------
-- Table structure for recommend
-- ----------------------------
DROP TABLE IF EXISTS `recommend`;
CREATE TABLE `recommend`  (
  `userid` int(255) NOT NULL,
  `relevantuserid` int(255) NOT NULL,
  `dynamicid` int(255) NOT NULL,
  `isclick` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`userid`, `relevantuserid`, `dynamicid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of recommend
-- ----------------------------
INSERT INTO `recommend` VALUES (1, 2, 3, '否');

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `fromuid` int(255) NOT NULL,
  `touid` int(255) NOT NULL,
  `dynamicid` int(255) NOT NULL,
  `commentid` int(255) NOT NULL,
  `commentcontent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `replyuid` int(255) NOT NULL,
  `replyid` int(255) NOT NULL,
  `replycontent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `replytime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`fromuid`, `touid`, `dynamicid`, `commentid`, `replyuid`, `replyid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of reply
-- ----------------------------
INSERT INTO `reply` VALUES (1, 2, 1, 1, '用户1对用户2的第1条动态的第1条评论', 3, 1, '在用户2的动态1下，用户3对用户1的第1条评论的第1条回复', '2019-03-12-12-20-10');
INSERT INTO `reply` VALUES (1, 2, 1, 2, '用户1对用户2的第1条动态的第2条评论', 3, 1, '在用户2的动态1下，用户3对用户1的第2条评论的第1条回复', '2019-03-12-12-20-20');
INSERT INTO `reply` VALUES (1, 2, 2, 1, '用户1对用户2的第2条动态的第1条评论', 3, 1, '在用户2的动态2下，用户3对用户1的第1条评论的第1条回复', '2019-03-12-12-30-1');
INSERT INTO `reply` VALUES (2, 1, 1, 0, '', 2, 1, '大家好', '2019-3-22 10:0:47');
INSERT INTO `reply` VALUES (2, 1, 3, 0, '', 2, 1, '将计就计', '2019-3-22 18:44:26');
INSERT INTO `reply` VALUES (2, 1, 6, 3, 'njjndndndndnd嘻嘻心', 2, 1, 'YY一晚自习', '2019-3-26-16-11-6');
INSERT INTO `reply` VALUES (2, 2, 1, 1, '天气这么好，出去遛狗吧', 2, 1, 'hhhbb', '2019-3-21 0:26:11');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `UserId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `PassWord` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `isAdmin` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `isBanned` int(1) UNSIGNED ZEROFILL NOT NULL,
  PRIMARY KEY (`UserId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('qqq', '123', '0', 0);
INSERT INTO `user` VALUES ('sunupo', '123456', '1', 0);
INSERT INTO `user` VALUES ('zhangsan', '123456', '0', 0);

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo`  (
  `userid` int(255) NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `loginname` char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sendAnimal` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `adoptionAnimal` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `birthday` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `age` int(255) DEFAULT NULL,
  `working` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `salary` int(255) DEFAULT NULL,
  `education` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `married` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `jointime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `isBanned` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '否',
  `isAdmin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '否',
  `haschildren` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '中国',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '北京',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '北京',
  `logincount` int(255) DEFAULT 0,
  `logo` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'logo/default.bmp' COMMENT '用户头像',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '电话号码',
  `qq` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '电话号码',
  `wechat` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '电话号码',
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES (0, '系统管理员', 'Admin', '123', '加菲猫,哈士奇', '加菲猫,哈士奇', '男', '1992-11-30', 27, '待就业', 8000, '大学', '已婚', '1970-1-1-0-0-0', '否', '是', '有', '中国', '北京', '北京', 0, 'logo/default.bmp', '0', '0', '0');
INSERT INTO `userinfo` VALUES (1, '孙敬钦', 'sunupo', '123', '加菲猫,哈士奇', '仓鼠', '男', '1985-12-3', 34, '待就业', 8000, '大学', '已婚', '2019-01-01-1-1-1', '是', '否', '有', '中国', '四川', '广安', 34, 'logo/1.bmp', '13222223333', '99887766', 'wechat123');
INSERT INTO `userinfo` VALUES (2, '张三', 'zhangsan', '123', '加菲猫,哈士奇', '仓鼠', '男', '2001-1-15', 18, '待就业', 8000, '硕士', '未婚', '2019-01-01-12-32-33', '否', '是', '有', '中国', '重庆', '重庆', 30, 'logo/2.bmp', '13222223333', '99887766', 'wechat123');
INSERT INTO `userinfo` VALUES (3, '李四', 'lisi', '123', '加菲猫,哈士奇', '仓鼠', '男', '1998-3-4', 21, '待就业', 8000, '硕士', '未婚', '2019-01-02-11-0-0', '否', '是', '有', '中国', '重庆', '重庆', 30, 'logo/3.bmp', '15974653746', 'qq1232134', 'wechat32323');
INSERT INTO `userinfo` VALUES (4, '王五', 'wangwu', '123', '加菲猫,哈士奇', '仓鼠', '男', '1980-7-22', 39, '待就业', 8000, '硕士', '未婚', '2019-01-02-12-0-0', '否', '是', '有', '中国', '北京', '北京', 30, 'logo/4.bmp', '15536485674', 'qq131231', 'wechat244');

-- ----------------------------
-- View structure for v_apply_dunamic
-- ----------------------------
DROP VIEW IF EXISTS `v_apply_dunamic`;
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `v_apply_dunamic` AS select `a`.`applyuid` AS `applyuid`,`a`.`applyTime` AS `applyTime`,`a`.`applyStatus` AS `applyStatus`,`a`.`applyContent` AS `applyContent`,`a`.`responseTime` AS `responseTime`,`a`.`responseContent` AS `responseContent`,`d`.`userid` AS `userid`,`d`.`dynamicid` AS `dynamicid`,`d`.`issend` AS `issend`,`d`.`content` AS `content`,`d`.`type1` AS `type1`,`d`.`type2` AS `type2`,`d`.`type3` AS `type3`,`d`.`type4` AS `type4`,`d`.`type5` AS `type5`,`d`.`type6` AS `type6`,`d`.`collected` AS `collected`,`d`.`favorite` AS `favorite`,`d`.`views` AS `views`,`d`.`createtime` AS `createtime`,`d`.`picture` AS `picture` from (`apply` `a` left join `dynamic` `d` on(((`a`.`touid` = `d`.`userid`) and (`a`.`dynamicid` = `d`.`dynamicid`))));

-- ----------------------------
-- View structure for v_apply_dynamic
-- ----------------------------
DROP VIEW IF EXISTS `v_apply_dynamic`;
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `v_apply_dynamic` AS select `a`.`applyuid` AS `applyuid`,`a`.`applyTime` AS `applyTime`,`a`.`applyStatus` AS `applyStatus`,`a`.`applyContent` AS `applyContent`,`a`.`responseTime` AS `responseTime`,`a`.`responseContent` AS `responseContent`,`d`.`userid` AS `userid`,`d`.`dynamicid` AS `dynamicid`,`d`.`issend` AS `issend`,`d`.`content` AS `content`,`d`.`type1` AS `type1`,`d`.`type2` AS `type2`,`d`.`type3` AS `type3`,`d`.`type4` AS `type4`,`d`.`type5` AS `type5`,`d`.`type6` AS `type6`,`d`.`collected` AS `collected`,`d`.`favorite` AS `favorite`,`d`.`views` AS `views`,`d`.`createtime` AS `createtime`,`d`.`picture` AS `picture` from (`apply` `a` left join `dynamic` `d` on(((`a`.`touid` = `d`.`userid`) and (`a`.`dynamicid` = `d`.`dynamicid`))));

SET FOREIGN_KEY_CHECKS = 1;
