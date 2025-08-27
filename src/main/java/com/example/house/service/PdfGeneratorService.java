package com.example.house.service;

import com.example.house.entity.ContractsNew;
import com.example.house.entity.ContractsOld;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.font.FontProvider;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * PDF生成服务
 */
@Service
public class PdfGeneratorService {

    /**
     * 生成六楼合同PDF
     */
    public byte[] generateNewContractPdf(ContractsNew contract) throws IOException {
        String htmlContent = generateNewContractHtml(contract);
        return convertHtmlToPdf(htmlContent);
    }

    /**
     * 生成五楼合同PDF
     */
    public byte[] generateOldContractPdf(ContractsOld contract) throws IOException {
        String htmlContent = generateOldContractHtml(contract);
        return convertHtmlToPdf(htmlContent);
    }

    /**
     * 生成六楼合同HTML内容
     */
    private String generateNewContractHtml(ContractsNew contract) {
        return buildContractHtml(
            contract.getContractNumber(),
            contract.getCreatedAt() != null ? contract.getCreatedAt().toString().substring(0, 10) : "",
            contract.getLandlordName() != null ? contract.getLandlordName() : "房东",
            contract.getLandlordPhone() != null ? contract.getLandlordPhone() : "",
            contract.getTenantName(),
            contract.getTenantPhone(),
            contract.getTenantIdCard(),
            contract.getRoomNumber(),
            contract.getMonthlyRent() != null ? contract.getMonthlyRent().doubleValue() : 0.0,
            contract.getDeposit() != null ? contract.getDeposit().doubleValue() : 0.0,
            contract.getPaymentMethod() != null ? contract.getPaymentMethod() : "月付",
            contract.getContractStartDate() != null ? contract.getContractStartDate().toString() : "",
            contract.getContractEndDate() != null ? contract.getContractEndDate().toString() : "",
            contract.getContractDuration(),
            contract.getUtilitiesIncluded() != null && contract.getUtilitiesIncluded() == 1 ? "包含" : "不包含",
            contract.getWaterRate() != null ? contract.getWaterRate().doubleValue() : 3.5,
            contract.getElectricityRate() != null ? contract.getElectricityRate().doubleValue() : 0.6,
            contract.getContractTerms(),
            contract.getSpecialAgreement(),
            contract.getRemarks()
        );
    }

    /**
     * 生成五楼合同HTML内容
     */
    private String generateOldContractHtml(ContractsOld contract) {
        return buildContractHtml(
            contract.getContractNumber(),
            contract.getCreatedAt() != null ? contract.getCreatedAt().toString().substring(0, 10) : "",
            contract.getLandlordName() != null ? contract.getLandlordName() : "房东",
            contract.getLandlordPhone() != null ? contract.getLandlordPhone() : "",
            contract.getTenantName(),
            contract.getTenantPhone(),
            contract.getTenantIdCard(),
            contract.getRoomNumber(),
            contract.getMonthlyRent() != null ? contract.getMonthlyRent().doubleValue() : 0.0,
            contract.getDeposit() != null ? contract.getDeposit().doubleValue() : 0.0,
            contract.getPaymentMethod() != null ? contract.getPaymentMethod() : "月付",
            contract.getContractStartDate() != null ? contract.getContractStartDate().toString() : "",
            contract.getContractEndDate() != null ? contract.getContractEndDate().toString() : "",
            contract.getContractDuration(),
            contract.getUtilitiesIncluded() != null && contract.getUtilitiesIncluded() == 1 ? "包含" : "不包含",
            contract.getWaterRate() != null ? contract.getWaterRate().doubleValue() : 3.5,
            contract.getElectricityRate() != null ? contract.getElectricityRate().doubleValue() : 0.6,
            contract.getContractTerms(),
            contract.getSpecialAgreement(),
            contract.getRemarks()
        );
    }

    /**
     * 构建合同HTML内容
     */
    private String buildContractHtml(String contractNumber, String createdDate, String landlordName,
                                   String landlordPhone, String tenantName, String tenantPhone,
                                   String tenantIdCard, String roomNumber, Double monthlyRent,
                                   Double deposit, String paymentMethod, String startDate,
                                   String endDate, Integer duration, String utilitiesIncluded,
                                   Double waterRate, Double electricityRate, String contractTerms,
                                   String specialAgreement, String remarks) {

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: 'SimSun', serif; font-size: 12px; line-height: 1.6; margin: 40px; }");
        html.append("h1 { text-align: center; font-size: 20px; font-weight: bold; margin-bottom: 30px; }");
        html.append(".contract-header { text-align: center; margin-bottom: 30px; border-bottom: 2px solid #333; padding-bottom: 20px; }");
        html.append(".section { margin-bottom: 20px; }");
        html.append(".section-title { font-weight: bold; font-size: 14px; margin-bottom: 10px; color: #333; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }");
        html.append("td { padding: 8px; border: 1px solid #ddd; }");
        html.append(".highlight { background-color: #f5f5f5; }");
        html.append(".signature-section { margin-top: 40px; }");
        html.append(".signature-row { display: flex; justify-content: space-between; margin-bottom: 40px; }");
        html.append(".signature-item { width: 45%; }");
        html.append(".underline { border-bottom: 1px solid #333; display: inline-block; width: 200px; margin-left: 10px; }");
        html.append("</style>");
        html.append("</head><body>");

