package com.example.contract.service;

import com.example.contract.doamin.Contract;
import com.example.contract.doamin.Product;
import com.example.contract.repository.ContractRepository;
import com.example.contract.repository.ProductRepository;
import com.example.contract.web.dto.ContractDetail;
import com.example.contract.web.dto.ContractResponse;
import com.example.contract.web.dto.ContractSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;

    private final ProductRepository productRepository;

    @Transactional
    public ContractResponse created(ContractSaveRequest dto) {

        // todo : exception
        Product product = productRepository.findByIdAndWarrants_IdIn(dto.getProductId(), dto.getWarrantIds()).orElseThrow(() -> new RuntimeException("not found"));

        BigDecimal premium = product.calculatePremium();

        Contract entity = dto.toEntity(product, product.getWarrants(), premium);

        return new ContractResponse(contractRepository.save(entity));
    }

    public Optional<ContractDetail> getOne(Long id) {
        return contractRepository.findById(id, ContractDetail.class);
    }
}
