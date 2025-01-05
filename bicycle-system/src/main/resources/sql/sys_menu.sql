/*
 Navicat Premium Data Transfer

 Source Server         : medical
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : medical

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 05/01/2025 00:05:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(0) NOT NULL DEFAULT 0 COMMENT '上级菜单',
  `menu_type` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '权限类型: M=目录，C=菜单，A=按钮',
  `menu_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单名称',
  `menu_icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单图标',
  `menu_sort` smallint(0) NOT NULL DEFAULT 0 COMMENT '菜单排序',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '权限标识',
  `paths` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '前端组件',
  `selected` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选中路径',
  `params` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路由参数',
  `is_cache` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否缓存: 0=否, 1=是',
  `is_show` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否显示: 0=否, 1=是',
  `is_disable` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否禁用: 0=否, 1=是',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单管理表' ROW_FORMAT = Dynamic;

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
INSERT INTO `sys_menu` VALUES (25, 0, 'C', '自行车管理', 'local-icon-list-2', 100, 'bicycle:list', 'bicycle', '/index', '', '', 1, 1, 0, '2024-02-20 20:37:21', '2025-01-04 23:47:15');
INSERT INTO `sys_menu` VALUES (42, 0, 'M', '组织管理', 'el-icon-OfficeBuilding', 93, '', 'department', '', '', '', 0, 1, 0, '2024-02-25 22:44:29', '2024-02-26 23:08:41');
INSERT INTO `sys_menu` VALUES (43, 42, 'C', '部门管理', 'el-icon-School', 0, 'department:deptList', 'deptList', 'department/deptList/index', '', '', 0, 1, 0, '2024-02-25 22:46:07', '2024-02-25 22:46:33');
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

SET FOREIGN_KEY_CHECKS = 1;
