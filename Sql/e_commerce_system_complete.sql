SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `after_sale_ticket`;
DROP TABLE IF EXISTS `inventory`;
DROP TABLE IF EXISTS `logistics`;
DROP TABLE IF EXISTS `payment_settlement`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `rfq_award_notice`;
DROP TABLE IF EXISTS `rfq_business_award`;
DROP TABLE IF EXISTS `rfq_demand`;
DROP TABLE IF EXISTS `rfq_quotation`;
DROP TABLE IF EXISTS `rfq_supplier_quote`;
DROP TABLE IF EXISTS `rfq_supplier_rel`;
DROP TABLE IF EXISTS `sales_order`;
DROP TABLE IF EXISTS `supplier`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_notification`;
DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `wf_approval_record`;
DROP TABLE IF EXISTS `wf_todo_task`;

CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(200) DEFAULT NULL,
  `real_name` VARCHAR(50) NOT NULL,
  `role_code` VARCHAR(20) NOT NULL,
  `dept_id` BIGINT DEFAULT NULL,
  `status` INT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `supplier` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(50) DEFAULT NULL,
  `name` VARCHAR(200) DEFAULT NULL,
  `contact` VARCHAR(50) DEFAULT NULL,
  `grade` VARCHAR(10) DEFAULT NULL,
  `score` INT DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sku` VARCHAR(100) DEFAULT NULL,
  `name` VARCHAR(200) DEFAULT NULL,
  `category` VARCHAR(100) DEFAULT NULL,
  `unit` VARCHAR(20) DEFAULT NULL,
  `price` DECIMAL(10,2) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_sku` (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `inventory` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sku` VARCHAR(100) DEFAULT NULL,
  `warehouse` VARCHAR(100) DEFAULT NULL,
  `available` INT DEFAULT NULL,
  `locked` INT DEFAULT NULL,
  `alert` INT DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sales_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(100) DEFAULT NULL,
  `customer` VARCHAR(200) DEFAULT NULL,
  `product` VARCHAR(200) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `pay_status` VARCHAR(20) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `logistics` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(100) DEFAULT NULL,
  `order_no` VARCHAR(100) DEFAULT NULL,
  `carrier` VARCHAR(100) DEFAULT NULL,
  `route` VARCHAR(200) DEFAULT NULL,
  `eta` DATETIME DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_logistics_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `payment_settlement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(100) DEFAULT NULL,
  `order_no` VARCHAR(100) DEFAULT NULL,
  `channel` VARCHAR(100) DEFAULT NULL,
  `amount` DECIMAL(10,2) DEFAULT NULL,
  `account_date` VARCHAR(20) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `after_sale_ticket` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(100) DEFAULT NULL,
  `order_no` VARCHAR(100) DEFAULT NULL,
  `type` VARCHAR(50) DEFAULT NULL,
  `owner` VARCHAR(50) DEFAULT NULL,
  `reason` VARCHAR(500) DEFAULT NULL,
  `status` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_after_sale_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rfq_demand` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `demand_no` VARCHAR(50) NOT NULL,
  `category_code` VARCHAR(50) NOT NULL,
  `quantity` DECIMAL(18,2) NOT NULL,
  `specification` TEXT,
  `deadline` DATETIME NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  `assignee_id` BIGINT DEFAULT NULL,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_demand_no` (`demand_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rfq_quotation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `rfq_no` VARCHAR(50) NOT NULL,
  `demand_id` BIGINT DEFAULT NULL,
  `category_code` VARCHAR(50) NOT NULL,
  `quantity` DECIMAL(18,2) NOT NULL,
  `specification` TEXT,
  `delivery_date` DATETIME NOT NULL,
  `payment_terms` VARCHAR(200) DEFAULT NULL,
  `quote_deadline` DATETIME NOT NULL,
  `source_type` VARCHAR(20) NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  `attachment` VARCHAR(500) DEFAULT NULL,
  `created_by` BIGINT NOT NULL,
  `remark` VARCHAR(500) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rfq_no` (`rfq_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rfq_supplier_quote` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `rfq_id` BIGINT NOT NULL,
  `supplier_id` BIGINT NOT NULL,
  `quote_type` VARCHAR(20) NOT NULL,
  `unit_price` DECIMAL(18,2) NOT NULL,
  `total_price` DECIMAL(18,2) NOT NULL,
  `delivery_date` DATETIME NOT NULL,
  `warranty_period` VARCHAR(100) DEFAULT NULL,
  `payment_terms` VARCHAR(200) DEFAULT NULL,
  `batch_price_gradient` TEXT,
  `cost_breakdown` TEXT,
  `attachment` VARCHAR(500) DEFAULT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED',
  `submitted_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rfq_supplier` (`rfq_id`, `supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rfq_supplier_rel` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `rfq_id` BIGINT NOT NULL,
  `supplier_id` BIGINT NOT NULL,
  `is_selected` INT NOT NULL DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rfq_business_award` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `award_no` VARCHAR(50) NOT NULL,
  `rfq_id` BIGINT NOT NULL,
  `supplier_id` BIGINT NOT NULL,
  `analysis_report` VARCHAR(500) DEFAULT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'APPROVING',
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_award_no` (`award_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rfq_award_notice` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `notice_no` VARCHAR(50) NOT NULL,
  `award_id` BIGINT NOT NULL,
  `supplier_id` BIGINT NOT NULL,
  `title` VARCHAR(200) NOT NULL,
  `content` TEXT NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  `confirm_remark` VARCHAR(500) DEFAULT NULL,
  `confirmed_at` DATETIME DEFAULT NULL,
  `generated_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_notice_no` (`notice_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `wf_approval_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `biz_type` VARCHAR(30) NOT NULL,
  `biz_id` BIGINT NOT NULL,
  `approver_id` BIGINT NOT NULL,
  `action` VARCHAR(20) NOT NULL,
  `opinion` VARCHAR(500) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `wf_todo_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `biz_type` VARCHAR(30) NOT NULL,
  `biz_id` BIGINT NOT NULL,
  `title` VARCHAR(200) NOT NULL,
  `priority` VARCHAR(10) NOT NULL DEFAULT 'MEDIUM',
  `deadline` DATETIME DEFAULT NULL,
  `is_done` INT NOT NULL DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sys_notification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `recipient_id` BIGINT NOT NULL,
  `title` VARCHAR(200) NOT NULL,
  `content` VARCHAR(500) NOT NULL,
  `biz_type` VARCHAR(30) DEFAULT NULL,
  `biz_id` BIGINT DEFAULT NULL,
  `is_read` INT NOT NULL DEFAULT 0,
  `read_at` DATETIME DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT DEFAULT 0,
  `menu_name` VARCHAR(100) NOT NULL,
  `path` VARCHAR(200) DEFAULT NULL,
  `component` VARCHAR(200) DEFAULT NULL,
  `icon` VARCHAR(100) DEFAULT NULL,
  `sort_order` INT DEFAULT 0,
  `visible` INT DEFAULT 1,
  `permission` VARCHAR(200) DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sys_role_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `role_code` VARCHAR(50) NOT NULL,
  `menu_id` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_code`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET @pwd = '$2a$10$N7/UKpyDZy5gVoF.3FAh1ej/uQ0SoJeCh.Xvx5DrFEd5/cjyZxw4W';

INSERT INTO `sys_user` (`id`,`username`,`password`,`real_name`,`role_code`,`dept_id`,`status`) VALUES
(100,'admin',@pwd,'系统管理员','JS03',1000,1),
(101,'engineer',@pwd,'李明','JS01',1001,1),
(102,'wangfang',@pwd,'王芳','JS01',1001,1),
(103,'leader',@pwd,'赵强','JS02',1001,1),
(104,'director',@pwd,'孙伟','JS03',1001,1),
(107,'warehouse',@pwd,'郑丽','JS06',1003,1),
(201,'supplier_huawei',@pwd,'华为供应-陈工','JS05',2001,1),
(202,'supplier',@pwd,'小米供应-刘工','JS05',2002,1),
(203,'supplier_oppo',@pwd,'OPPO供应-张工','JS05',2003,1),
(204,'supplier_vivo',@pwd,'VIVO供应-黄工','JS05',2004,1),
(205,'supplier_apple',@pwd,'苹果供应-赵工','JS05',2005,1);

INSERT INTO `supplier` (`id`,`code`,`name`,`contact`,`grade`,`score`,`status`) VALUES
(201,'SUP-HW','华为供应链有限公司','陈工','A',96,'正常'),
(202,'SUP-MI','小米智能供应有限公司','刘工','A',98,'正常'),
(203,'SUP-OP','OPPO器件供应中心','张工','B',89,'正常'),
(204,'SUP-VI','VIVO电子材料有限公司','黄工','B',86,'观察'),
(205,'SUP-AP','苹果精密器件供应商','赵工','A',95,'正常');

INSERT INTO `product` (`id`,`sku`,`name`,`category`,`unit`,`price`,`status`) VALUES
(1,'DDR4-16G-3200','DDR4 16GB 内存条','ELEC-001','件',280.00,'上架'),
(2,'TYPEC-100W-1M','Type-C 100W 数据线','ELEC-002','条',18.50,'上架'),
(3,'AMOLED-667-120HZ','AMOLED 6.67英寸显示模组','ELEC-003','片',310.00,'上架'),
(4,'CNC-FRAME-AL','手机中框 CNC 加工件','MACH-001','件',45.00,'上架');

INSERT INTO `inventory` (`sku`,`warehouse`,`available`,`locked`,`alert`,`status`) VALUES
('DDR4-16G-3200','华东一号仓',80,20,100,'预警'),
('TYPEC-100W-1M','华南二号仓',21000,6000,30000,'预警'),
('AMOLED-667-120HZ','华东一号仓',222,12,60,'正常'),
('CNC-FRAME-AL','华北中心仓',5100,300,1000,'正常');

INSERT INTO `rfq_demand` VALUES
(1,'DEM-20260316-001','ELEC-001',5000,'DDR4 16GB 内存条，频率3200MHz，要求原厂颗粒；预算：￥1,500,000.00','2026-04-15 18:00:00','COMPLETED',101,101,'2026-04-01 09:00:00'),
(2,'DEM-20260316-002','ELEC-002',10000,'Type-C 3.1 数据线，长度1米，支持 PD 100W 快充；预算：￥220,000.00','2026-04-20 18:00:00','PENDING',101,101,'2026-04-03 10:30:00'),
(3,'DEM-20260316-003','MACH-001',2000,'手机中框 CNC 加工件，铝合金材质，公差±0.02mm；预算：￥120,000.00','2026-04-18 18:00:00','PENDING',101,102,'2026-04-05 14:00:00');

INSERT INTO `rfq_quotation` VALUES
(1,'RFQ-20260316-001',1,'ELEC-001',5000,'DDR4 16GB 内存条，频率3200MHz，要求原厂颗粒','2026-05-30 00:00:00','交付后60天付清','2026-04-10 12:00:00','INQUIRY','APPROVED',NULL,101,'询比价采购，至少3家供应商参与','2026-04-06 09:00:00',NULL),
(2,'RFQ-20260316-002',2,'ELEC-002',10000,'Type-C 3.1 数据线，长度1米，支持 PD 100W 快充','2026-06-15 00:00:00','预付30%，验收后付70%','2026-04-15 12:00:00','INQUIRY','DRAFT',NULL,101,'草稿待完善','2026-04-08 10:00:00',NULL),
(3,'RFQ-20260316-003',3,'MACH-001',2000,'手机中框 CNC 加工件，铝合金材质，公差±0.02mm','2026-05-10 00:00:00','货到验收后30天付清','2026-04-18 12:00:00','INQUIRY','APPROVING',NULL,101,'等待组长审批','2026-04-09 10:00:00',NULL);

INSERT INTO `rfq_supplier_rel` (`rfq_id`,`supplier_id`,`is_selected`) VALUES
(1,201,1),(1,202,1),(1,203,1),(2,202,1),(2,204,1),(3,205,1);

INSERT INTO `rfq_supplier_quote` VALUES
(1,1,201,'L2',280.00,1400000.00,'2026-05-25 00:00:00','3年质保','交付后60天付清','{"moq":1000}',NULL,NULL,'SUBMITTED','2026-04-09 10:30:00'),
(2,1,202,'L2',275.00,1375000.00,'2026-05-28 00:00:00','3年质保','验收后90天付清','{"moq":800}',NULL,NULL,'SUBMITTED','2026-04-09 14:00:00'),
(3,1,203,'L1',295.00,1475000.00,'2026-05-22 00:00:00','2年质保','预付50%尾款到付','{"moq":1200}',NULL,NULL,'SUBMITTED','2026-04-09 16:45:00');

INSERT INTO `rfq_business_award` VALUES
(1,'BA-20260410-001',1,202,'系统按单价排序，小米供应报价 275 元，总价 137.5 万元，综合评分第一。','APPROVING',101,'2026-04-10 15:00:00');

INSERT INTO `rfq_award_notice` VALUES
(1,'NT-20260410-001',1,202,'DDR4内存条采购定点通知书','经询比价评审，贵司报价总额137.5万元，交期5月28日，综合评分第一。现确认贵司为本项目合作供应商。','PENDING',NULL,NULL,'2026-04-10 16:00:00');

INSERT INTO `wf_approval_record` (`biz_type`,`biz_id`,`approver_id`,`action`,`opinion`,`created_at`) VALUES
('DEMAND',1,103,'APPROVE','需求合理，同意发布 RFQ。','2026-04-06 08:30:00'),
('RFQ',1,103,'APPROVE','供应商资质齐全，价格在预算范围内。','2026-04-08 09:00:00'),
('RFQ',1,104,'APPROVE','询比价模式符合规定，供应商清单合规。','2026-04-08 11:00:00');

INSERT INTO `wf_todo_task` (`user_id`,`biz_type`,`biz_id`,`title`,`priority`,`deadline`,`is_done`) VALUES
(101,'QUOTE_ALERT',1,'RFQ-20260316-001 报价汇总完成，请查看','HIGH','2026-04-10 18:00:00',0),
(103,'DEMAND',2,'寻源需求 DEM-20260316-002 待审核','HIGH','2026-04-20 18:00:00',0),
(104,'AWARD_APPROVAL',1,'商务定点单 BA-20260410-001 待最终审批','HIGH','2026-04-15 18:00:00',0),
(202,'QUOTE_ALERT',1,'RFQ-20260316-001 报价已提交，请跟踪定点状态','MEDIUM','2026-04-12 12:00:00',0),
(107,'INVENTORY_ALERT',1,'华东一号仓 DDR4 库存低于预警阈值','HIGH','2026-04-12 18:00:00',0);

INSERT INTO `sys_notification` (`recipient_id`,`title`,`content`,`biz_type`,`biz_id`,`is_read`) VALUES
(101,'报价汇总完成','RFQ-20260316-001 已收到 3 家供应商报价，请进入报价汇总页选择定点供应商。','QUOTE_ALERT',1,0),
(103,'寻源需求待审核','DEM-20260316-002 等待品类组长审核。','DEMAND',2,0),
(104,'商务定点待审批','李明提交了 BA-20260410-001，意向供应商：小米供应。','AWARD_APPROVAL',1,0),
(202,'收到 RFQ 公告','您收到 RFQ-20260316-001，请按截止时间提交报价。','RFQ',1,1),
(107,'库存预警','DDR4 16GB 内存条库存低于阈值，请安排补货或调拨。','INVENTORY_ALERT',1,0);

INSERT INTO `sales_order` (`code`,`customer`,`product`,`amount`,`pay_status`,`status`) VALUES
('PO-20260410-001','采购中心','DDR4 16GB 内存条',1375000.00,'待付款','待入库');

INSERT INTO `payment_settlement` (`code`,`order_no`,`channel`,`amount`,`account_date`,`status`) VALUES
('PAY-20260410-001','PO-20260410-001','企业网银',1375000.00,'2026-04-30','待付款');

INSERT INTO `logistics` (`code`,`order_no`,`carrier`,`route`,`eta`,`status`) VALUES
('LOG-20260410-001','PO-20260410-001','顺丰供应链','小米供应 -> 华东一号仓','2026-05-28 18:00:00','待调度');

INSERT INTO `after_sale_ticket` (`code`,`order_no`,`type`,`owner`,`reason`,`status`) VALUES
('AS-20260412-001','PO-20260410-001','质量异常','王芳','抽检发现少量外包装破损，等待供应商确认。','处理中');

INSERT INTO `sys_menu` (`id`,`parent_id`,`menu_name`,`path`,`component`,`icon`,`sort_order`,`visible`,`permission`) VALUES
(1,0,'工作台','/dashboard','Dashboard','LayoutDashboard',1,1,'dashboard:view'),
(2,0,'寻源定点','/sourcing',NULL,'Search',2,1,'sourcing:view'),
(3,0,'供应商管理','/suppliers','supplier/SupplierList','Users',3,1,'supplier:list'),
(4,0,'商品库','/products','product/ProductList','Goods',4,1,'product:list'),
(5,0,'采购订单','/orders','purchase/OrderList','ShoppingCart',5,1,'purchase:order:list'),
(6,0,'仓储库存','/inventory','warehouse/Inventory','Package',6,1,'warehouse:inventory'),
(7,0,'物流调度','/logistics','warehouse/Logistics','Truck',7,1,'warehouse:logistics'),
(8,0,'付款结算','/payments','purchase/PaymentList','Wallet',8,1,'purchase:payment:list'),
(9,0,'售后工单','/aftersales','afterSales/TicketList','Service',9,1,'aftersales:list'),
(10,0,'用户与角色','/users','system/UserList','User',10,1,'system:user:list');

INSERT INTO `sys_role_menu` (`role_code`,`menu_id`) VALUES
('JS01',1),('JS01',2),('JS01',4),('JS01',6),
('JS02',1),('JS02',2),('JS02',4),('JS02',6),
('JS03',1),('JS03',2),('JS03',3),('JS03',4),('JS03',5),('JS03',6),('JS03',7),('JS03',8),('JS03',9),('JS03',10),
('JS04',1),('JS04',2),('JS04',3),('JS04',4),('JS04',5),('JS04',6),('JS04',7),('JS04',8),('JS04',9),('JS04',10),
('JS05',1),('JS05',2),
('JS06',1),('JS06',6),('JS06',7);

SET FOREIGN_KEY_CHECKS = 1;
