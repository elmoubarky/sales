package org.sales.taxes.controller;

import lombok.RequiredArgsConstructor;
import org.sales.taxes.service.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/sales")
public class SalesController {

    private final SalesService salesService;

    @PostMapping("/receipt")
    public ResponseEntity<String> generateReceipt(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(salesService.generateReceipt(file));
    }
}
