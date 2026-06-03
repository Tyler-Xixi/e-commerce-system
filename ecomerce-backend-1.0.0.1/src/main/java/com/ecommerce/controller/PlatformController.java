package com.ecommerce.controller;

import com.ecommerce.dto.PageResult;
import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import com.ecommerce.util.PageRequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlatformController {

    private final SysUserRepository userRepo;
    private final SupplierRepository supplierRepo;
    private final ProductRepository productRepo;
    private final SalesOrderRepository orderRepo;
    private final InventoryRepository inventoryRepo;
    private final LogisticsRepository logisticsRepo;
    private final PaymentSettlementRepository paymentRepo;
    private final AfterSaleTicketRepository afterSaleRepo;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('system:user:manage')")
    public ResponseEntity<?> listUsers(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size) {
        Page<SysUser> users = userRepo.findAll(PageRequestUtil.of(page, size));
        List<Map<String, Object>> result = new ArrayList<>();
        for (SysUser u : users.getContent()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", u.getId()); m.put("code", "U-100" + u.getId()); m.put("username", u.getUsername());
            m.put("name", u.getRealName()); m.put("role", getRoleLabel(u.getRoleCode()));
            m.put("roleCode", u.getRoleCode()); m.put("dept", "运营部"); m.put("status", u.getStatus() == 1 ? "启用" : "禁用");
            result.add(m);
        }
        return ResponseEntity.ok(PageResult.of(result, users.getTotalElements(), users.getNumber() + 1, users.getSize()));
    }

    @GetMapping("/suppliers")
    @PreAuthorize("hasAuthority('supplier:list')")
    public ResponseEntity<?> listSuppliers(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(PageResult.of(supplierRepo.findAll(PageRequestUtil.of(page, size))));
    }

    @GetMapping("/products")
    @PreAuthorize("hasAuthority('product:list')")
    public ResponseEntity<?> listProducts(@RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(PageResult.of(productRepo.findAll(PageRequestUtil.of(page, size))));
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('purchase:order:list')")
    public ResponseEntity<?> listOrders(@RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(PageResult.of(orderRepo.findAll(PageRequestUtil.of(page, size))));
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasAuthority('warehouse:inventory')")
    public ResponseEntity<?> listInventory(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(PageResult.of(inventoryRepo.findAll(PageRequestUtil.of(page, size))));
    }

    @GetMapping("/logistics")
    @PreAuthorize("hasAuthority('warehouse:logistics')")
    public ResponseEntity<?> listLogistics(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(PageResult.of(logisticsRepo.findAll(PageRequestUtil.of(page, size))));
    }

    @GetMapping("/payments")
    @PreAuthorize("hasAuthority('purchase:payment:list')")
    public ResponseEntity<?> listPayments(@RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(PageResult.of(paymentRepo.findAll(PageRequestUtil.of(page, size))));
    }

    @GetMapping("/aftersales")
    @PreAuthorize("hasAuthority('aftersales:list')")
    public ResponseEntity<?> listAfterSales(@RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(PageResult.of(afterSaleRepo.findAll(PageRequestUtil.of(page, size))));
    }

    @PostMapping("/suppliers")
    @PreAuthorize("hasAuthority('supplier:manage')")
    public ResponseEntity<?> createSupplier(@RequestBody Supplier s) { return ResponseEntity.ok(supplierRepo.save(s)); }

    @PostMapping("/products")
    @PreAuthorize("hasAuthority('product:manage')")
    public ResponseEntity<?> createProduct(@RequestBody Product p) { return ResponseEntity.ok(productRepo.save(p)); }

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('purchase:order:manage')")
    public ResponseEntity<?> createOrder(@RequestBody SalesOrder o) { return ResponseEntity.ok(orderRepo.save(o)); }

    @PostMapping("/inventory")
    @PreAuthorize("hasAuthority('warehouse:inventory:manage')")
    public ResponseEntity<?> createInventory(@RequestBody Inventory i) { return ResponseEntity.ok(inventoryRepo.save(i)); }

    @PostMapping("/logistics")
    @PreAuthorize("hasAuthority('warehouse:logistics:manage')")
    public ResponseEntity<?> createLogistics(@RequestBody Logistics l) { return ResponseEntity.ok(logisticsRepo.save(l)); }

    @PostMapping("/payments")
    @PreAuthorize("hasAuthority('purchase:payment:manage')")
    public ResponseEntity<?> createPayment(@RequestBody PaymentSettlement p) { return ResponseEntity.ok(paymentRepo.save(p)); }

    @PostMapping("/aftersales")
    @PreAuthorize("hasAuthority('aftersales:manage')")
    public ResponseEntity<?> createAfterSale(@RequestBody AfterSaleTicket t) { return ResponseEntity.ok(afterSaleRepo.save(t)); }

    @PutMapping("/suppliers/{id}")
    @PreAuthorize("hasAuthority('supplier:manage')")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id, @RequestBody Supplier s) {
        s.setId(id); return ResponseEntity.ok(supplierRepo.save(s));
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasAuthority('product:manage')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product p) {
        p.setId(id); return ResponseEntity.ok(productRepo.save(p));
    }

    @PutMapping("/orders/{id}")
    @PreAuthorize("hasAuthority('purchase:order:manage')")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody SalesOrder o) {
        o.setId(id); return ResponseEntity.ok(orderRepo.save(o));
    }

    @PutMapping("/inventory/{id}")
    @PreAuthorize("hasAuthority('warehouse:inventory:manage')")
    public ResponseEntity<?> updateInventory(@PathVariable Long id, @RequestBody Inventory i) {
        i.setId(id); return ResponseEntity.ok(inventoryRepo.save(i));
    }

    @PutMapping("/logistics/{id}")
    @PreAuthorize("hasAuthority('warehouse:logistics:manage')")
    public ResponseEntity<?> updateLogistics(@PathVariable Long id, @RequestBody Logistics l) {
        l.setId(id); return ResponseEntity.ok(logisticsRepo.save(l));
    }

    @PutMapping("/payments/{id}")
    @PreAuthorize("hasAuthority('purchase:payment:manage')")
    public ResponseEntity<?> updatePayment(@PathVariable Long id, @RequestBody PaymentSettlement p) {
        p.setId(id); return ResponseEntity.ok(paymentRepo.save(p));
    }

    @PutMapping("/aftersales/{id}")
    @PreAuthorize("hasAuthority('aftersales:manage')")
    public ResponseEntity<?> updateAfterSale(@PathVariable Long id, @RequestBody AfterSaleTicket t) {
        t.setId(id); return ResponseEntity.ok(afterSaleRepo.save(t));
    }

    @DeleteMapping("/suppliers/{id}")
    @PreAuthorize("hasAuthority('supplier:manage')")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        supplierRepo.deleteById(id); return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAuthority('product:manage')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id); return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    @DeleteMapping("/orders/{id}")
    @PreAuthorize("hasAuthority('purchase:order:manage')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderRepo.deleteById(id); return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    @DeleteMapping("/inventory/{id}")
    @PreAuthorize("hasAuthority('warehouse:inventory:manage')")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        inventoryRepo.deleteById(id); return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    @GetMapping("/inventory/alerts")
    @PreAuthorize("hasAuthority('warehouse:inventory')")
    public ResponseEntity<?> inventoryAlerts() {
        return ResponseEntity.ok(inventoryRepo.findAll().stream()
            .filter(i -> i.getAvailable() != null && i.getAlert() != null)
            .filter(i -> i.getAvailable() < i.getAlert())
            .collect(Collectors.toList()));
    }

    @PostMapping("/{resource}/{id}/approve")
    @PreAuthorize("hasAuthority('dashboard:approval')")
    public ResponseEntity<?> approve(@PathVariable String resource, @PathVariable Long id, @RequestBody(required = false) Map<String, Object> body) {
        return ResponseEntity.ok(Map.of("message", "审批成功"));
    }

    private String getRoleLabel(String code) {
        return switch (code) {
            case "JS01" -> "品类工程师"; case "JS02" -> "品类组长"; case "JS03" -> "采购处长";
            case "JS04" -> "平台管理员"; case "JS05" -> "供应商操作员"; case "JS06" -> "仓储调度员";
            default -> "未知";
        };
    }
}
