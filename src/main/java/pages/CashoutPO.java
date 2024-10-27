package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.ParseAndFormatUtility;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CashoutPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final ParseAndFormatUtility ParseUtil = new ParseAndFormatUtility();

    public CashoutPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public CashoutPO(){
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Go to Cashout page")
    public void gotoCashoutSection(){
        Select.selector(cashoutSettingsSectionButton).click();
    }

    static final By
        paypalTab = By.xpath("//a[@href='#paypal']"),
        wiretransferTab = By.xpath("//a[@href='#wiretransfer']"),
        cashoutLimitText = By.xpath("//div[2]//small[not(@class='hide')]"),
        cashOutToggle = By.xpath("//input[@id='cashout-state']");

    @Step("Select Cashout method")
    public void clickCashoutMethod(boolean isPaypal){
        if(isPaypal){
            Select.selector(paypalTab).click();
        } else {
            Select.selector(wiretransferTab).click();
        }
    }

    @Step("Assert Cashout limit text")
    public void assertCashoutLimitText(boolean isPaypal, double limit){
        String text = "Minimum revenue amount for ${type} cashout is $${limit}",
                type = isPaypal ? "PayPal" : "Wire Transfer", limitString = ParseUtil.formatPriceWithZeros(limit);
        softAssert.assertEquals(Select.selector(cashoutLimitText).getText(), text.replace("${type}", type).replace("${limit}", limitString), "Cashout limit text is not correct");
    }

}
