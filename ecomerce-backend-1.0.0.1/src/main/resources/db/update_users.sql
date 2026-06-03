UPDATE sys_user SET real_name = 'Zhang San', role_code = 'JS01' WHERE username = 'admin';

INSERT IGNORE INTO sys_user (username, password, real_name, role_code, status) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHsPEd0jZRGzFzVqYiW', 'Zhang San', 'JS01', 1);

INSERT IGNORE INTO sys_user (username, password, real_name, role_code, status) 
VALUES ('lisi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHsPEd0jZRGzFzVqYiW', 'Li Si', 'JS02', 1);

INSERT IGNORE INTO sys_user (username, password, real_name, role_code, status) 
VALUES ('wangwu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHsPEd0jZRGzFzVqYiW', 'Wang Wu', 'JS03', 1);

INSERT IGNORE INTO sys_user (username, password, real_name, role_code, status) 
VALUES ('zhaoliu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHsPEd0jZRGzFzVqYiW', 'Zhao Liu', 'JS05', 1);

INSERT IGNORE INTO sys_user (username, password, real_name, role_code, status) 
VALUES ('sunqi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHsPEd0jZRGzFzVqYiW', 'Sun Qi', 'JS06', 1);