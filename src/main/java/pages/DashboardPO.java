package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.utils.BasicUtility;
import common.Waits;
import data.UserEnum;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.asserts.Assertion;

import java.util.List;

import static pages.ReportPO.backButton;
import static pages.ReportPO.reportTableLoading;

public class DashboardPO extends CommonElementsPO {
    public static Assertion hardAssert = new Assertion();
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final BasicUtility Util = new BasicUtility();

    public DashboardPO(){
        softAssert = new SoftAssertCustom();
        isSoftAssertLocal = true;
    }

    public DashboardPO(SoftAssertCustom softAssert) {
        this.softAssert = softAssert;
        isSoftAssertLocal = false;
    }

    @Step("Go to dashboard")
    public void gotoDashboard(){
        Select.selector(dashboardSectionButton).click();
        Wait.waitForVisibility(widgetCard);
    }

    static final By
            widgetCard = By.xpath("//div[@class='card-content'][not(@style)]"),
            widgetCardCount = By.xpath(".//div[@class='count']/a"),
            widgetCardText = By.xpath(".//div[contains(@class, 'upper')]"),
            widgetTable = By.xpath("//div[@class='card-content'][@style]");

    @Step("Assert Dashboard for user role: {role}")
    public void assertDashboard(UserEnum.userRoleEnum role){
        List<WebElement> cards = Select.multiSelector(widgetCard), tables = Select.multiSelector(widgetTable);
        assertCalendarRange(Util.getCurrentDateTime(), Util.getCurrentDateTime(), "Admin/Publisher dashboard");
        switch (role){
            case ADM, PUBL:
                softAssert.assertTrue(Select.selector(cards.get(0), widgetCardCount).getText().matches("\\$\\d+\\.\\d{1,2}"), "Revenue widget count is incorrect");
                softAssert.assertTrue(Select.selector(cards.get(1), widgetCardCount).getText().matches("\\d+"), "Impressions widget count is incorrect");
                softAssert.assertTrue(Select.selector(cards.get(2), widgetCardCount).getText().matches("\\d+\\.\\d{1,2}\\%"), "Fill rate widget count is incorrect");
                softAssert.assertTrue(Select.selector(cards.get(3), widgetCardCount).getText().matches("\\$\\d+\\.\\d{1,2}"), "Balance widget count is incorrect");
                softAssert.assertFalse(Double.parseDouble(Select.selector(cards.get(0), widgetCardCount).getText().replace("$", "")) < 0, "Revenue widget count is less than 0");
                softAssert.assertFalse(Integer.parseInt(Select.selector(cards.get(1), widgetCardCount).getText()) < 0, "Impressions widget count is less than 0");
                softAssert.assertFalse(Double.parseDouble(Select.selector(cards.get(2), widgetCardCount).getText().replace("%", "")) < 0, "Fill rate widget count is less than 0");
                softAssert.assertFalse(Double.parseDouble(Select.selector(cards.get(3), widgetCardCount).getText().replace("$", "")) < 0, "Balance widget count is less than 0");
                softAssert.assertEquals(Select.selector(cards.get(0), widgetCardText).getText(), "REVENUE FOR TODAY", "Revenue widget text is incorrect");
                softAssert.assertEquals(Select.selector(cards.get(1), widgetCardText).getText(), "IMPRESSIONS FOR TODAY", "Impressions widget text is incorrect");
                softAssert.assertEquals(Select.selector(cards.get(2), widgetCardText).getText(), "FILL RATE FOR TODAY", "Fill rate widget text is incorrect");
                softAssert.assertEquals(Select.selector(cards.get(3), widgetCardText).getText(), "BALANCE", "Balance widget text is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(0), relative_generalTableHeadCell).get(0).getText(), "Id", "Inventory table ID header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(0), relative_generalTableHeadCell).get(1).getText(), "Name", "Inventory table Name header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(0), relative_generalTableHeadCell).get(2).getText(), "Impressions", "Inventory table Impressions header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(0), relative_generalTableHeadCell).get(3).getText(), "Fill Rate", "Inventory table Fill Rate header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(0), relative_generalTableHeadCell).get(4).getText(), "Revenue", "Inventory table Revenue header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(0), relative_generalTableHeadCell).get(5).getText(), "", "Inventory table report header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(1), relative_generalTableHeadCell).get(0).getText(), "Id", "Placement table ID header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(1), relative_generalTableHeadCell).get(1).getText(), "Name", "Placement table Name header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(1), relative_generalTableHeadCell).get(2).getText(), "Impressions", "Placement table Impressions header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(1), relative_generalTableHeadCell).get(3).getText(), "Fill Rate", "Placement table Fill Rate header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(1), relative_generalTableHeadCell).get(4).getText(), "Revenue", "Placement table Revenue header is incorrect");
                softAssert.assertEquals(Select.multiSelector(tables.get(1), relative_generalTableHeadCell).get(5).getText(), "", "Placement table report header is incorrect");
                break;
            case PUBL_SSP:
                //                assertPublSspDashboard(); //TODO: Implement SSP dashboard
                break;
            case PUBL_DSP:
                //                assertPublDspDashboard(); //TODO: Implement DSP dashboard
                break;
        }
    }

    @Step("Click widget link")
    public void clickWidgetReport(int widgetNumber){
        List<WebElement> cards = Select.multiSelector(widgetCard);
        Select.selector(cards.get(widgetNumber), widgetCardCount).click();
        Wait.waitForClickable(backButton);
        Wait.waitForNotVisible(reportTableLoading, 120);
    }

    @Step("Click inventory name")
    public void clickInventoryName(){
        WebElement cell = Select.multiSelector(Select.multiSelector(widgetTable).get(0), relative_generalTableBodyCell).get(1);
        String inventoryName = Select.selector(cell, relative_generalLink).getText();
        Select.selector(cell, relative_generalLink).click();
        Wait.waitForClickable(submitButton);
        softAssert.assertEquals(Select.selector(titleInput).getAttribute("value"), inventoryName, "Inventory: Name is incorrect");
    }

    @Step("Check, if there are more than 0 impressions in top inventory")
    public boolean checkInventoryImpressionPresent(){
        List<WebElement> cells = Select.multiSelector(Select.multiSelector(widgetTable).get(0), relative_generalTableBodyCell);
        return Integer.parseInt(cells.get(2).getText()) > 0;
    }

    @Step("Click inventory report")
    public String clickInventoryReport(){
        List<WebElement> cells = Select.multiSelector(Select.multiSelector(widgetTable).get(0), relative_generalTableBodyCell);
        String inventoryNameId = cells.get(1).getText() + "#" + cells.get(0).getText();
        Select.selector(cells.get(5), relative_generalLink).click();
        Wait.waitForClickable(backButton);
        return inventoryNameId;
    }

    @Step("Click placement name")
    public void clickPlacementName(){
        WebElement cell = Select.multiSelector(Select.multiSelector(widgetTable).get(1), relative_generalTableBodyCell).get(1);
        String placementName = Select.selector(cell, relative_generalLink).getText();
        Select.selector(cell, relative_generalLink).click();
        Wait.waitForClickable(submitButton);
        softAssert.assertEquals(Select.selector(titleInput).getAttribute("value"), placementName, "Placement: Name is incorrect");
    }

    @Step("Check, if there are more than 0 impressions in top placement")
    public boolean checkPlacementImpressionPresent(){
        List<WebElement> cells = Select.multiSelector(Select.multiSelector(widgetTable).get(1), relative_generalTableBodyCell);
        return Integer.parseInt(cells.get(2).getText()) > 0;
    }

    @Step("Click placement report")
    public String clickPlacementReport(){
        List<WebElement> cells = Select.multiSelector(Select.multiSelector(widgetTable).get(1), relative_generalTableBodyCell);
        String placementNameId = cells.get(1).getText() + "#" + cells.get(0).getText();
        Select.selector(cells.get(5), relative_generalLink).click();
        Wait.waitForClickable(backButton);
        return placementNameId;
    }

}
