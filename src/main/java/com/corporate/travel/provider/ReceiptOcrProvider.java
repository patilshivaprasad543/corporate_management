package com.corporate.travel.provider;

import com.corporate.travel.dto.ExpenseDto;

public interface ReceiptOcrProvider {
    ExpenseDto.OcrScanResponse scanReceipt(String fileName, Double amountHint);
    String getOcrEngineName();
}
