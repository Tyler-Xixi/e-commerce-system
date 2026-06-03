CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT 0,
    menu_name VARCHAR(100) NOT NULL,
    path VARCHAR(200),
    component VARCHAR(200),
    icon VARCHAR(100),
    sort_order INT DEFAULT 0,
    visible TINYINT DEFAULT 1,
    permission VARCHAR(200),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_code VARCHAR(50) NOT NULL,
    menu_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_menu (role_code, menu_id)
);

INSERT INTO sys_menu (parent_id, menu_name, path, component, icon, sort_order, visible, permission) VALUES
(0, 'Dashboard', '/dashboard', 'Dashboard', 'LayoutDashboard', 1, 1, NULL),
(0, 'Sourcing', '/sourcing', NULL, 'Search', 2, 1, NULL),
(0, 'Supplier', '/supplier', NULL, 'Users', 3, 1, NULL),
(0, 'Warehouse', '/warehouse', NULL, 'Package', 4, 1, NULL),
(0, 'Purchase', '/purchase', NULL, 'ShoppingCart', 5, 1, NULL),
(0, 'System', '/system', NULL, 'Settings', 6, 1, NULL);

INSERT INTO sys_menu (parent_id, menu_name, path, component, icon, sort_order, visible, permission) VALUES
(1, 'Todo Tasks', '/dashboard/todo', 'dashboard/Todo', 'Clock', 1, 1, 'dashboard:todo'),
(1, 'Approval Records', '/dashboard/approval', 'dashboard/Approval', 'FileCheck', 2, 1, 'dashboard:approval');

INSERT INTO sys_menu (parent_id, menu_name, path, component, icon, sort_order, visible, permission) VALUES
(2, 'Demands', '/sourcing/demands', 'sourcing/DemandList', 'FileText', 1, 1, 'sourcing:demand:list'),
(2, 'RFQ', '/sourcing/rfq', 'sourcing/RfqList', 'MessageSquare', 2, 1, 'sourcing:rfq:list'),
(2, 'Quotes Summary', '/sourcing/quotes', 'sourcing/QuoteSummary', 'BarChart3', 3, 1, 'sourcing:quote:summary'),
(2, 'Award', '/sourcing/award', 'sourcing/AwardList', 'CheckCircle', 4, 1, 'sourcing:award:list');

INSERT INTO sys_menu (parent_id, menu_name, path, component, icon, sort_order, visible, permission) VALUES
(3, 'Supplier List', '/supplier/list', 'supplier/SupplierList', 'Users', 1, 1, 'supplier:list'),
(3, 'RFQ Notice', '/supplier/rfq', 'supplier/RfqNotice', 'Bell', 2, 1, 'supplier:rfq:view'),
(3, 'My Quotes', '/supplier/quotes', 'supplier/MyQuotes', 'FileEdit', 3, 1, 'supplier:quote:manage');

INSERT INTO sys_menu (parent_id, menu_name, path, component, icon, sort_order, visible, permission) VALUES
(4, 'Inventory', '/warehouse/inventory', 'warehouse/Inventory', 'Package', 1, 1, 'warehouse:inventory'),
(4, 'Warehouse In', '/warehouse/warehouse-in', 'warehouse/WarehouseIn', 'Download', 2, 1, 'warehouse:in'),
(4, 'Warehouse Out', '/warehouse/warehouse-out', 'warehouse/WarehouseOut', 'Upload', 3, 1, 'warehouse:out'),
(4, 'Logistics', '/warehouse/logistics', 'warehouse/Logistics', 'Truck', 4, 1, 'warehouse:logistics');

INSERT INTO sys_menu (parent_id, menu_name, path, component, icon, sort_order, visible, permission) VALUES
(5, 'Orders', '/purchase/orders', 'purchase/OrderList', 'FileText', 1, 1, 'purchase:order:list'),
(5, 'Contracts', '/purchase/contracts', 'purchase/ContractList', 'FileCheck', 2, 1, 'purchase:contract:list'),
(5, 'Payment', '/purchase/payment', 'purchase/PaymentList', 'Wallet', 3, 1, 'purchase:payment:list');

INSERT INTO sys_menu (parent_id, menu_name, path, component, icon, sort_order, visible, permission) VALUES
(6, 'Users', '/system/users', 'system/UserList', 'User', 1, 1, 'system:user:manage'),
(6, 'Roles', '/system/roles', 'system/RoleList', 'Shield', 2, 1, 'system:role:manage'),
(6, 'Menus', '/system/menus', 'system/MenuList', 'List', 3, 1, 'system:menu:manage');

INSERT INTO sys_role_menu (role_code, menu_id) VALUES
('JS01', 1), ('JS01', 2), ('JS01', 3), ('JS01', 4), ('JS01', 5), ('JS01', 6), ('JS01', 7), 
('JS01', 8), ('JS01', 9), ('JS01', 10), ('JS01', 11), ('JS01', 12);

INSERT INTO sys_role_menu (role_code, menu_id) VALUES
('JS02', 1), ('JS02', 2), ('JS02', 3), ('JS02', 4), ('JS02', 5), ('JS02', 6), ('JS02', 7), 
('JS02', 8), ('JS02', 9), ('JS02', 10), ('JS02', 11), ('JS02', 12), ('JS02', 13), ('JS02', 14);

INSERT INTO sys_role_menu (role_code, menu_id) VALUES
('JS03', 1), ('JS03', 2), ('JS03', 3), ('JS03', 4), ('JS03', 5), ('JS03', 6), ('JS03', 7), 
('JS03', 8), ('JS03', 9), ('JS03', 10), ('JS03', 11), ('JS03', 12), ('JS03', 13), ('JS03', 14),
('JS03', 15), ('JS03', 16), ('JS03', 17), ('JS03', 18), ('JS03', 19), ('JS03', 20), ('JS03', 21),
('JS03', 22), ('JS03', 23), ('JS03', 24), ('JS03', 25), ('JS03', 26), ('JS03', 27), ('JS03', 28);

INSERT INTO sys_role_menu (role_code, menu_id) VALUES
('JS05', 1), ('JS05', 14), ('JS05', 15);

INSERT INTO sys_role_menu (role_code, menu_id) VALUES
('JS06', 1), ('JS06', 16), ('JS06', 17), ('JS06', 18), ('JS06', 19);