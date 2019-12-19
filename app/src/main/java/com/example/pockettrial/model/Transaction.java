package com.example.pockettrial.model;

import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private String to;
    private String toCurrency;
    private String fromCurrency;
    private Double amount;
    private Double convertedAmount;


    public Double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(Double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }


    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }



    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }





    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("to", to);
        result.put("toCurrency", toCurrency);
        result.put("fromCurrency", fromCurrency);
        result.put("convertedAmount", convertedAmount);
        result.put("amount", amount);

        return result;
    }
}
