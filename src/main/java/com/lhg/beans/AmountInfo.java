package com.lhg.beans;

import java.math.BigDecimal;

public class AmountInfo {

    //时间
    private String amountDate;
    //类型
    private String amountType;
    //金额
    private BigDecimal amount;
    //序号
    private int index;

    public AmountInfo(String amountDate, String amountType, BigDecimal amount, int index) {
        this.amountDate = amountDate;
        this.amountType = amountType;
        this.amount = amount;
        this.index = index;
    }

    public String getAmountDate() {
        return amountDate;
    }

    public void setAmountDate(String amountDate) {
        this.amountDate = amountDate;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "AmountInfo{" +
                "amountDate='" + amountDate + '\'' +
                ", amountType='" + amountType + '\'' +
                ", amount=" + amount +
                ", index=" + index +
                '}';
    }
}
