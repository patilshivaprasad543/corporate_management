package com.corporate.travel.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.entity.ExpenseItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AIExpenseFraudService {
    private static final Logger log = LoggerFactory.getLogger(AIExpenseFraudService.class);


    public List<String> inspectExpense(ExpenseItem item) {
        List<String> flags = new ArrayList<>();
        if (item.getAmount() != null && item.getAmount().compareTo(BigDecimal.valueOf(50000)) > 0) {
            flags.add("High-value single expense exceeds standard automatic audit ceiling");
        }
        return flags;
    }
}
