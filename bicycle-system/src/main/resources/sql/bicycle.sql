/*
 Navicat Premium Data Transfer

 Source Server         : bicycle
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : bicycle

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 16/01/2025 16:05:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bicycle_info
-- ----------------------------
DROP TABLE IF EXISTS `bicycle_info`;
CREATE TABLE `bicycle_info`  (
  `id` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'id',
  `model` bigint NOT NULL COMMENT '型号',
  `image` varchar(10240) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'X光图片',
  `frame_no` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '车架号',
  `produce_time` datetime NOT NULL COMMENT '生产日期',
  `qrcode` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '二维码编码',
  `qr_img` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '二维码',
  `conclusion` tinyint(1) NULL DEFAULT NULL COMMENT '结论：1-通过；0-不通过',
  `remark` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bicycle_info
-- ----------------------------
INSERT INTO `bicycle_info` VALUES ('BY20250116068657', 1, 'http://127.0.0.1/static/file/1737037463A区-1-0.jpg', '123534534', '2025-01-16 00:00:00', 'OAOKHXZO1JP8A0ZOYQYTH1BTOVFFXY1G', 'D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static\\2025\\01\\16\\OAOKHXZO1JP8A0ZOYQYTH1BTOVFFXY1G.png', 1, '测试', 0, '2025-01-16 14:54:58', '2025-01-16 14:54:58', NULL);
INSERT INTO `bicycle_info` VALUES ('BY20250116083280', 1, 'http://127.0.0.1/static/file/1737049911A区-1-0.jpg', '1312312321', '2025-01-15 00:00:00', 'DZDJGEWR76FL8KW7PFN8EPMU9Z226LNH', 'D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static\\2025\\01\\16\\DZDJGEWR76FL8KW7PFN8EPMU9Z226LNH.png', 1, '测试数据', 0, '2025-01-16 15:02:54', '2025-01-16 15:02:54', NULL);
INSERT INTO `bicycle_info` VALUES ('BY20250116181465', 1, 'http://127.0.0.1/static/file/1737048855A区-1-0.jpg', '123213123', '2025-01-08 00:00:00', '25JZPSNKY6MAM4OCGDEK9MHU7CAT3CDC', 'D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static\\2025\\01\\16\\25JZPSNKY6MAM4OCGDEK9MHU7CAT3CDC.png', 1, '测试', 0, '2025-01-16 14:41:01', '2025-01-16 14:41:01', NULL);
INSERT INTO `bicycle_info` VALUES ('BY20250116351954', 1, 'http://127.0.0.1/static/file/1737021853A区-1-0.jpg', '123456', '2025-01-16 00:00:00', 'H1RVBFO96W5OMBI1EK57LB3PNVG3O4AG', 'D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static\\2025\\01\\16\\H1RVBFO96W5OMBI1EK57LB3PNVG3O4AG.png', 1, '测试数据', 0, '2025-01-16 14:25:06', '2025-01-16 14:25:06', NULL);
INSERT INTO `bicycle_info` VALUES ('BY20250116403771', 1, 'http://127.0.0.1/static/file/1737084641A区-1-0.jpg', '12345', '2025-01-16 00:00:00', 'UL3HJDHE9ZRPEAHJL18OSRYZ0D74Q5WV', 'D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static\\2025\\01\\16\\UL3HJDHE9ZRPEAHJL18OSRYZ0D74Q5WV.png', 1, '测试数据', 0, '2025-01-16 14:38:47', '2025-01-16 14:38:47', NULL);
INSERT INTO `bicycle_info` VALUES ('BY20250116737457', 1, 'http://127.0.0.1/static/file/1737076550A区-1-0.jpg', '3232423423424', '2025-01-16 00:00:00', '7O6PXT7SF0SC4FQENINAQXXMV5BMI64G', 'D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static\\2025\\01\\16\\7O6PXT7SF0SC4FQENINAQXXMV5BMI64G.png', 1, '测试数据', 0, '2025-01-16 15:07:38', '2025-01-16 15:07:38', NULL);
INSERT INTO `bicycle_info` VALUES ('BY20250116749962', 1, 'http://127.0.0.1/static/file/1737060745A区-1-0.jpg', '213123213213213', '2025-01-16 00:00:00', 'QBN02NR237MRMZ32V8IP2XV5C9LL0VNB', 'https://127.0.0.1/static/2025/01/16/QBN02NR237MRMZ32V8IP2XV5C9LL0VNB.png', 1, '这是测试数据', 0, '2025-01-16 15:25:29', '2025-01-16 15:25:29', NULL);
INSERT INTO `bicycle_info` VALUES ('BY20250116778377', 1, 'http://127.0.0.1/static/file/1737079088A区-1-0.jpg', '1212332323243242', '2025-01-16 00:00:00', '20XBWTHKRBRGH96JQP1FSFR6DY7587ZI', 'D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static\\2025\\01\\16\\20XBWTHKRBRGH96JQP1FSFR6DY7587ZI.png', 1, '测试数据', 0, '2025-01-16 14:59:13', '2025-01-16 14:59:13', NULL);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '值类型',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表单label',
  `value` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'sys', 'filings', '网站备案链接', '', '网站备案链接', '2024-01-10 22:44:41', '2025-01-16 15:32:29');
INSERT INTO `sys_config` VALUES (2, 'sys', 'webLogo', '登录页LOGO', 'https://winspace-bikes.com//images/master/2022/9/131-83EENS.png', '登录界面LOGO', '2024-01-10 22:44:41', '2025-01-16 15:32:46');
INSERT INTO `sys_config` VALUES (3, 'sys', 'webName', '登录页系统名称', '银斯贝自行车管理系统', '网站名称', '2024-01-10 22:44:41', '2025-01-16 15:33:09');
INSERT INTO `sys_config` VALUES (4, 'sys', 'webFavicon', '网站LOGO', 'https://winspace-bikes.com//images/master/2022/9/131-83EENS.png', '网站logo', '2024-01-10 22:44:41', '2025-01-16 15:33:15');
INSERT INTO `sys_config` VALUES (5, 'qrcode', 'qrcodeWidth', '二维码生成的宽度', '400', '生成的二维码宽度', '2025-01-15 23:26:10', '2025-01-16 16:00:26');
INSERT INTO `sys_config` VALUES (6, 'qrcode', 'qrcodeHeight', '二维码生成的高度', '400', '生成的二维码高度', '2025-01-15 23:26:26', '2025-01-16 16:00:33');
INSERT INTO `sys_config` VALUES (7, 'sys', 'theme', '管理后台主题颜色', '#2682FC', '系统主题颜色', '2025-01-15 23:27:13', '2025-01-16 15:34:37');
INSERT INTO `sys_config` VALUES (8, 'official', 'queryImages', '公众号查询界面图片', 'https://winspace-bikes.com//images/master/2022/9/131-83EENS.png', '公众号查询界面图片列表', '2025-01-15 23:27:44', '2025-01-16 16:02:29');
INSERT INTO `sys_config` VALUES (9, 'sys', 'loginBg', '管理后台登录背景图', 'https://winspace-bikes.com//images/master/2022/9/131-83EENS.png', '登录界面背景图', '2025-01-15 23:30:10', '2025-01-16 15:35:23');
INSERT INTO `sys_config` VALUES (10, 'qrcode', 'qrcodeFontSize', '二维码上文字大小', '16', '生成的二维码图片下方的文字大小', '2025-01-16 14:44:28', '2025-01-16 16:01:08');
INSERT INTO `sys_config` VALUES (11, 'qrcode', 'qrcodeMargin', '二维码距离边框的距离', '4', '二维码和文字的间距', '2025-01-16 14:44:59', '2025-01-16 16:00:41');
INSERT INTO `sys_config` VALUES (12, 'qrcode', 'qrcodeImgServerUrl', '二维码上传服务器地址', 'https://127.0.0.1/', '二维码保存的服务器地址，结尾必须添加/', '2025-01-16 15:15:41', '2025-01-16 16:00:40');
INSERT INTO `sys_config` VALUES (13, 'official', 'mobileTheme', '公众号主题色', '#2682FC', '公众号查询界面的主题色', '2025-01-16 15:22:53', '2025-01-16 16:02:37');
INSERT INTO `sys_config` VALUES (14, 'qrcode', 'qrcodeFilePath', '二维码上传路径', 'D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static', '二维码图片保存的文件夹路径，windows环境l路径分隔符请使用反斜杠', '2025-01-16 15:26:58', '2025-01-16 16:00:35');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` int NULL DEFAULT NULL COMMENT '上级部门id',
  `dept_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '部门名称',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序，数值越大，顺序月靠前',
  `duty` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '部门负责人',
  `mobile` char(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '部门负责人手机号',
  `is_stop` tinyint NOT NULL DEFAULT 0 COMMENT '部门状态，0：未禁用，1：已禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '部门信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, NULL, '超级部门', 0, '超级管理员', '15772725805', 0, '2025-01-09 14:22:34', '2025-01-09 14:22:34');

-- ----------------------------
-- Table structure for sys_dept_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_role`;
CREATE TABLE `sys_dept_role`  (
  `id` bigint NULL DEFAULT NULL,
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  INDEX `fk_dept_id_dept_role`(`dept_id` ASC) USING BTREE,
  INDEX `fk_role_id_dept_role`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fk_dept_id_dept_role` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_role_id_dept_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_user`;
CREATE TABLE `sys_dept_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_dept_user`(`dept_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `fk_dept_id`(`dept_id` ASC) USING BTREE,
  INDEX `fk_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_dept_user_dept_id` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_dept_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `dict_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字典名称',
  `dict_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字典类型',
  `dict_status` int NOT NULL DEFAULT 1 COMMENT '字典状态：0-禁用, 1-正常',
  `dict_remark` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dict_name`(`dict_name` ASC) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, '型号', 'frameNo', 1, '自行车型号', '2025-01-16 13:53:45', '2025-01-16 13:53:45');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字典类型，外键',
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '数据名称',
  `value` bigint NOT NULL COMMENT '数据值',
  `state` int NOT NULL DEFAULT 1 COMMENT '数据状态：0-禁用，1-启用',
  `remark` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '数据备注',
  `sort` bigint NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_dict_type`(`dict_type` ASC) USING BTREE,
  CONSTRAINT `fk_dict_type` FOREIGN KEY (`dict_type`) REFERENCES `sys_dict` (`dict_type`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 'frameNo', '山地自行车', 1, 1, '', 0);
INSERT INTO `sys_dict_data` VALUES (2, 'frameNo', '儿童自行车', 2, 1, '', 0);
INSERT INTO `sys_dict_data` VALUES (3, 'frameNo', '城市自行车', 3, 1, '', 0);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint NOT NULL DEFAULT 0 COMMENT '上级菜单',
  `menu_type` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '权限类型: M=目录，C=菜单，A=按钮',
  `menu_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单名称',
  `menu_icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单图标',
  `menu_sort` smallint NOT NULL DEFAULT 0 COMMENT '菜单排序',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '权限标识',
  `paths` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '前端组件',
  `selected` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选中路径',
  `params` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路由参数',
  `is_cache` tinyint NOT NULL DEFAULT 0 COMMENT '是否缓存: 0=否, 1=是',
  `is_show` tinyint NOT NULL DEFAULT 1 COMMENT '是否显示: 0=否, 1=是',
  `is_disable` tinyint NOT NULL DEFAULT 0 COMMENT '是否禁用: 0=否, 1=是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 145 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (2, 0, 'M', '权限管理', 'el-icon-Lock', 92, '', 'permission', '', '', '', 0, 1, 0, '2024-01-20 23:42:17', '2024-02-26 23:09:05');
INSERT INTO `sys_menu` VALUES (3, 2, 'C', '菜单管理', 'el-icon-Operation', 0, 'system:menu:list', 'menu', 'permission/menu/index', '', '', 1, 1, 0, '2024-01-20 23:42:17', '2024-01-20 23:42:17');
INSERT INTO `sys_menu` VALUES (4, 2, 'C', '用户管理', 'el-icon-UserFilled', 0, 'permission:user:list', 'user', 'permission/user/index', '', '', 0, 1, 0, '2024-01-23 23:33:04', '2024-04-14 16:42:37');
INSERT INTO `sys_menu` VALUES (5, 2, 'C', '角色管理', 'el-icon-Female', 0, 'permission:role:list', 'role', 'permission/role/index', '', '', 0, 1, 0, '2024-01-23 23:34:36', '2024-01-24 22:11:10');
INSERT INTO `sys_menu` VALUES (6, 0, 'M', '开发工具', 'local-icon-bianji', 91, '', 'tools', '', '', '', 0, 1, 0, '2024-01-24 22:52:42', '2024-02-26 23:09:28');
INSERT INTO `sys_menu` VALUES (7, 6, 'C', '字典管理', 'el-icon-Box', 0, 'tools:dictionary:list', 'dictionary', 'tools/dictionary/index', '', '', 0, 1, 0, '2024-01-24 22:54:15', '2024-01-26 21:27:31');
INSERT INTO `sys_menu` VALUES (8, 3, 'A', '查询', '', 0, 'system:menus', '', '', '', '', 0, 1, 0, '2024-01-24 22:56:31', '2024-01-26 21:18:32');
INSERT INTO `sys_menu` VALUES (9, 3, 'A', '新增', '', 0, 'system:menu:add', '', '', '', '', 0, 1, 0, '2024-01-24 23:19:56', '2024-04-14 16:41:54');
INSERT INTO `sys_menu` VALUES (10, 3, 'A', '编辑', '', 0, 'system:menu:edit', '', '', '', '', 0, 1, 0, '2024-01-24 23:20:48', '2024-04-14 16:42:00');
INSERT INTO `sys_menu` VALUES (11, 3, 'A', '删除', '', 0, 'system:menu:del', '', '', '', '', 0, 1, 0, '2024-01-24 23:21:26', '2024-04-14 16:42:05');
INSERT INTO `sys_menu` VALUES (12, 3, 'A', '详情', '', 0, 'system:menu:detail', '', '', '', '', 0, 1, 0, '2024-01-24 23:22:38', '2024-04-14 16:42:12');
INSERT INTO `sys_menu` VALUES (13, 7, 'A', '新增', '', 0, 'dict:add', '', '', '', '', 0, 1, 0, '2024-01-26 21:28:12', '2024-04-14 16:55:47');
INSERT INTO `sys_menu` VALUES (14, 7, 'A', '编辑', '', 0, 'dict:edit', '', '', '', '', 0, 1, 0, '2024-01-26 21:28:32', '2024-04-14 16:56:24');
INSERT INTO `sys_menu` VALUES (15, 7, 'A', '查询', '', 0, 'dict:list', '', '', '', '', 0, 1, 0, '2024-01-26 21:28:47', '2024-04-14 16:55:01');
INSERT INTO `sys_menu` VALUES (16, 7, 'A', '删除', '', 0, 'dict:delete', '', '', '', '', 0, 1, 0, '2024-01-26 21:29:10', '2024-04-14 16:56:47');
INSERT INTO `sys_menu` VALUES (17, 6, 'C', '字典数据管理', '', 0, 'tools:dictdata:list', 'dictionary/data', 'tools/dictdata/index', '', '', 0, 0, 0, '2024-01-26 21:33:29', '2024-01-26 21:35:23');
INSERT INTO `sys_menu` VALUES (18, 17, 'A', '查询', '', 0, 'dict:dataList', '', '', '', '', 0, 1, 0, '2024-01-26 21:36:18', '2024-04-14 16:57:18');
INSERT INTO `sys_menu` VALUES (19, 17, 'A', '新增', '', 0, 'dict:addData', '', '', '', '', 0, 1, 0, '2024-01-26 21:36:28', '2024-04-14 16:58:29');
INSERT INTO `sys_menu` VALUES (20, 17, 'A', '编辑', '', 0, 'dict:editData', '', '', '', '', 0, 1, 0, '2024-01-26 21:36:37', '2024-04-14 16:58:42');
INSERT INTO `sys_menu` VALUES (21, 17, 'A', '删除', '', 0, 'dict:deleteData', '', '', '', '', 0, 1, 0, '2024-01-26 21:36:44', '2024-04-14 16:58:58');
INSERT INTO `sys_menu` VALUES (25, 0, 'C', '自行车管理', 'local-icon-list-2', 100, 'bicycle:list', 'bicycle', '/index', '', '', 1, 1, 0, '2024-02-20 20:37:21', '2025-01-05 15:39:26');
INSERT INTO `sys_menu` VALUES (42, 0, 'M', '组织管理', 'el-icon-OfficeBuilding', 93, '', 'department', '', '', '', 0, 1, 0, '2024-02-25 22:44:29', '2025-01-10 18:27:47');
INSERT INTO `sys_menu` VALUES (43, 42, 'C', '部门管理', 'el-icon-School', 0, 'department:deptList', 'deptList', 'department/deptList/index', '', '', 0, 1, 0, '2024-02-25 22:46:07', '2025-01-10 18:27:56');
INSERT INTO `sys_menu` VALUES (114, 43, 'A', '查询', '', 0, 'system:deptList', '', '', '', '', 0, 1, 0, '2024-04-14 16:35:31', '2024-04-14 16:35:31');
INSERT INTO `sys_menu` VALUES (115, 43, 'A', '新增', '', 0, 'system:deptAdd', '', '', '', '', 0, 1, 0, '2024-04-14 16:35:47', '2024-04-14 16:35:47');
INSERT INTO `sys_menu` VALUES (116, 43, 'A', '编辑', '', 0, 'system:deptEdit', '', '', '', '', 0, 1, 0, '2024-04-14 16:36:03', '2024-04-14 16:36:03');
INSERT INTO `sys_menu` VALUES (117, 43, 'A', '删除', '', 0, 'system:deptDelete', '', '', '', '', 0, 1, 0, '2024-04-14 16:36:21', '2024-04-14 16:36:21');
INSERT INTO `sys_menu` VALUES (118, 43, 'A', '详情', '', 0, 'system:deptDetail', '', '', '', '', 0, 1, 0, '2024-04-14 16:36:38', '2024-04-14 16:36:38');
INSERT INTO `sys_menu` VALUES (119, 4, 'A', '查询', '', 0, 'system:userLists', '', '', '', '', 0, 1, 0, '2024-04-14 16:43:43', '2024-04-14 16:43:43');
INSERT INTO `sys_menu` VALUES (120, 4, 'A', '新增', '', 0, 'system:addUser', '', '', '', '', 0, 1, 0, '2024-04-14 16:44:03', '2024-04-14 16:44:03');
INSERT INTO `sys_menu` VALUES (121, 4, 'A', '编辑', '', 0, 'system:editUser', '', '', '', '', 0, 1, 0, '2024-04-14 16:44:19', '2024-04-14 16:44:19');
INSERT INTO `sys_menu` VALUES (122, 4, 'A', '详情', '', 0, 'system:userDetail', '', '', '', '', 0, 1, 0, '2024-04-14 16:44:38', '2024-04-14 16:44:38');
INSERT INTO `sys_menu` VALUES (123, 4, 'A', '删除', '', 0, 'system:deleteUser', '', '', '', '', 0, 1, 0, '2024-04-14 16:44:53', '2024-04-14 16:44:53');
INSERT INTO `sys_menu` VALUES (124, 4, 'A', '切换状态', '', 0, 'system:userStatus', '', '', '', '', 0, 1, 0, '2024-04-14 16:45:10', '2024-04-14 16:45:10');
INSERT INTO `sys_menu` VALUES (125, 5, 'A', '查询', '', 0, 'system:roleLists', '', '', '', '', 0, 1, 0, '2024-04-14 16:46:34', '2024-04-14 16:46:34');
INSERT INTO `sys_menu` VALUES (126, 5, 'A', '查询所有角色', '', 0, 'system:roleAll', '', '', '', '', 0, 1, 0, '2024-04-14 16:47:00', '2024-04-14 16:47:00');
INSERT INTO `sys_menu` VALUES (127, 5, 'A', '新增', '', 0, 'system:addRole', '', '', '', '', 0, 1, 0, '2024-04-14 16:47:16', '2024-04-14 16:47:16');
INSERT INTO `sys_menu` VALUES (128, 5, 'A', '编辑', '', 0, 'system:editRole', '', '', '', '', 0, 1, 0, '2024-04-14 16:47:32', '2024-04-14 16:47:32');
INSERT INTO `sys_menu` VALUES (129, 5, 'A', '详情', '', 0, 'system:roleDetail', '', '', '', '', 0, 1, 0, '2024-04-14 16:47:51', '2024-04-14 16:47:51');
INSERT INTO `sys_menu` VALUES (130, 5, 'A', '删除', '', 0, 'system:roleDelete', '', '', '', '', 0, 1, 0, '2024-04-14 16:48:14', '2024-04-14 16:48:14');
INSERT INTO `sys_menu` VALUES (131, 5, 'A', '角色授权', '', 0, 'system:roleAuth', '', '', '', '', 0, 1, 0, '2024-04-14 16:49:02', '2024-04-14 16:49:02');
INSERT INTO `sys_menu` VALUES (132, 5, 'A', '获取角色权限列表', '', 0, 'system:roleMenus', '', '', '', '', 0, 1, 0, '2024-04-14 16:49:31', '2024-04-14 16:49:31');
INSERT INTO `sys_menu` VALUES (133, 17, 'A', '获取所有字典', '', 0, 'dict:all', '', '', '', '', 0, 1, 0, '2024-04-14 16:55:30', '2024-04-14 16:55:30');
INSERT INTO `sys_menu` VALUES (134, 7, 'A', '详情', '', 0, 'dict:detail', '', '', '', '', 0, 1, 0, '2024-04-14 16:56:09', '2024-04-14 16:56:09');
INSERT INTO `sys_menu` VALUES (135, 17, 'A', '获取所有数据', '', 0, 'dict:allDictData', '', '', '', '', 0, 1, 0, '2024-04-14 16:58:13', '2024-04-14 16:58:13');
INSERT INTO `sys_menu` VALUES (136, 17, 'A', '详情', '', 0, 'dict:dataDetail', '', '', '', '', 0, 1, 0, '2024-04-14 16:59:21', '2024-04-14 16:59:21');
INSERT INTO `sys_menu` VALUES (137, 7, 'A', '数据管理', '', 0, 'dict:dataList', '', '', '', '', 0, 1, 0, '2024-04-14 17:00:23', '2024-04-14 17:00:44');
INSERT INTO `sys_menu` VALUES (138, 25, 'A', '新增', '', 0, 'bicycle:add', '', '', '', '', 0, 1, 0, '2025-01-05 00:37:06', '2025-01-05 00:37:06');
INSERT INTO `sys_menu` VALUES (139, 25, 'A', '查询', '', 0, 'bicycle:list', '', '', '', '', 0, 1, 0, '2025-01-05 00:37:28', '2025-01-05 00:37:28');
INSERT INTO `sys_menu` VALUES (140, 25, 'A', '编辑', '', 0, 'bicycle:edit', '', '', '', '', 0, 1, 0, '2025-01-05 00:37:50', '2025-01-05 00:37:50');
INSERT INTO `sys_menu` VALUES (141, 25, 'A', '删除', '', 0, 'bicycle:delete', '', '', '', '', 0, 1, 0, '2025-01-05 00:38:11', '2025-01-05 00:38:11');
INSERT INTO `sys_menu` VALUES (142, 25, 'A', '批量导入', '', 0, 'bicycle:import', '', '', '', '', 0, 1, 0, '2025-01-05 00:38:42', '2025-01-05 00:38:42');
INSERT INTO `sys_menu` VALUES (143, 25, 'A', '批量导出', '', 0, 'bicycle:export', '', '', '', '', 0, 1, 0, '2025-01-05 00:39:02', '2025-01-05 00:39:02');
INSERT INTO `sys_menu` VALUES (144, 25, 'A', '详情', '', 0, 'bicycle:detail', '', '', '', '', 0, 1, 0, '2025-01-09 14:25:49', '2025-01-09 14:25:49');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `sort` bigint NOT NULL DEFAULT 0 COMMENT '排序',
  `role_count` bigint NOT NULL DEFAULT 0 COMMENT '管理员人数',
  `role_state` tinyint NOT NULL DEFAULT 0 COMMENT '管理员状态：0-正常；1-禁用',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 0, 0, 0, '', '2025-01-09 14:23:00');
INSERT INTO `sys_role` VALUES (2, 'ces', 0, 0, 0, '', '2025-01-10 13:45:30');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_role_id_role_menu`(`role_id` ASC) USING BTREE,
  INDEX `fk_menu_id_role_menu`(`menu_id` ASC) USING BTREE,
  CONSTRAINT `fk_menu_id_role_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_role_id_role_menu` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名称',
  `password` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录密码',
  `avatar` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `email` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `account` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录账号，可使用手机号登录',
  `user_state` tinyint NULL DEFAULT 0 COMMENT '用户状态，0：正常，1：禁用',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `account`(`account` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '123456', 'https://tse2-mm.cn.bing.net/th/id/OIP-C.51RJal6KkFmCkblE6KOfJQAAAA?rs=1&pid=ImgDetMain', '2564888970@qq.com', 'admin', 0, '超级管理员', '2024-02-29 20:38:34', '2025-01-05 00:41:04');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_id`(`user_id` ASC) USING BTREE,
  INDEX `fk_role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fk_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
