package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("status", "UP");
        info.put("service", "e-commerce-system");
        info.put("version", "1.0.0");
        return ResponseEntity.ok(info);
    }

    @GetMapping("/dictionaries")
    public ResponseEntity<?> dictionaries() {
        Map<String, Object> dicts = new LinkedHashMap<>();
        List<Map<String, String>> roleOptions = new ArrayList<>();
        roleOptions.add(Map.of("label", "平台管理员", "value", "JS04"));
        roleOptions.add(Map.of("label", "品类工程师", "value", "JS01"));
        roleOptions.add(Map.of("label", "品类组长", "value", "JS02"));
        roleOptions.add(Map.of("label", "采购处长", "value", "JS03"));
        roleOptions.add(Map.of("label", "供应商操作员", "value", "JS05"));
        roleOptions.add(Map.of("label", "仓储调度员", "value", "JS06"));
        dicts.put("roleOptions", roleOptions);
        return ResponseEntity.ok(dicts);
    }

    @GetMapping("/oss/policy")
    public ResponseEntity<?> ossPolicy() {
        Map<String, String> policy = new LinkedHashMap<>();
        policy.put("accessKeyId", "test-access-key");
        policy.put("policy", "test-policy");
        policy.put("signature", "test-signature");
        policy.put("bucket", "test-bucket");
        policy.put("endpoint", "oss-cn-hangzhou.aliyuncs.com");
        return ResponseEntity.ok(policy);
    }
}