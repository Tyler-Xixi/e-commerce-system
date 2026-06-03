package com.ecommerce.controller;

import com.ecommerce.entity.*;
import com.ecommerce.service.SourcingService;
import com.ecommerce.util.PageRequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/sourcing")
@RequiredArgsConstructor
public class SourcingController {

    private final SourcingService sourcingService;

    @GetMapping("/demands")
    @PreAuthorize("hasAuthority('sourcing:demand:list')")
    public ResponseEntity<?> listDemands(@RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size,
                                         @RequestParam(required = false) Long userId,
                                         @RequestParam(required = false) Boolean mineOnly) {
        return ResponseEntity.ok(sourcingService.listDemands(PageRequestUtil.of(page, size), userId, mineOnly));
    }

    @PostMapping("/demands")
    @PreAuthorize("hasAuthority('sourcing:demand:create')")
    public ResponseEntity<?> createDemand(@RequestBody RfqDemand demand) {
        return ResponseEntity.ok(sourcingService.createDemand(demand));
    }

    @PutMapping("/demands/{id}")
    @PreAuthorize("hasAuthority('sourcing:demand:review')")
    public ResponseEntity<?> updateDemand(@PathVariable Long id, @RequestBody RfqDemand demand) {
        return ResponseEntity.ok(sourcingService.updateDemand(id, demand));
    }

    @GetMapping("/quotations")
    @PreAuthorize("hasAuthority('sourcing:rfq:list')")
    public ResponseEntity<?> listQuotations(@RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size,
                                            @RequestParam(required = false) Boolean publishedOnly) {
        return ResponseEntity.ok(sourcingService.listQuotations(PageRequestUtil.of(page, size), publishedOnly));
    }

    @PostMapping("/quotations")
    @PreAuthorize("hasAuthority('sourcing:rfq:manage')")
    public ResponseEntity<?> createQuotation(@RequestBody RfqQuotation quotation) {
        return ResponseEntity.ok(sourcingService.createQuotation(quotation));
    }

    @PutMapping("/quotations/{id}")
    @PreAuthorize("hasAuthority('sourcing:rfq:manage')")
    public ResponseEntity<?> updateQuotation(@PathVariable Long id, @RequestBody RfqQuotation quotation) {
        return ResponseEntity.ok(sourcingService.updateQuotation(id, quotation));
    }

    @GetMapping("/quotes")
    @PreAuthorize("hasAuthority('sourcing:quote:summary')")
    public ResponseEntity<?> listQuotes(@RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size,
                                        @RequestParam(required = false) Long supplierId) {
        return ResponseEntity.ok(sourcingService.listQuotes(PageRequestUtil.of(page, size, Sort.by(Sort.Direction.ASC, "unitPrice")), supplierId));
    }

    @PostMapping("/quotes")
    @PreAuthorize("hasAuthority('sourcing:quote:create')")
    public ResponseEntity<?> createQuote(@RequestBody RfqSupplierQuote quote) {
        return ResponseEntity.ok(sourcingService.createQuote(quote));
    }

    @GetMapping("/awards")
    @PreAuthorize("hasAuthority('sourcing:award:list')")
    public ResponseEntity<?> listAwards(@RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size,
                                        @RequestParam(required = false) Long supplierId) {
        return ResponseEntity.ok(sourcingService.listAwards(PageRequestUtil.of(page, size), supplierId));
    }

    @PostMapping("/awards")
    @PreAuthorize("hasAuthority('sourcing:award:manage')")
    public ResponseEntity<?> createAward(@RequestBody RfqBusinessAward award) {
        return ResponseEntity.ok(sourcingService.createAward(award));
    }

    @PutMapping("/awards/{id}")
    @PreAuthorize("hasAuthority('sourcing:award:approve')")
    public ResponseEntity<?> updateAward(@PathVariable Long id, @RequestBody RfqBusinessAward award) {
        return ResponseEntity.ok(sourcingService.updateAward(id, award));
    }

    @GetMapping("/award-notices")
    @PreAuthorize("hasAuthority('sourcing:award:list')")
    public ResponseEntity<?> listAwardNotices(@RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(sourcingService.listAwardNotices(PageRequestUtil.of(page, size)));
    }

    @PostMapping("/award-notices")
    @PreAuthorize("hasAuthority('sourcing:award:manage')")
    public ResponseEntity<?> createAwardNotice(@RequestBody RfqAwardNotice notice) {
        return ResponseEntity.ok(sourcingService.createAwardNotice(notice));
    }

    @PutMapping("/award-notices/{id}")
    @PreAuthorize("hasAuthority('sourcing:award:manage')")
    public ResponseEntity<?> updateAwardNotice(@PathVariable Long id, @RequestBody RfqAwardNotice notice) {
        return ResponseEntity.ok(sourcingService.updateAwardNotice(id, notice));
    }

    @GetMapping("/approvals")
    @PreAuthorize("hasAuthority('dashboard:approval')")
    public ResponseEntity<?> listApprovals(@RequestParam String bizType, @RequestParam Long bizId) {
        return ResponseEntity.ok(sourcingService.listApprovalRecords(bizType, bizId));
    }

    @PostMapping("/approvals")
    @PreAuthorize("hasAuthority('dashboard:approval')")
    public ResponseEntity<?> approve(@RequestBody WfApprovalRecord record) {
        return ResponseEntity.ok(sourcingService.approve(
                record.getBizType(), record.getBizId(), record.getApproverId(), record.getAction(), record.getOpinion()));
    }

    @GetMapping("/tasks/pending/{userId}")
    @PreAuthorize("hasAuthority('dashboard:todo')")
    public ResponseEntity<?> pendingTasks(@PathVariable Long userId,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(sourcingService.listPendingTasks(userId, PageRequestUtil.of(page, size)));
    }

    @GetMapping("/tasks/count/{userId}")
    @PreAuthorize("hasAuthority('dashboard:todo')")
    public ResponseEntity<?> taskCount(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of("count", sourcingService.countPendingTasks(userId)));
    }

    @GetMapping("/notifications/{userId}")
    @PreAuthorize("hasAuthority('dashboard:todo')")
    public ResponseEntity<?> notifications(@PathVariable Long userId,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(sourcingService.listNotifications(userId, PageRequestUtil.of(page, size)));
    }
}
