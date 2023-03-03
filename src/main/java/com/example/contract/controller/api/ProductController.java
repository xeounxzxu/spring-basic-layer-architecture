package com.example.contract.controller.api;

import com.example.contract.service.ProductService;
import com.example.contract.dto.mapper.EstimatedPremium;
import com.example.contract.dto.response.ProductResponse;
import com.example.contract.controller.request.ProductSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 상품 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 생성 API
     *
     * @param dto 상품 생성에 필요한 데이터
     * @return 상품
     */
    @PostMapping("product")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse write(@RequestBody @Valid ProductSaveRequest dto) {
        return new ProductResponse(productService.created(dto));
    }

    /**
     * 총 보험료 API
     *
     * @param id         상품의 아이디
     * @param warrantIds 담보 데이터 (emptyable)
     * @return 총 보험료 데이터
     */
    @GetMapping("products/{id}/premium")
    public ResponseEntity<EstimatedPremium> selectEstimatedPremium(@PathVariable Long id, @RequestParam("warrantIds") List<Long> warrantIds) {
        Optional<EstimatedPremium> data = productService.getEstimatedPremium(id, warrantIds);
        return data.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
}