package com.ecommerce.config;

import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements ApplicationRunner {

    private final SysUserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final RfqDemandRepository demandRepository;
    private final PasswordEncoder passwordEncoder;

    private void ensureUser(String username, String realName, String roleCode) {
        Optional<SysUser> existing = userRepository.findByUsername(username);
        SysUser user;
        if (existing.isEmpty()) {
            user = new SysUser();
            user.setUsername(username);
        } else {
            user = existing.get();
        }
        user.setRealName(realName);
        user.setRoleCode(roleCode);
        user.setStatus(1);
        if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode("123456"));
        }
        userRepository.save(user);
    }

    @Override
    public void run(ApplicationArguments args) {
        ensureUser("engineer", "李明", "JS01");
        ensureUser("leader", "赵强", "JS02");
        ensureUser("director", "张处长", "JS03");
        ensureUser("supplier", "小米供应-刘工", "JS05");
        ensureUser("warehouse", "郑丽", "JS06");
        ensureUser("admin", "系统管理员", "JS04");

        if (supplierRepository.count() == 0) {
            Supplier s1 = new Supplier();
            s1.setCode("SUP-001"); s1.setName("华东电子有限公司"); s1.setContact("周宁"); s1.setGrade("A"); s1.setScore(96); s1.setStatus("正常");
            supplierRepository.save(s1);

            Supplier s2 = new Supplier();
            s2.setCode("SUP-002"); s2.setName("云仓智能科技"); s2.setContact("李明"); s2.setGrade("A"); s2.setScore(94); s2.setStatus("正常");
            supplierRepository.save(s2);

            Supplier s3 = new Supplier();
            s3.setCode("SUP-003"); s3.setName("华南包装材料"); s3.setContact("王芳"); s3.setGrade("B"); s3.setScore(82); s3.setStatus("正常");
            supplierRepository.save(s3);
        }

        if (productRepository.count() == 0) {
            Product p1 = new Product();
            p1.setSku("P-SCAN-001"); p1.setName("智能扫码终端"); p1.setCategory("仓储设备"); p1.setUnit("台"); p1.setPrice(BigDecimal.valueOf(1680)); p1.setStatus("上架");
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setSku("P-PACK-001"); p2.setName("环保包装耗材"); p2.setCategory("包装材料"); p2.setUnit("箱"); p2.setPrice(BigDecimal.valueOf(32)); p2.setStatus("上架");
            productRepository.save(p2);

            Product p3 = new Product();
            p3.setSku("P-SORT-001"); p3.setName("分拣线传感器"); p3.setCategory("仓储设备"); p3.setUnit("个"); p3.setPrice(BigDecimal.valueOf(580)); p3.setStatus("上架");
            productRepository.save(p3);
        }

        if (inventoryRepository.count() == 0) {
            Inventory i1 = new Inventory();
            i1.setSku("P-SCAN-001"); i1.setWarehouse("华东一号仓"); i1.setAvailable(80); i1.setLocked(20); i1.setAlert(100); i1.setStatus("预警");
            inventoryRepository.save(i1);

            Inventory i2 = new Inventory();
            i2.setSku("P-PACK-001"); i2.setWarehouse("华南二号仓"); i2.setAvailable(21000); i2.setLocked(6000); i2.setAlert(30000); i2.setStatus("预警");
            inventoryRepository.save(i2);

            Inventory i3 = new Inventory();
            i3.setSku("P-SORT-001"); i3.setWarehouse("华东一号仓"); i3.setAvailable(222); i3.setLocked(12); i3.setAlert(60); i3.setStatus("正常");
            inventoryRepository.save(i3);
        }

        if (demandRepository.count() == 0) {
            RfqDemand d1 = new RfqDemand();
            d1.setDemandNo("YS-202604-001"); d1.setCategoryCode("智能扫码终端"); d1.setQuantity(BigDecimal.valueOf(300));
            d1.setDeadline(LocalDateTime.of(2026, 4, 30, 0, 0)); d1.setStatus("待接收"); d1.setCreatedBy(2L); d1.setCreatedAt(LocalDateTime.now());
            demandRepository.save(d1);
        }
    }
}
