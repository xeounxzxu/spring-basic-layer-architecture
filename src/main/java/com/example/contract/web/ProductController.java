package com.example.contract.web;

import com.example.contract.service.ProductService;
import com.example.contract.web.dto.EstimatedPremium;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("products/{id}/premium")
    public ResponseEntity<EstimatedPremium> selectEstimatedPremium(@PathVariable Long id, @RequestParam("warrantIds") List<Long> warrantIds) {
        Optional<EstimatedPremium> data = productService.getEstimatedPremium(id, warrantIds);
        return data.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
}