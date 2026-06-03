/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80043
Source Host           : localhost:3306
Source Database       : e_commerce_system

Target Server Type    : MYSQL
Target Server Version : 80043
File Encoding         : 65001

Date: 2026-04-26 15:39:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rfq_award_notice
-- ----------------------------
DROP TABLE IF EXISTS `rfq_award_notice`;
CREATE TABLE `rfq_award_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知书ID',
  `notice_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知书编号',
  `award_id` bigint NOT NULL COMMENT '关联定点单ID',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `title` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知书标题',
  `content` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知书内容(含价格/交期/权责)',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING待确认/CONFIRMED已确认/REJECTED已拒绝',
  `confirm_remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商确认备注',
  `confirmed_at` datetime DEFAULT NULL COMMENT '确认时间',
  `generated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_notice_no` (`notice_no`),
  KEY `idx_supplier_id` (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='定点通知书表';

-- ----------------------------
-- Records of rfq_award_notice
-- ----------------------------
INSERT INTO `rfq_award_notice` VALUES ('1', 'NT-20260410-001', '1', '202', 'DDR4内存条采购定点通知书', '经询比价评审，贵司报价总额137.5万元，交期5月28日，综合评分第一。现确认贵司为本项目合作供应商。', 'PENDING', null, null, '2026-04-26 15:21:09');

-- ----------------------------
-- Table structure for rfq_business_award
-- ----------------------------
DROP TABLE IF EXISTS `rfq_business_award`;
CREATE TABLE `rfq_business_award` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '定点单ID',
  `award_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '定点单编号',
  `rfq_id` bigint NOT NULL COMMENT '关联询价单ID',
  `supplier_id` bigint NOT NULL COMMENT '拟定点供应商ID',
  `analysis_report` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '询比价分析报告附件路径',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DRAFT' COMMENT '状态: DRAFT草稿/APPROVING审批中/APPROVED已通过/REJECTED已驳回',
  `created_by` bigint NOT NULL COMMENT '申请人ID(JS01)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_award_no` (`award_no`),
  KEY `idx_rfq_id` (`rfq_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商务定点单表';

-- ----------------------------
-- Records of rfq_business_award
-- ----------------------------
INSERT INTO `rfq_business_award` VALUES ('1', 'BA-20260410-001', '1', '202', '/reports/analysis_rfq_001_20260410.pdf', 'APPROVING', '101', '2026-04-26 15:21:09');

-- ----------------------------
-- Table structure for rfq_demand
-- ----------------------------
DROP TABLE IF EXISTS `rfq_demand`;
CREATE TABLE `rfq_demand` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '需求ID',
  `demand_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '需求编号',
  `category_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '采购品类编码',
  `quantity` decimal(18,2) NOT NULL COMMENT '采购数量',
  `specification` text COLLATE utf8mb4_general_ci COMMENT '规格描述',
  `deadline` datetime NOT NULL COMMENT '需求截止时间',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING待接收/ASSIGNED已分配/REJECTED已拒绝/COMPLETED已完成',
  `assignee_id` bigint DEFAULT NULL COMMENT '被指派的品类工程师ID',
  `created_by` bigint NOT NULL COMMENT '发起人ID(JS04)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_assignee` (`assignee_id`),
  KEY `idx_created_by` (`created_by`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='寻源需求表';

-- ----------------------------
-- Records of rfq_demand
-- ----------------------------
INSERT INTO `rfq_demand` VALUES ('1', 'DEM-20260316-001', 'ELEC-001', '5000.00', 'DDR4 16GB 内存条，频率3200MHz，要求原厂颗粒', '2026-04-15 18:00:00', 'COMPLETED', '101', '105', '2026-04-26 15:21:09');
INSERT INTO `rfq_demand` VALUES ('2', 'DEM-20260316-002', 'ELEC-002', '10000.00', 'Type-C 3.1 数据线，长度1米，支持PD 100W快充', '2026-04-20 18:00:00', 'ASSIGNED', '102', '106', '2026-04-26 15:21:09');
INSERT INTO `rfq_demand` VALUES ('3', 'DEM-20260316-003', 'MACH-001', '2000.00', '手机中框CNC加工件，铝合金材质，公差±0.02mm', '2026-04-18 18:00:00', 'PENDING', '108', '105', '2026-04-26 15:21:09');
INSERT INTO `rfq_demand` VALUES ('4', 'DEM-20260316-004', 'ELEC-003', '3000.00', 'AMOLED 6.67英寸显示模组，分辨率2400x1080，120Hz刷新率', '2026-04-25 18:00:00', 'PENDING', '109', '106', '2026-04-26 15:21:09');

-- ----------------------------
-- Table structure for rfq_quotation
-- ----------------------------
DROP TABLE IF EXISTS `rfq_quotation`;
CREATE TABLE `rfq_quotation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '询价单ID',
  `rfq_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'RFQ编号',
  `demand_id` bigint DEFAULT NULL COMMENT '关联寻源需求ID',
  `category_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '采购品类编码',
  `quantity` decimal(18,2) NOT NULL COMMENT '数量',
  `specification` text COLLATE utf8mb4_general_ci COMMENT '规格参数',
  `delivery_date` datetime NOT NULL COMMENT '交期要求',
  `payment_terms` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '付款条件',
  `quote_deadline` datetime NOT NULL COMMENT '报价截止时间',
  `source_type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '寻源方式: INQUIRY询比价/SINGLE单一来源',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DRAFT' COMMENT '状态: DRAFT草稿/APPROVING审批中/APPROVED已通过/REJECTED已驳回/PUBLISHED已发布',
  `attachment` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附件路径',
  `created_by` bigint NOT NULL COMMENT '创建人ID(JS01)',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rfq_no` (`rfq_no`),
  KEY `idx_created_by` (`created_by`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='询价单表';

-- ----------------------------
-- Records of rfq_quotation
-- ----------------------------
INSERT INTO `rfq_quotation` VALUES ('1', 'RFQ-20260316-001', '1', 'ELEC-001', '5000.00', 'DDR4 16GB 内存条，频率3200MHz，要求原厂颗粒', '2026-05-30 00:00:00', '交付后60天付清', '2026-04-10 12:00:00', 'INQUIRY', 'APPROVED', null, '101', '询比价采购，至少3家供应商参与', '2026-04-26 15:21:09', null);
INSERT INTO `rfq_quotation` VALUES ('2', 'RFQ-20260316-002', '2', 'ELEC-002', '10000.00', 'Type-C 3.1 数据线，长度1米，支持PD 100W快充', '2026-06-15 00:00:00', '预付30%，验收后付70%', '2026-04-15 12:00:00', 'INQUIRY', 'DRAFT', null, '102', '草稿待完善', '2026-04-26 15:21:09', null);
INSERT INTO `rfq_quotation` VALUES ('3', 'RFQ-20260316-003', '1', 'ELEC-001', '1000.00', 'DDR5 16GB 内存条，频率5600MHz，要求原厂颗粒，供试样测试用', '2026-04-30 00:00:00', '货到付款', '2026-04-05 12:00:00', 'SINGLE', 'APPROVING', null, '101', '单一来源采购，战略供应商试样', '2026-04-26 15:21:09', null);

-- ----------------------------
-- Table structure for rfq_supplier_quote
-- ----------------------------
DROP TABLE IF EXISTS `rfq_supplier_quote`;
CREATE TABLE `rfq_supplier_quote` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报价ID',
  `rfq_id` bigint NOT NULL COMMENT '询价单ID',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `quote_type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '报价类型: L1/L2/L3/SAMPLE_L1',
  `unit_price` decimal(18,2) NOT NULL COMMENT '单价',
  `total_price` decimal(18,2) NOT NULL COMMENT '总价',
  `delivery_date` datetime NOT NULL COMMENT '交期',
  `warranty_period` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '质保期限',
  `payment_terms` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '付款条件',
  `batch_price_gradient` text COLLATE utf8mb4_general_ci COMMENT '批量价格梯度(JSON)',
  `cost_breakdown` text COLLATE utf8mb4_general_ci COMMENT '成本拆解(JSON)',
  `attachment` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '报价附件路径',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SUBMITTED' COMMENT '状态: DRAFT暂存/SUBMITTED已提交/LOCKED已锁定',
  `submitted_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`id`),
  KEY `idx_rfq_supplier` (`rfq_id`,`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='供应商报价表';

-- ----------------------------
-- Records of rfq_supplier_quote
-- ----------------------------
INSERT INTO `rfq_supplier_quote` VALUES ('1', '1', '201', 'L2', '280.00', '1400000.00', '2026-05-25 00:00:00', '3年质保', '交付后60天付清', null, null, null, 'SUBMITTED', '2026-04-09 10:30:00');
INSERT INTO `rfq_supplier_quote` VALUES ('2', '1', '202', 'L2', '275.00', '1375000.00', '2026-05-28 00:00:00', '3年质保', '验收后90天付清', null, null, null, 'SUBMITTED', '2026-04-09 14:00:00');
INSERT INTO `rfq_supplier_quote` VALUES ('3', '1', '203', 'L1', '295.00', '1475000.00', '2026-05-22 00:00:00', '2年质保', '预付50%尾款到付', null, null, null, 'SUBMITTED', '2026-04-09 16:45:00');
INSERT INTO `rfq_supplier_quote` VALUES ('4', '3', '205', 'L3', '350.00', '350000.00', '2026-04-28 00:00:00', '5年质保', '货到验收后30天付清', null, null, null, 'SUBMITTED', '2026-04-04 09:00:00');

-- ----------------------------
-- Table structure for rfq_supplier_rel
-- ----------------------------
DROP TABLE IF EXISTS `rfq_supplier_rel`;
CREATE TABLE `rfq_supplier_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `rfq_id` bigint NOT NULL COMMENT '询价单ID',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `is_selected` tinyint NOT NULL DEFAULT '1' COMMENT '是否被选中: 1是 0否',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_rfq_id` (`rfq_id`),
  KEY `idx_supplier_id` (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='询价单-供应商关联表';

-- ----------------------------
-- Records of rfq_supplier_rel
-- ----------------------------
INSERT INTO `rfq_supplier_rel` VALUES ('1', '1', '201', '1', '2026-04-26 15:21:09');
INSERT INTO `rfq_supplier_rel` VALUES ('2', '1', '202', '1', '2026-04-26 15:21:09');
INSERT INTO `rfq_supplier_rel` VALUES ('3', '1', '203', '1', '2026-04-26 15:21:09');
INSERT INTO `rfq_supplier_rel` VALUES ('4', '2', '202', '1', '2026-04-26 15:21:09');
INSERT INTO `rfq_supplier_rel` VALUES ('5', '2', '204', '1', '2026-04-26 15:21:09');
INSERT INTO `rfq_supplier_rel` VALUES ('6', '3', '205', '1', '2026-04-26 15:21:09');

-- ----------------------------
-- Table structure for sys_notification
-- ----------------------------
DROP TABLE IF EXISTS `sys_notification`;
CREATE TABLE `sys_notification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `recipient_id` bigint NOT NULL COMMENT '接收人ID',
  `title` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知标题',
  `content` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知内容',
  `biz_type` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联业务类型',
  `biz_id` bigint DEFAULT NULL COMMENT '关联业务ID',
  `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '是否已读: 1是 0否',
  `read_at` datetime DEFAULT NULL COMMENT '阅读时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_recipient_read` (`recipient_id`,`is_read`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息通知表';

-- ----------------------------
-- Records of sys_notification
-- ----------------------------
INSERT INTO `sys_notification` VALUES ('1', '101', '寻源需求待办通知', '您收到一条新的寻源需求【DEM-20260316-001】，品类：DDR4内存条，数量5000件，请在2026-03-20前处理。', 'DEMAND', '1', '1', null, '2026-04-26 15:21:09');
INSERT INTO `sys_notification` VALUES ('2', '101', 'RFQ审批通过通知', '您的询价单【RFQ-20260316-001】已审批通过，已推送至供应商中心，请关注后续报价。', 'RFQ_APPROVAL', '1', '0', null, '2026-04-26 15:21:09');
INSERT INTO `sys_notification` VALUES ('3', '202', '收到新询价单', '您收到一份新询价单【RFQ-20260316-001】，品类：DDR4内存条，报价截止时间：2026-04-10 12:00，请及时处理。', 'QUOTE_ALERT', '1', '1', null, '2026-04-26 15:21:09');
INSERT INTO `sys_notification` VALUES ('4', '104', '商务定点待审批', '品类工程师李明提交了商务定点申请【BA-20260410-001】，意向供应商：小米供应，请及时审批。', 'AWARD_APPROVAL', '1', '0', null, '2026-04-26 15:21:09');
INSERT INTO `sys_notification` VALUES ('5', '205', '定点通知书推送', '恭喜！您已被确认为DDR5内存条项目的合作供应商，请登录供应商中心查看定点通知书详情并确认合作。', 'COOPERATION_CONFIRM', '1', '0', null, '2026-04-26 15:21:09');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
  `real_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `role_code` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码: JS01品类工程师/JS02品类组长/JS03采购处长/JS05供应商操作人员',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 1启用 0禁用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('101', 'liming', '李明', 'JS01', '1001', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('102', 'wangfang', '王芳', 'JS01', '1001', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('103', 'zhaoqiang', '赵强', 'JS02', '1001', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('104', 'sunwei', '孙伟', 'JS03', '1001', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('105', 'zhoujie', '周杰', 'JS04', '1002', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('106', 'wujing', '吴婧', 'JS04', '1002', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('107', 'zhengli', '郑丽', 'JS06', '1003', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('108', 'liuqiang', '刘强', 'JS01', '1001', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('109', 'huangli', '黄丽', 'JS01', '1001', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('201', 'supplier_huawei_chen', '华为供应-陈工', 'JS05', '2001', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('202', 'supplier_xiaomi_liu', '小米供应-刘工', 'JS05', '2002', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('203', 'supplier_oppo_zhang', 'OPPO供应-张工', 'JS05', '2003', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('204', 'supplier_vivo_huang', 'VIVO供应-黄工', 'JS05', '2004', '1', '2026-04-26 15:21:09');
INSERT INTO `sys_user` VALUES ('205', 'supplier_apple_zhao', '苹果供应-赵工', 'JS05', '2005', '1', '2026-04-26 15:21:09');

-- ----------------------------
-- Table structure for wf_approval_record
-- ----------------------------
DROP TABLE IF EXISTS `wf_approval_record`;
CREATE TABLE `wf_approval_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '审批ID',
  `biz_type` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型: RFQ/商务定点',
  `biz_id` bigint NOT NULL COMMENT '业务单据ID',
  `approver_id` bigint NOT NULL COMMENT '审批人ID',
  `action` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '审批操作: APPROVE通过/REJECT驳回/DELEGATE加签',
  `opinion` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审批意见',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审批时间',
  PRIMARY KEY (`id`),
  KEY `idx_biz` (`biz_type`,`biz_id`),
  KEY `idx_approver` (`approver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='审批记录表';

-- ----------------------------
-- Records of wf_approval_record
-- ----------------------------
INSERT INTO `wf_approval_record` VALUES ('1', 'RFQ', '1', '103', 'APPROVE', '供应商资质齐全，价格在预算范围内，同意通过。', '2026-04-08 09:00:00');
INSERT INTO `wf_approval_record` VALUES ('2', 'RFQ', '1', '104', 'APPROVE', '询比价模式符合规定，供应商清单合规，同意。', '2026-04-08 11:00:00');
INSERT INTO `wf_approval_record` VALUES ('3', 'RFQ', '3', '103', 'APPROVE', '供应商为战略合作伙伴，单一来源理由充分，初审通过。', '2026-04-04 10:00:00');

-- ----------------------------
-- Table structure for wf_todo_task
-- ----------------------------
DROP TABLE IF EXISTS `wf_todo_task`;
CREATE TABLE `wf_todo_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '待办ID',
  `user_id` bigint NOT NULL COMMENT '待办人ID',
  `biz_type` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型: DEMAND寻源需求/RFQ_APPROVAL审批/QUOTE_ALERT报价提醒/AWARD_APPROVAL定点审批/COOPERATION_CONFIRM合作确认',
  `biz_id` bigint NOT NULL COMMENT '业务单据ID',
  `title` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '待办标题',
  `priority` varchar(10) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级: HIGH/MEDIUM/LOW',
  `deadline` datetime DEFAULT NULL COMMENT '截止时间',
  `is_done` tinyint NOT NULL DEFAULT '0' COMMENT '是否已处理: 1是 0否',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_done` (`user_id`,`is_done`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工作台待办表';

-- ----------------------------
-- Records of wf_todo_task
-- ----------------------------
INSERT INTO `wf_todo_task` VALUES ('1', '101', 'DEMAND', '1', '寻源需求 DEM-20260316-001 - DDR4内存条采购', 'HIGH', '2026-03-20 18:00:00', '1', '2026-04-26 15:21:09');
INSERT INTO `wf_todo_task` VALUES ('2', '101', 'QUOTE_ALERT', '1', 'RFQ-20260316-001 报价汇总完成，请查看', 'HIGH', '2026-04-10 18:00:00', '0', '2026-04-26 15:21:09');
INSERT INTO `wf_todo_task` VALUES ('3', '102', 'DEMAND', '2', '寻源需求 DEM-20260316-002 - Type-C数据线采购', 'MEDIUM', '2026-03-25 18:00:00', '0', '2026-04-26 15:21:09');
INSERT INTO `wf_todo_task` VALUES ('4', '103', 'RFQ_APPROVAL', '1', 'RFQ-20260316-001 待审批 - DDR4内存条', 'HIGH', '2026-04-07 18:00:00', '1', '2026-04-26 15:21:09');
INSERT INTO `wf_todo_task` VALUES ('5', '103', 'RFQ_APPROVAL', '3', 'RFQ-20260316-003 待审批 - DDR5内存条（单一来源）', 'HIGH', '2026-04-04 18:00:00', '0', '2026-04-26 15:21:09');
INSERT INTO `wf_todo_task` VALUES ('6', '104', 'RFQ_APPROVAL', '1', 'RFQ-20260316-001 高级审批 - DDR4内存条', 'HIGH', '2026-04-08 12:00:00', '1', '2026-04-26 15:21:09');
INSERT INTO `wf_todo_task` VALUES ('7', '104', 'AWARD_APPROVAL', '1', '商务定点单 BA-20260410-001 待审批 - 小米供应', 'HIGH', '2026-04-15 18:00:00', '0', '2026-04-26 15:21:09');
INSERT INTO `wf_todo_task` VALUES ('8', '202', 'QUOTE_ALERT', '1', 'RFQ-20260316-001 待报价提醒 - DDR4内存条', 'HIGH', '2026-04-09 12:00:00', '1', '2026-04-26 15:21:09');
INSERT INTO `wf_todo_task` VALUES ('9', '205', 'COOPERATION_CONFIRM', '1', '定点通知书 NT-20260410-001 待确认 - 苹果供应', 'LOW', '2026-04-20 18:00:00', '0', '2026-04-26 15:21:09');
