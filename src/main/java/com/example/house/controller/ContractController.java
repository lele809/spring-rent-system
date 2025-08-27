package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import com.example.house.entity.ContractsNew;
import com.example.house.entity.ContractsOld;
import com.example.house.service.ContractService;
import com.example.house.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 合同管理控制器
 */
@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {
    
    private final ContractService contractService;
    private final PdfGeneratorService pdfGeneratorService;
    
    // ==================== 六楼合同管理 ====================
    
    /**
     * 获取六楼合同列表
     */
    @GetMapping("/new")
    public ApiResponse<Page<ContractsNew>> getNewContracts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer contractStatus,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String keyword) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        // 记录搜索参数
        System.out.println("六楼合同搜索参数: contractStatus=" + contractStatus + 
                          ", roomNumber=" + roomNumber + ", keyword=" + keyword);
        
        Page<ContractsNew> contracts;
        
        // 如果没有任何筛选条件，直接获取所有数据
        if (contractStatus == null && (roomNumber == null || roomNumber.trim().isEmpty()) 
            && (keyword == null || keyword.trim().isEmpty())) {
            contracts = contractService.getAllNewContracts(pageable);
        } else {
            // 使用筛选查询，支持关键词搜索房间号、租客姓名、合同编号
            contracts = contractService.getNewContractsWithFilters(pageable, contractStatus, roomNumber, keyword);
        }
        
        System.out.println("查询结果数量: " + contracts.getContent().size() + "/" + contracts.getTotalElements());
        return ApiResponse.success(contracts);
    }
    
    /**
     * 根据ID获取六楼合同详情
     */
    @GetMapping("/new/{id}")
    public ApiResponse<ContractsNew> getNewContractById(@PathVariable Integer id) {
        Optional<ContractsNew> contract = contractService.getNewContractById(id);
        if (contract.isPresent()) {
            return ApiResponse.success(contract.get());
        } else {
            return ApiResponse.error("合同不存在");
        }
    }
    
    /**
     * 根据状态获取六楼合同
     */
    @GetMapping("/new/status/{status}")
    public ApiResponse<List<ContractsNew>> getNewContractsByStatus(@PathVariable Integer status) {
        List<ContractsNew> contracts = contractService.getNewContractsByStatus(status);
        return ApiResponse.success(contracts);
    }
    
    /**
     * 搜索六楼合同
     */
    @GetMapping("/new/search")
    public ApiResponse<Page<ContractsNew>> searchNewContracts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ContractsNew> contracts = contractService.searchNewContracts(keyword, pageable);
        return ApiResponse.success(contracts);
    }
    
    /**
     * 创建六楼合同
     */
    @PostMapping("/new")
    public ApiResponse<ContractsNew> createNewContract(@Validated @RequestBody ContractsNew contract) {
        try {
            ContractsNew createdContract = contractService.createNewContract(contract);
            return ApiResponse.success(createdContract);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 更新六楼合同
     */
    @PutMapping("/new/{id}")
    public ApiResponse<ContractsNew> updateNewContract(@PathVariable Integer id, 
                                                       @Validated @RequestBody ContractsNew contract) {
        try {
            ContractsNew updatedContract = contractService.updateNewContract(id, contract);
            return ApiResponse.success(updatedContract);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 删除六楼合同
     */
    @DeleteMapping("/new/{id}")
    public ApiResponse<Void> deleteNewContract(@PathVariable Integer id) {
        try {
            contractService.deleteNewContract(id);
            return ApiResponse.success();
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取六楼合同统计信息
     */
    @GetMapping("/new/stats")
    public ApiResponse<Map<String, Object>> getNewContractStats() {
        Map<String, Object> stats = contractService.getNewContractStats();
        return ApiResponse.success(stats);
    }
    
    /**
     * 获取即将到期的六楼合同
     */
    @GetMapping("/new/expiring")
    public ApiResponse<List<ContractsNew>> getExpiringNewContracts(
            @RequestParam(defaultValue = "30") int days) {
        List<ContractsNew> contracts = contractService.getExpiringNewContracts(days);
        return ApiResponse.success(contracts);
    }
    
    // ==================== 五楼合同管理 ====================
    
    /**
     * 获取五楼合同列表
     */
    @GetMapping("/old")
    public ApiResponse<Page<ContractsOld>> getOldContracts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer contractStatus,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String keyword) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        // 记录搜索参数
        System.out.println("五楼合同搜索参数: contractStatus=" + contractStatus + 
                          ", roomNumber=" + roomNumber + ", keyword=" + keyword);
        
        Page<ContractsOld> contracts;
        
        // 如果没有任何筛选条件，直接获取所有数据
        if (contractStatus == null && (roomNumber == null || roomNumber.trim().isEmpty()) 
            && (keyword == null || keyword.trim().isEmpty())) {
            contracts = contractService.getAllOldContracts(pageable);
        } else {
            // 使用筛选查询，支持关键词搜索房间号、租客姓名、合同编号
            contracts = contractService.getOldContractsWithFilters(pageable, contractStatus, roomNumber, keyword);
        }
        
        System.out.println("查询结果数量: " + contracts.getContent().size() + "/" + contracts.getTotalElements());
        return ApiResponse.success(contracts);
    }
    
    /**
     * 根据ID获取五楼合同详情
     */
    @GetMapping("/old/{id}")
    public ApiResponse<ContractsOld> getOldContractById(@PathVariable Integer id) {
        Optional<ContractsOld> contract = contractService.getOldContractById(id);
        if (contract.isPresent()) {
            return ApiResponse.success(contract.get());
        } else {
            return ApiResponse.error("合同不存在");
        }
    }
    
    /**
     * 根据状态获取五楼合同
     */
    @GetMapping("/old/status/{status}")
    public ApiResponse<List<ContractsOld>> getOldContractsByStatus(@PathVariable Integer status) {
        List<ContractsOld> contracts = contractService.getOldContractsByStatus(status);
        return ApiResponse.success(contracts);
    }
    
    /**
     * 搜索五楼合同
     */
    @GetMapping("/old/search")
    public ApiResponse<Page<ContractsOld>> searchOldContracts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ContractsOld> contracts = contractService.searchOldContracts(keyword, pageable);
        return ApiResponse.success(contracts);
    }
    
    /**
     * 创建五楼合同
     */
    @PostMapping("/old")
    public ApiResponse<ContractsOld> createOldContract(@Validated @RequestBody ContractsOld contract) {
        try {
            ContractsOld createdContract = contractService.createOldContract(contract);
            return ApiResponse.success(createdContract);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 更新五楼合同
     */
    @PutMapping("/old/{id}")
    public ApiResponse<ContractsOld> updateOldContract(@PathVariable Integer id, 
                                                       @Validated @RequestBody ContractsOld contract) {
        try {
            ContractsOld updatedContract = contractService.updateOldContract(id, contract);
            return ApiResponse.success(updatedContract);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 删除五楼合同
     */
    @DeleteMapping("/old/{id}")
    public ApiResponse<Void> deleteOldContract(@PathVariable Integer id) {
        try {
            contractService.deleteOldContract(id);
            return ApiResponse.success();
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取五楼合同统计信息
     */
    @GetMapping("/old/stats")
    public ApiResponse<Map<String, Object>> getOldContractStats() {
        Map<String, Object> stats = contractService.getOldContractStats();
        return ApiResponse.success(stats);
    }
    
    /**
     * 下载六楼合同（文本格式）
     */
    @GetMapping("/new/{id}/download")
    public ResponseEntity<byte[]> downloadNewContract(@PathVariable Integer id) {
        try {
            System.out.println("=== 下载六楼合同 ===");
            System.out.println("合同ID: " + id);
            
            // 获取合同详情
            Optional<ContractsNew> contractOpt = contractService.getNewContractById(id);
            if (!contractOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            ContractsNew contract = contractOpt.get();
            System.out.println("合同信息: " + contract.getContractNumber() + " - " + contract.getTenantName());
            
            // 生成合同内容
            String contractContent = generateContractContent(contract);
            byte[] contentBytes = contractContent.getBytes("UTF-8");
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "合同_" + contract.getContractNumber() + "_" + contract.getTenantName() + ".txt");
            headers.setContentLength(contentBytes.length);
            
            System.out.println("合同下载成功，文件大小: " + contentBytes.length + " 字节");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(contentBytes);
                
        } catch (Exception e) {
            System.err.println("下载六楼合同失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 下载六楼合同（PDF格式）
     */
    @GetMapping("/new/{id}/download-pdf")
    public ResponseEntity<byte[]> downloadNewContractPdf(@PathVariable Integer id) {
        try {
            System.out.println("=== 下载六楼合同PDF ===");
            System.out.println("合同ID: " + id);
            
            // 获取合同详情
            Optional<ContractsNew> contractOpt = contractService.getNewContractById(id);
            if (!contractOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            ContractsNew contract = contractOpt.get();
            System.out.println("合同信息: " + contract.getContractNumber() + " - " + contract.getTenantName());
            
            // 生成PDF内容
            byte[] pdfBytes = pdfGeneratorService.generateNewContractPdf(contract);
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "合同_" + contract.getContractNumber() + "_" + contract.getTenantName() + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            System.out.println("合同PDF下载成功，文件大小: " + pdfBytes.length + " 字节");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
                
        } catch (Exception e) {
            System.err.println("下载六楼合同PDF失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 下载五楼合同（文本格式）
     */
    @GetMapping("/old/{id}/download")
    public ResponseEntity<byte[]> downloadOldContract(@PathVariable Integer id) {
        try {
            System.out.println("=== 下载五楼合同 ===");
            System.out.println("合同ID: " + id);
            
            // 获取合同详情
            Optional<ContractsOld> contractOpt = contractService.getOldContractById(id);
            if (!contractOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            ContractsOld contract = contractOpt.get();
            System.out.println("合同信息: " + contract.getContractNumber() + " - " + contract.getTenantName());
            
            // 生成合同内容
            String contractContent = generateContractContent(contract);
            byte[] contentBytes = contractContent.getBytes("UTF-8");
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                "合同_" + contract.getContractNumber() + "_" + contract.getTenantName() + ".txt");
            headers.setContentLength(contentBytes.length);
            
            System.out.println("合同下载成功，文件大小: " + contentBytes.length + " 字节");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(contentBytes);
                
        } catch (Exception e) {
            System.err.println("下载五楼合同失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 下载五楼合同（PDF格式）
     */
    @GetMapping("/old/{id}/download-pdf")
    public ResponseEntity<byte[]> downloadOldContractPdf(@PathVariable Integer id) {
        try {
            System.out.println("=== 下载五楼合同PDF ===");
            System.out.println("合同ID: " + id);
            
            // 获取合同详情
            Optional<ContractsOld> contractOpt = contractService.getOldContractById(id);
            if (!contractOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            ContractsOld contract = contractOpt.get();
            System.out.println("合同信息: " + contract.getContractNumber() + " - " + contract.getTenantName());
            
            // 生成PDF内容
            byte[] pdfBytes = pdfGeneratorService.generateOldContractPdf(contract);
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "合同_" + contract.getContractNumber() + "_" + contract.getTenantName() + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            System.out.println("合同PDF下载成功，文件大小: " + pdfBytes.length + " 字节");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
                
        } catch (Exception e) {
            System.err.println("下载五楼合同PDF失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 生成六楼合同内容
     */
    private String generateContractContent(ContractsNew contract) {
        StringBuilder content = new StringBuilder();
        content.append("房屋租赁合同\n");
        content.append("=" .repeat(50)).append("\n\n");
        
        content.append("合同编号: ").append(contract.getContractNumber()).append("\n");
        content.append("签订日期: ").append(contract.getCreatedAt() != null ? 
            contract.getCreatedAt().toString().substring(0, 10) : "").append("\n\n");
        
        content.append("甲方（出租方）信息:\n");
        content.append("姓名: ").append(contract.getLandlordName() != null ? contract.getLandlordName() : "房东").append("\n");
        content.append("联系电话: ").append(contract.getLandlordPhone() != null ? contract.getLandlordPhone() : "").append("\n\n");
        
        content.append("乙方（承租方）信息:\n");
        content.append("姓名: ").append(contract.getTenantName()).append("\n");
        content.append("联系电话: ").append(contract.getTenantPhone()).append("\n");
        content.append("身份证号: ").append(contract.getTenantIdCard()).append("\n\n");
        
        content.append("房屋信息:\n");
        content.append("房间号: ").append(contract.getRoomNumber()).append("\n");
        content.append("月租金: ￥").append(contract.getMonthlyRent()).append("元\n");
        content.append("押金: ￥").append(contract.getDeposit()).append("元\n");
        content.append("付款方式: ").append(contract.getPaymentMethod() != null ? contract.getPaymentMethod() : "月付").append("\n\n");
        
        content.append("合同期限:\n");
        content.append("开始日期: ").append(contract.getContractStartDate()).append("\n");
        content.append("结束日期: ").append(contract.getContractEndDate()).append("\n");
        content.append("租期: ").append(contract.getContractDuration()).append("个月\n\n");
        
        content.append("水电费用:\n");
        content.append("是否包含水电费: ").append(contract.getUtilitiesIncluded() != null && contract.getUtilitiesIncluded() == 1 ? "包含" : "不包含").append("\n");
        content.append("水费单价: ￥").append(contract.getWaterRate() != null ? contract.getWaterRate() : 3.5).append("元/吨\n");
        content.append("电费单价: ￥").append(contract.getElectricityRate() != null ? contract.getElectricityRate() : 0.6).append("元/度\n\n");
        
        if (contract.getContractTerms() != null && !contract.getContractTerms().trim().isEmpty()) {
            content.append("合同条款:\n");
            content.append(contract.getContractTerms()).append("\n\n");
        }
        
        if (contract.getSpecialAgreement() != null && !contract.getSpecialAgreement().trim().isEmpty()) {
            content.append("特殊约定:\n");
            content.append(contract.getSpecialAgreement()).append("\n\n");
        }
        
        if (contract.getRemarks() != null && !contract.getRemarks().trim().isEmpty()) {
            content.append("备注:\n");
            content.append(contract.getRemarks()).append("\n\n");
        }
        
        content.append("=" .repeat(50)).append("\n");
        content.append("甲方签字: ________________    日期: ________________\n\n");
        content.append("乙方签字: ________________    日期: ________________\n");
        
        return content.toString();
    }
    
    /**
     * 生成五楼合同内容
     */
    private String generateContractContent(ContractsOld contract) {
        StringBuilder content = new StringBuilder();
        content.append("房屋租赁合同\n");
        content.append("=" .repeat(50)).append("\n\n");
        
        content.append("合同编号: ").append(contract.getContractNumber()).append("\n");
        content.append("签订日期: ").append(contract.getCreatedAt() != null ? 
            contract.getCreatedAt().toString().substring(0, 10) : "").append("\n\n");
        
        content.append("甲方（出租方）信息:\n");
        content.append("姓名: ").append(contract.getLandlordName() != null ? contract.getLandlordName() : "房东").append("\n");
        content.append("联系电话: ").append(contract.getLandlordPhone() != null ? contract.getLandlordPhone() : "").append("\n\n");
        
        content.append("乙方（承租方）信息:\n");
        content.append("姓名: ").append(contract.getTenantName()).append("\n");
        content.append("联系电话: ").append(contract.getTenantPhone()).append("\n");
        content.append("身份证号: ").append(contract.getTenantIdCard()).append("\n\n");
        
        content.append("房屋信息:\n");
        content.append("房间号: ").append(contract.getRoomNumber()).append("\n");
        content.append("月租金: ￥").append(contract.getMonthlyRent()).append("元\n");
        content.append("押金: ￥").append(contract.getDeposit()).append("元\n");
        content.append("付款方式: ").append(contract.getPaymentMethod() != null ? contract.getPaymentMethod() : "月付").append("\n\n");
        
        content.append("合同期限:\n");
        content.append("开始日期: ").append(contract.getContractStartDate()).append("\n");
        content.append("结束日期: ").append(contract.getContractEndDate()).append("\n");
        content.append("租期: ").append(contract.getContractDuration()).append("个月\n\n");
        
        content.append("水电费用:\n");
        content.append("是否包含水电费: ").append(contract.getUtilitiesIncluded() != null && contract.getUtilitiesIncluded() == 1 ? "包含" : "不包含").append("\n");
        content.append("水费单价: ￥").append(contract.getWaterRate() != null ? contract.getWaterRate() : 3.5).append("元/吨\n");
        content.append("电费单价: ￥").append(contract.getElectricityRate() != null ? contract.getElectricityRate() : 0.6).append("元/度\n\n");
        
        if (contract.getContractTerms() != null && !contract.getContractTerms().trim().isEmpty()) {
            content.append("合同条款:\n");
            content.append(contract.getContractTerms()).append("\n\n");
        }
        
        if (contract.getSpecialAgreement() != null && !contract.getSpecialAgreement().trim().isEmpty()) {
            content.append("特殊约定:\n");
            content.append(contract.getSpecialAgreement()).append("\n\n");
        }
        
        if (contract.getRemarks() != null && !contract.getRemarks().trim().isEmpty()) {
            content.append("备注:\n");
            content.append(contract.getRemarks()).append("\n\n");
        }
        
        content.append("=" .repeat(50)).append("\n");
        content.append("甲方签字: ________________    日期: ________________\n\n");
        content.append("乙方签字: ________________    日期: ________________\n");
        
        return content.toString();
    }
} 