        // 合同标题
        html.append("<div class='contract-header'>");
        html.append("<h1>房屋租赁合同</h1>");
        html.append("<table>");
        html.append("<tr><td class='highlight' style='width: 120px;'>合同编号</td><td>").append(contractNumber).append("</td></tr>");
        html.append("<tr><td class='highlight'>签订日期</td><td>").append(createdDate).append("</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 甲方信息
        html.append("<div class='section'>");
        html.append("<div class='section-title'>甲方（出租方）信息</div>");
        html.append("<table>");
        html.append("<tr><td class='highlight' style='width: 120px;'>姓名</td><td>").append(landlordName).append("</td></tr>");
        html.append("<tr><td class='highlight'>联系电话</td><td>").append(landlordPhone).append("</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 乙方信息
        html.append("<div class='section'>");
        html.append("<div class='section-title'>乙方（承租方）信息</div>");
        html.append("<table>");
        html.append("<tr><td class='highlight' style='width: 120px;'>姓名</td><td>").append(tenantName).append("</td></tr>");
        html.append("<tr><td class='highlight'>联系电话</td><td>").append(tenantPhone).append("</td></tr>");
        html.append("<tr><td class='highlight'>身份证号</td><td>").append(tenantIdCard).append("</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 房屋信息
        html.append("<div class='section'>");
        html.append("<div class='section-title'>房屋信息</div>");
        html.append("<table>");
        html.append("<tr><td class='highlight' style='width: 120px;'>房间号</td><td>").append(roomNumber).append("</td></tr>");
        html.append("<tr><td class='highlight'>月租金</td><td>￥").append(monthlyRent).append(" 元</td></tr>");
        html.append("<tr><td class='highlight'>押金</td><td>￥").append(deposit).append(" 元</td></tr>");
        html.append("<tr><td class='highlight'>付款方式</td><td>").append(paymentMethod).append("</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 合同期限
        html.append("<div class='section'>");
        html.append("<div class='section-title'>合同期限</div>");
        html.append("<table>");
        html.append("<tr><td class='highlight' style='width: 120px;'>开始日期</td><td>").append(startDate).append("</td></tr>");
        html.append("<tr><td class='highlight'>结束日期</td><td>").append(endDate).append("</td></tr>");
        html.append("<tr><td class='highlight'>租期</td><td>").append(duration).append(" 个月</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 水电费用
        html.append("<div class='section'>");
        html.append("<div class='section-title'>水电费用</div>");
        html.append("<table>");
        html.append("<tr><td class='highlight' style='width: 120px;'>是否包含水电费</td><td>").append(utilitiesIncluded).append("</td></tr>");
        html.append("<tr><td class='highlight'>水费单价</td><td>￥").append(waterRate).append(" 元/吨</td></tr>");
        html.append("<tr><td class='highlight'>电费单价</td><td>￥").append(electricityRate).append(" 元/度</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 合同条款
        if (contractTerms != null && !contractTerms.trim().isEmpty()) {
            html.append("<div class='section'>");
            html.append("<div class='section-title'>合同条款</div>");
            html.append("<div style='padding: 10px; border: 1px solid #ddd; background-color: #f9f9f9;'>");
            html.append(contractTerms.replace("\n", "<br/>"));
            html.append("</div>");
            html.append("</div>");
        }

        // 特殊约定
        if (specialAgreement != null && !specialAgreement.trim().isEmpty()) {
            html.append("<div class='section'>");
            html.append("<div class='section-title'>特殊约定</div>");
            html.append("<div style='padding: 10px; border: 1px solid #ddd; background-color: #f9f9f9;'>");
            html.append(specialAgreement.replace("\n", "<br/>"));
            html.append("</div>");
            html.append("</div>");
        }

        // 备注
        if (remarks != null && !remarks.trim().isEmpty()) {
            html.append("<div class='section'>");
            html.append("<div class='section-title'>备注</div>");
            html.append("<div style='padding: 10px; border: 1px solid #ddd; background-color: #f9f9f9;'>");
            html.append(remarks.replace("\n", "<br/>"));
            html.append("</div>");
            html.append("</div>");
        }

        // 签字区域
        html.append("<div class='signature-section'>");
        html.append("<table>");
        html.append("<tr>");
        html.append("<td style='width: 50%; padding: 20px;'>甲方签字：<span class='underline'></span></td>");
        html.append("<td style='width: 50%; padding: 20px;'>乙方签字：<span class='underline'></span></td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td style='padding: 20px;'>日期：<span class='underline'></span></td>");
        html.append("<td style='padding: 20px;'>日期：<span class='underline'></span></td>");
        html.append("</tr>");
        html.append("</table>");
        html.append("</div>");

        html.append("</body></html>");
        return html.toString();
    }

    /**
     * 将HTML转换为PDF
     */
    private byte[] convertHtmlToPdf(String htmlContent) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // 设置转换属性
            ConverterProperties converterProperties = new ConverterProperties();
            
            // 设置字体提供者以支持中文
            FontProvider fontProvider = new FontProvider();
            fontProvider.addStandardPdfFonts();
            
            // 尝试添加中文字体支持
            try {
                fontProvider.addFont("STSong-Light", "UniGB-UCS2-H");
            } catch (Exception e) {
                // 如果字体不可用，使用默认字体
                System.err.println("中文字体加载失败，使用默认字体: " + e.getMessage());
            }
            
            converterProperties.setFontProvider(fontProvider);
            
            // 转换HTML为PDF
            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
            
        } catch (Exception e) {
            throw new IOException("PDF生成失败: " + e.getMessage(), e);
        }

        return outputStream.toByteArray();
    }
} 