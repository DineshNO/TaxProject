package com.tax.calculator;

import java.math.BigDecimal;

public interface TaxService {

    /**
     * Calculates and returns the net tax which is the sum of Municiapality tax,
     * Social security Tax and ordindary income tax
     * @param taxableAmt
     * @return net tax amount to be be paid
     */
    BigDecimal calculateNetTax(BigDecimal taxableAmt);

    /**
     * Calculates the Municipality tax based on the basis amount
     * which is the net taxable income after deductions.
     * The tax amount is calculated by deducting standard and applying applicable rates
     * as defined for the category.
     * @param taxableAmt
     * @return net municaipal tax amount
     */
    BigDecimal calculateMunicipalTax(BigDecimal taxableAmt);

    /**
     * Calculates the social security tax .
     * The amount is arrived by calculating on socialSecurity
     * defined rate and it cannot exceed amount calculated based
     * levelling rate for the amount above threshold limit
     * @param taxableAmt
     * @return social security tax amount
     */
    BigDecimal calculateSocialSecurityTax(BigDecimal taxableAmt);

    /**
     * Calculates the social security tax for the levelling rate
     * on the amount more than the threshold defined.
     * @param taxableAmt
     * @return social security tax amount for levelling rate
     */
    BigDecimal socialSecurityTax_levellingRate(BigDecimal taxableAmt);


}
