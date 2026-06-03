package com.ecommerce.service;

import com.ecommerce.dto.PageResult;
import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import com.ecommerce.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SourcingService {
    private static final Logger logger = LoggerFactory.getLogger(SourcingService.class);
    private final RfqDemandRepository demandRepository;
    private final RfqQuotationRepository quotationRepository;
    private final RfqSupplierQuoteRepository quoteRepository;
    private final RfqBusinessAwardRepository awardRepository;
    private final RfqAwardNoticeRepository noticeRepository;
    private final RfqSupplierRelRepository relRepository;
    private final WfApprovalRecordRepository approvalRecordRepository;
    private final WfTodoTaskRepository todoTaskRepository;
    private final SysNotificationRepository notificationRepository;
    private final SupplierRepository supplierRepository;
    private final SysUserRepository userRepository;
    private final JwtUtil jwtUtil;

    private Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String header = request.getHeader(jwtUtil.getHeader());
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    return jwtUtil.getUserIdFromToken(token);
                } catch (Exception e) {
                    logger.warn("Current user token parse failed: {}", e.getMessage());
                }
            }
        }
        return null;
    }

    public PageResult<RfqDemand> listDemands(Pageable pageable, Long userId, Boolean mineOnly) {
        Page<RfqDemand> demands = Boolean.TRUE.equals(mineOnly) && userId != null
            ? demandRepository.findByCreatedByOrAssigneeId(userId, userId, pageable)
            : demandRepository.findAll(pageable);
        demands.getContent().forEach(demand -> {
            if (demand.getAssigneeId() != null) {
                userRepository.findById(demand.getAssigneeId()).ifPresent(user -> demand.setAssigneeName(user.getRealName()));
            }
            if (demand.getCreatedBy() != null) {
                userRepository.findById(demand.getCreatedBy()).ifPresent(user -> demand.setCreatorName(user.getRealName()));
            }
        });
        return PageResult.of(demands);
    }

    public RfqDemand createDemand(RfqDemand demand) {
        if (demand.getDemandNo() == null || demand.getDemandNo().isBlank()) {
            demand.setDemandNo("DEM-" + System.currentTimeMillis());
        }
        demand.setCreatedAt(LocalDateTime.now());
        return demandRepository.save(demand);
    }

    public RfqDemand updateDemand(Long id, RfqDemand demand) {
        demand.setId(id);
        return demandRepository.save(demand);
    }

    public PageResult<RfqQuotation> listQuotations(Pageable pageable, Boolean publishedOnly) {
        Page<RfqQuotation> quotations = Boolean.TRUE.equals(publishedOnly)
            ? quotationRepository.findByStatusNot("DRAFT", pageable)
            : quotationRepository.findAll(pageable);
        return PageResult.of(quotations);
    }

    public RfqQuotation createQuotation(RfqQuotation quotation) {
        if (quotation.getRfqNo() == null || quotation.getRfqNo().isBlank()) {
            quotation.setRfqNo("RFQ-" + System.currentTimeMillis());
        }
        if (quotation.getDeliveryDate() == null) {
            quotation.setDeliveryDate(quotation.getQuoteDeadline() != null
                ? quotation.getQuoteDeadline().plusDays(7)
                : LocalDateTime.now().plusDays(14));
        }
        quotation.setCreatedAt(LocalDateTime.now());
        if (quotation.getCreatedBy() == null) {
            quotation.setCreatedBy(getCurrentUserId());
        }
        return quotationRepository.save(quotation);
    }

    public RfqQuotation updateQuotation(Long id, RfqQuotation quotation) {
        quotation.setId(id);
        quotation.setUpdatedAt(LocalDateTime.now());
        return quotationRepository.save(quotation);
    }

    public PageResult<RfqSupplierQuote> listQuotes(Pageable pageable, Long supplierId) {
        Page<RfqSupplierQuote> quotes = supplierId == null
            ? quoteRepository.findAll(pageable)
            : quoteRepository.findBySupplierId(supplierId, pageable);
        quotes.getContent().forEach(quote -> {
            supplierRepository.findById(quote.getSupplierId()).ifPresent(supplier -> quote.setSupplierName(supplier.getName()));
            quotationRepository.findById(quote.getRfqId()).ifPresent(rfq -> quote.setRfqNo(rfq.getRfqNo()));
        });
        return PageResult.of(quotes);
    }

    public RfqSupplierQuote createQuote(RfqSupplierQuote quote) {
        quote.setSubmittedAt(LocalDateTime.now());
        return quoteRepository.save(quote);
    }

    public PageResult<RfqBusinessAward> listAwards(Pageable pageable, Long supplierId) {
        Page<RfqBusinessAward> awards = supplierId == null
            ? awardRepository.findAll(pageable)
            : awardRepository.findBySupplierId(supplierId, pageable);
        awards.getContent().forEach(award -> {
            supplierRepository.findById(award.getSupplierId()).ifPresent(supplier -> award.setSupplierName(supplier.getName()));
            quotationRepository.findById(award.getRfqId()).ifPresent(rfq -> award.setRfqNo(rfq.getRfqNo()));
        });
        return PageResult.of(awards);
    }

    public RfqBusinessAward createAward(RfqBusinessAward award) {
        if (award.getAwardNo() == null || award.getAwardNo().isBlank()) {
            award.setAwardNo("BA-" + System.currentTimeMillis());
        }
        if (award.getStatus() == null || award.getStatus().isBlank()) {
            award.setStatus("APPROVING");
        }
        if (award.getCreatedBy() == null) {
            award.setCreatedBy(getCurrentUserId());
        }
        award.setCreatedAt(LocalDateTime.now());
        return awardRepository.save(award);
    }

    public RfqBusinessAward updateAward(Long id, RfqBusinessAward award) {
        award.setId(id);
        return awardRepository.save(award);
    }

    public PageResult<RfqAwardNotice> listAwardNotices(Pageable pageable) {
        return PageResult.of(noticeRepository.findAll(pageable));
    }

    public RfqAwardNotice createAwardNotice(RfqAwardNotice notice) {
        if (notice.getNoticeNo() == null || notice.getNoticeNo().isBlank()) {
            notice.setNoticeNo("NT-" + System.currentTimeMillis());
        }
        if (notice.getStatus() == null || notice.getStatus().isBlank()) {
            notice.setStatus("PENDING");
        }
        if (notice.getGeneratedAt() == null) {
            notice.setGeneratedAt(LocalDateTime.now());
        }
        return noticeRepository.save(notice);
    }

    public RfqAwardNotice updateAwardNotice(Long id, RfqAwardNotice notice) {
        notice.setId(id);
        if ("CONFIRMED".equals(notice.getStatus()) && notice.getConfirmedAt() == null) {
            notice.setConfirmedAt(LocalDateTime.now());
        }
        return noticeRepository.save(notice);
    }

    public List<WfApprovalRecord> listApprovalRecords(String bizType, Long bizId) {
        List<WfApprovalRecord> records = approvalRecordRepository.findByBizTypeAndBizIdOrderByCreatedAtAsc(bizType, bizId);
        records.forEach(record -> {
            if (record.getApproverId() != null) {
                userRepository.findById(record.getApproverId()).ifPresent(user -> record.setApproverName(user.getRealName()));
            }
        });
        return records;
    }

    @Transactional
    public WfApprovalRecord approve(String bizType, Long bizId, Long approverId, String action, String opinion) {
        WfApprovalRecord record = new WfApprovalRecord();
        record.setBizType(bizType);
        record.setBizId(bizId);
        record.setApproverId(approverId);
        record.setAction(action);
        record.setOpinion(opinion);
        record.setCreatedAt(LocalDateTime.now());
        return approvalRecordRepository.save(record);
    }

    public long countPendingTasks(Long userId) {
        return todoTaskRepository.countByUserIdAndIsDone(userId, 0);
    }

    public PageResult<WfTodoTask> listPendingTasks(Long userId, Pageable pageable) {
        Page<WfTodoTask> tasks = todoTaskRepository.findByUserIdAndIsDone(userId, 0, pageable);
        tasks.getContent().forEach(task -> userRepository.findById(task.getUserId()).ifPresent(user -> task.setUserName(user.getRealName())));
        return PageResult.of(tasks);
    }

    public List<WfTodoTask> listDoneTasks(Long userId) {
        List<WfTodoTask> tasks = todoTaskRepository.findByUserIdAndIsDoneOrderByCreatedAtDesc(userId, 1);
        tasks.forEach(task -> userRepository.findById(task.getUserId()).ifPresent(user -> task.setUserName(user.getRealName())));
        return tasks;
    }

    public PageResult<SysNotification> listNotifications(Long userId, Pageable pageable) {
        return PageResult.of(notificationRepository.findByRecipientId(userId, pageable));
    }
}
