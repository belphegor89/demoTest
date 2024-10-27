package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.AssertUtility;
import common.utils.RandomUtility;
import data.CommonEnums;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdsTxtPartnersPO extends CommonElementsPO {
    private final boolean isSoftAssertLocal;
    private final SoftAssertCustom softAssert;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final RandomUtility RandomUtil = new RandomUtility();
    private final AssertUtility AssertUtil = new AssertUtility(driver);

    public AdsTxtPartnersPO(SoftAssertCustom softAssert) {
        this.softAssert = softAssert;
        this.isSoftAssertLocal = false;
    }

    public AdsTxtPartnersPO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open Ads TXT Partners page")
    public void gotoAdsTxtPartnersSection() {
        openAdminMenu();
        clickAdxActivityWrap(true);
        Select.selector(adsTxtPartnersLink).click();
        Wait.attributeNotContains(adsTxtPartnersListTable, "class", "disabled");
    }

    //<editor-fold desc="Ads.txt partners list">
    private static final By
            adsTxtPartnersListTable = By.xpath("//section[@id='page-content']//table"),
            addAdsTxtPartnersButton = By.xpath("//div/a[contains(@href, 'ad-exchange/ads-txt-partners/create')]"),
            adsTxtPartnerRow = By.xpath("//div[@id='sellersTableLists']//tbody/tr"),
            adsTxtPartnerName = By.xpath(".//td[@data-column='name']"),
            adsTxtPartnerId = By.xpath(".//td[@data-column='seller_id']"),
            adsTxtPartnerDomain = By.xpath(".//td[@data-column='domain']"),
            adsTxtPartnerDirectness = By.xpath(".//td[@data-column='directness']"),
            adsTxtPartnerComment = By.xpath(".//td[@data-column='comment']"),
            adsTxtPartnerCountDsp = By.xpath(".//td[@data-column='call|dsp']"),
            adsTxtPartnersListLoader = By.xpath("//ul[@id='dealsList']//div[contains(@class, 'loading')]"),
            adsTxtPartnersEditButton = By.xpath(".//td[@data-column='call|action']//a[contains(@href, 'update')]"),
            adsTxtPartnersDeleteButton = By.xpath("//div/a[contains(@title, 'Delete Seller')]"),
            adsTxtPartnersPagingNumberButton = By.xpath("//ul[contains(@class,'pagination')]//a"),
            deleteSellerButton = By.xpath(".//td[@data-column='call|action']//a[contains(@href, '#')]");

    @Step("Get ads.txt partners row by name")
    public WebElement getAdsTxtPartnersRow(String sellerId) {
        WebElement partner = Select.selectParentByTextContains(adsTxtPartnerRow, adsTxtPartnerId, sellerId);
        hardAssert.assertNotNull(partner, "There is no Ads Txt Partner with the name <" + sellerId + ">");
        return partner;
    }

    @Step("Click Add ads Txt Partner button")
    public void clickAddAdsTxtPartnerButton() {
        Select.selector(addAdsTxtPartnersButton).click();
        Wait.waitForClickable(nameInput);
    }

    @Step("Click Edit ads Txt Partner button")
    public void clickEditAdsTxtPartnerButton(String id) {
        WebElement adsTxtPartnersRow = getAdsTxtPartnersRow(id);
        Select.selector(adsTxtPartnersRow, adsTxtPartnersEditButton).click();
        Wait.waitForClickable(nameInput);
    }

    @Step("Click Delete ads Txt Partner button")
    public void clickDeleteAdsTxtPartnerButton(String name, String id, boolean deleteConfirm) {
        WebElement adsTxtPartnersRow = getAdsTxtPartnersRow(id);
        Select.selector(adsTxtPartnersRow, deleteSellerButton).click();
        Wait.waitForVisibility(modalConfirmButton);
        softAssert.assertEquals(Select.selector(modalHeaderText).getText(), "Are you sure you want to delete " + name + "?");
        if (deleteConfirm) {
            Select.selector(modalConfirmButton).click();
            Wait.attributeContains(adsTxtPartnersListTable, "class", "hide");
            Wait.attributeNotContains(adsTxtPartnersListTable, "class", "hide");
        } else {
            Select.selector(modalCancelButton).click();
            Wait.waitForNotVisible(modalDialog);
        }
    }

    @Step("Assert Deleted Ads Txt Partner in the list")
    public void assertDeletedSellerNameRow(String name, String id) {
        softAssert.assertTrue(AssertUtil.assertNotPresent(getAdsTxtPartnersRow(id)), "Ads.txt [" + name + "] not deleted");
    }

    @Step("Assert deal in the list")
    public void assertAdsTxtPartnerRow(String id, String name, String domain, String directness, String comment, Integer countDsp) {
        WebElement adsTxtPartnersRow = getAdsTxtPartnersRow(id);
        softAssert.assertEquals(Select.selector(adsTxtPartnersRow, adsTxtPartnerId).getText(), id, "Table row: wrong Seller Id");
        softAssert.assertEquals(Select.selector(adsTxtPartnersRow, adsTxtPartnerName).getText(), name, "Table row: wrong Seller name");
        softAssert.assertEquals(Select.selector(adsTxtPartnersRow, adsTxtPartnerDomain).getText(), domain, "Table row: wrong domain");
        softAssert.assertEquals(Select.selector(adsTxtPartnersRow, adsTxtPartnerComment).getText(), comment, "Table row: wrong Comment");
        softAssert.assertEquals(Select.selector(adsTxtPartnersRow, adsTxtPartnerDirectness).getText(), directness, "Table row: wrong Directness ");
        softAssert.assertEquals(Integer.valueOf(Select.selector(adsTxtPartnersRow, adsTxtPartnerCountDsp).getText()), countDsp, "Table row: wrong  Allow DSP ");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Ads Txt Partner row is incorrect: ");
        }
    }

    //Pagination on The Ads.Txt Page
    @Step("Look up for ads.txt partner")
    public void lookForAdsTxtPartner(String id, String name) {
        for (int i = 0; i < getPagingCount(); i++) {
            try {
                getAdsTxtPartnersRow(id);
                break;
            } catch (AssertionError e) {
                pagingActionSelect(CommonEnums.pagingActionsTypes.FORWARD, null);
                System.err.println("Ads.txt with name " + name + " not found on page " + i);
            }
        }
    }

    static final By pagingPageNumberButton = By.xpath(".//ul[@class='pagination']/li/a[@href]"),
            relative_pagingPageNumberParent = By.xpath("./parent::li");

    //On page Ads.txt locators different from Common
    @Step("Go to the selected page of the list")
    public void pagingActionSelect(CommonEnums.pagingActionsTypes pagingAction, String pageNumber) {
        if (AssertUtil.assertPresent(pagingPageNumberButton)) {
            List<WebElement> pages = Select.multiSelector(pagingPageNumberButton);
            WebElement activePage = Select.selector(pagingPageNumberButton, By.xpath("./../..//li[@class='active']"));
            pages.removeIf(s -> s.getText().equals("»"));
            pages.removeIf(s -> s.getText().equals("«"));
            switch (pagingAction) {
                case FIRST -> {
                    pages.get(0).click();
                }
                case LAST -> {
                    WebElement lastPageElement = pages.get(pages.size() - 1);
                    lastPageElement.click();
                    pageNumber = Select.selector(lastPageElement, relative_pagingPageNumberParent).getText();
                    Wait.waitForStaleness(lastPageElement);
                    lastPageElement = Select.selectByTextEquals(pagingPageNumberButton, pageNumber);
                    Wait.attributeToBe(Select.selector(lastPageElement, relative_pagingPageNumberParent), "class", "active");
                }
                case NUMBER -> {
                }
                case FORWARD -> {
                    int currentIndex = Integer.parseInt(activePage.getText()) - 1;
                    WebElement nextPage = pages.get(currentIndex + 1);
                    nextPage.click();
                    Wait.waitForStaleness(nextPage);

                }
                case BACKWARD -> pages.get(0).click();
            }
        } else {
            System.err.println("There are no pages!");
        }
    }

    @Step("Get number of pages")
    public int getPagingCount() {
        if (AssertUtil.assertPresent(pagingPageNumberButton)) {
            List<WebElement> pages = Select.multiSelector(pagingPageNumberButton);
            pages.removeIf(s -> s.getText().equals("»"));
            pages.removeIf(s -> s.getText().equals("«"));
            return Integer.parseInt(pages.get(pages.size() - 1).getText());
        } else {
            System.err.println("There are no pages!");
            return 0;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Ads TXT Partners Edit Page">
    private static final By
            sellerIDInput = By.xpath("//input[@id='seller_id']"),
            nameInput = By.xpath("//input[@id='name']"),
            domainInput = By.xpath("//input[@id='domain']"),
            hashKeyInput = By.xpath("//input[@id='hash']"),
            commentInput = By.xpath("//textarea[@id='comment']"),
            directnessButton = By.xpath("//div[@class='center-align']//following-sibling::input"),
            dspSelect = By.xpath("//select[@id='dspLists']"),
            saveButton = By.xpath("//button[@type='submit']");

    @Step("Input Seller name")
    public void inputName(String name) {
        enterInInput(nameInput, name);
    }

    @Step("Input Seller Id")
    public void inputSellerId(String id) {
        enterInInput(sellerIDInput, id);
    }

    @Step("Input Domain")
    public void inputDomain(String domain) {
        enterInInput(domainInput, domain);
    }

    @Step("Input Seller Hash Key")
    public void inputHashKey(String hashKey) {
        enterInInput(hashKeyInput, hashKey);
    }

    @Step("Input Seller comment")
    public void inputComment(String comment) {
        enterInInput(commentInput, comment);
    }

    @Step("Click On Directness radiobutton")
    public void clickRadiobutton(CommonEnums.directnessTypeEnum directness) {
        clickRadioButton(directnessButton, directness.publicName());
    }

    @Step("Get random DSP from list")
    public Map<String, Boolean> selectDsps(boolean selectAll) {
        Map<String, Boolean> placementsMap = new HashMap<>();
        Select.selector(dspSelect, relative_selectLabel).click();
        if (selectAll) {
            clickDropdownMultiselectOption(dspSelect, null, true);
        } else {
            int count = RandomUtil.getRandomInt(1, 4);
            placementsMap = getRandomActiveMultiselectOptionsAsMap(dspSelect, null, count);

            for (Map.Entry<String, Boolean> placementEntry : placementsMap.entrySet()) {
                clickDropdownMultiselectOption(dspSelect, placementEntry.getKey(), placementEntry.getValue());
            }
        }
        closeMultiselectList(dspSelect);
        return placementsMap;
    }

    @Step("Click Save Seller button")
    public void clickSaveSeller() {
        Select.selector(saveButton).click();
        Wait.waitForClickable(adsTxtPartnersDeleteButton);
        Wait.attributeNotContains(adsTxtPartnersListTable, "class", "hide");
    }

    @Step("Assert existing Seller Edit page")
    public void assertSellerDetail(String sellerId, String sellerName, String sellerDomain, String sellerComment, String sellerDirectness, String sellerHashKey, Map<String, Boolean> dspList) {
        String dspListPlaceholder = getExpectedMultiselectText(dspList, "All DSP", 10);
        softAssert.assertEquals(Select.selector(sellerIDInput).getAttribute("value"), sellerId, "Edit page: Seller Id is incorrect");
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value"), sellerName, "Edit page: Seller Name is incorrect");
        softAssert.assertEquals(Select.selector(directnessButton).getAttribute("value"), sellerDirectness, "Info page: Seller directness is incorrect");
        softAssert.assertEquals(Select.selector(domainInput).getAttribute("value"), sellerDomain, "Edit page: Seller domain is incorrect");
        softAssert.assertEquals(Select.selector(hashKeyInput).getAttribute("value"), sellerHashKey, "Edit page: Hash Key is incorrect");
        softAssert.assertEquals(Select.selector(commentInput).getText(), sellerComment, "Edit page: Comment is incorrect.");
        softAssert.assertEquals(Select.selector(dspSelect, relative_selectLabel).getText(), dspListPlaceholder, "Edit page: Seller DSP list is incorrect");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Seller details check has errors.");
        }
    }

    //</editor-fold>

}
