package com.tax.calculator;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class PersonTaxCalculator2018Test {

    @Test
    public void calculateNetTax() {
        TaxService taxService = new PersonTaxCalculator2018();
        BigDecimal taxAmount = taxService.calculateNetTax(BigDecimal.valueOf(500000L));
        Assert.assertEquals(String.valueOf(taxAmount),"60472");
        BigDecimal taxAmount1 = taxService.calculateNetTax(BigDecimal.valueOf(50000L));
        Assert.assertEquals(String.valueOf(taxAmount1),"0");
    }

    @Test
    public void calculateSocialSecurityTax() {
        TaxService taxService = new PersonTaxCalculator2018();
        BigDecimal socialSecurityTaxAmount1 = taxService.calculateSocialSecurityTax(BigDecimal.valueOf(54000));
        Assert.assertEquals(String.valueOf(socialSecurityTaxAmount1),"0");
        BigDecimal socialSecurityTaxAmount2 = taxService.calculateSocialSecurityTax(BigDecimal.valueOf(75000));
        Assert.assertEquals(String.valueOf(socialSecurityTaxAmount2),"5087");
        BigDecimal socialSecurityTaxAmount3 = taxService.calculateSocialSecurityTax(BigDecimal.valueOf(100000));
        Assert.assertEquals(String.valueOf(socialSecurityTaxAmount3),"8200");
        BigDecimal socialSecurityTaxAmount4 = taxService.calculateSocialSecurityTax(BigDecimal.valueOf(120000));
        Assert.assertEquals(String.valueOf(socialSecurityTaxAmount4),"9840");
    }

}