package com.tax.calculator;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

public class PersonTaxCalculator2018 implements TaxService{

    static Properties appProps = new Properties();
    static BigDecimal maxTaxAmountSlab1 ;
    static BigDecimal maxTaxAmountSlab2;
    static BigDecimal maxTaxAmountSlab3;
    static BigDecimal socialSecurityLowerLimit;
    static BigDecimal HUNDRED = new BigDecimal("100");
    static {
        try {
            appProps.load(new FileInputStream("PersonTaxDetails.properties"));

            maxTaxAmountSlab1=new BigDecimal(appProps.getProperty("maxTaxSlab1Amount"));
            maxTaxAmountSlab2=new BigDecimal(appProps.getProperty("maxTaxSlab2Amount"));
            maxTaxAmountSlab3=new BigDecimal(appProps.getProperty("maxTaxSlab3Amount"));

            socialSecurityLowerLimit  = new BigDecimal(appProps.getProperty("socialSecurityLowerLimit"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal calculateNetTax(BigDecimal taxableAmt) {
        return getStepTaxAmount(taxableAmt).add(calculateSocialSecurityTax(taxableAmt)).add(calculateMunicipalTax(taxableAmt)).setScale(0, RoundingMode.HALF_DOWN);
    }

    @Override
    public BigDecimal calculateMunicipalTax(BigDecimal taxableAmt) {
        BigDecimal netTaxIncome = taxableAmt.multiply(new BigDecimal(appProps.getProperty("minDeductionInsalaryIncomePer"))).divide(HUNDRED);
        BigDecimal stdDedAmt = new BigDecimal(appProps.getProperty("stdDeduction"));
        BigDecimal netIncome = netTaxIncome.compareTo(stdDedAmt) > 0 ? stdDedAmt : netTaxIncome;
        BigDecimal netIncomeAfterPerAllowanceDed = netIncome.subtract(new BigDecimal(appProps.getProperty("personalAllownaceClass1")));
        if(netIncomeAfterPerAllowanceDed.compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO;
        return new BigDecimal(appProps.getProperty("municiaplityTaxPercent")).multiply(netIncomeAfterPerAllowanceDed).divide(HUNDRED);
    }

    @Override
    public BigDecimal calculateSocialSecurityTax(BigDecimal taxableAmt) {
        if(taxableAmt.compareTo(socialSecurityLowerLimit) < 0) return BigDecimal.ZERO;
        BigDecimal socialSecurityPersonRate  = new BigDecimal(appProps.getProperty("socialSecurityPersonRate"));
        BigDecimal socialSecurityTaxAmount = taxableAmt.multiply(socialSecurityPersonRate).divide(HUNDRED);
        BigDecimal levellingAmount = socialSecurityTax_levellingRate(taxableAmt);
        return (socialSecurityTaxAmount.compareTo(levellingAmount) > 0 ? levellingAmount : socialSecurityTaxAmount).setScale(0, RoundingMode.HALF_DOWN);
    }

    @Override
    public BigDecimal socialSecurityTax_levellingRate(BigDecimal taxableAmt) {
        BigDecimal levellingRate  = new BigDecimal(appProps.getProperty("socialSecurityLevelingRate"));
        return taxableAmt.subtract(socialSecurityLowerLimit).multiply(levellingRate).divide(HUNDRED);
    }

    private BigDecimal getStepTaxAmount(BigDecimal taxableAmt){
        BigDecimal taxSlab4 = new BigDecimal(appProps.getProperty("stepFourSlabAmount"));
        BigDecimal taxSlab3 = new BigDecimal(appProps.getProperty("stepThreeSlabAmount"));
        BigDecimal taxSlab2 = new BigDecimal(appProps.getProperty("stepTwoSlabAmount"));
        BigDecimal taxSlab1 = new BigDecimal(appProps.getProperty("stepOneSlabAmount"));


        BigDecimal taxSlabPercent4 = new BigDecimal(appProps.getProperty("stepFourSlabPercent"));
        BigDecimal taxSlabPercent3 = new BigDecimal(appProps.getProperty("stepThreeSlabPercent"));
        BigDecimal taxSlabPercent2 = new BigDecimal(appProps.getProperty("stepTwoSlabPercent"));
        BigDecimal taxSlabPercent1 = new BigDecimal(appProps.getProperty("stepOneSlabPercent"));

        if(taxableAmt.compareTo(taxSlab4) > 0){
            return maxTaxAmountSlab3.add(taxableAmt.subtract(taxSlab4).multiply(taxSlabPercent4).divide(HUNDRED));
        }
        else if(taxableAmt.compareTo(taxSlab3) > 0){
            return maxTaxAmountSlab2.add(taxableAmt.subtract(taxSlab3).multiply(taxSlabPercent3).divide(HUNDRED));
        }
        else if(taxableAmt.compareTo(taxSlab2) > 0){
            return maxTaxAmountSlab1.add(taxableAmt.subtract(taxSlab2).multiply(taxSlabPercent2).divide(HUNDRED));
        }
        else if(taxableAmt.compareTo(taxSlab1) > 0){
            return taxableAmt.subtract(taxSlab1).multiply(taxSlabPercent1).divide(HUNDRED);
        }
        return BigDecimal.ZERO;
    }

}