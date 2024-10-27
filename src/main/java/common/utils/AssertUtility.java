package common.utils;

import common.Selectors;
import org.openqa.selenium.*;

import java.time.Duration;


public class AssertUtility {
    private final WebDriver driver;
    private final long defaultWait = 150;
    private final Selectors Select = new Selectors();

    public AssertUtility(WebDriver driver) {
        this.driver = driver;
    }

    public boolean assertPresent(By selector) {
        boolean isPresent;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        try {
            isPresent = !Select.multiSelector(selector).isEmpty() && Select.multiSelector(selector).get(0).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException |
                 IndexOutOfBoundsException ex) {
            isPresent = false;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWait));
        return isPresent;
    }

    public boolean assertPresent(By parentSelector, By childSelector) {
        boolean ret;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        try {
            ret = !Select.multiSelector(parentSelector, childSelector).isEmpty() && Select.multiSelector(parentSelector, childSelector).get(0).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = false;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWait));
        return ret;
    }

    public boolean assertPresent(WebElement element) {
        boolean ret = true;
        try {
            element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = false;
        }
        return ret;
    }

    public boolean assertPresent(WebElement parentElement, By childSelector) {
        boolean ret;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        try {
            ret = !Select.multiSelector(parentElement, childSelector).isEmpty() && Select.multiSelector(parentElement, childSelector).get(0).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = false;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWait));
        return ret;
    }

    public boolean assertNotPresent(By selector) {
        boolean ret;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        try {
            ret = Select.multiSelector(selector).isEmpty();
            if (!ret) {
                ret = Select.selector(selector).getSize().getWidth() <= 0;
            }
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = true;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWait));
        return ret;
    }

    public boolean assertNotPresent(By parentSelector, By childSelector) {
        boolean ret;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        try {
            ret = Select.multiSelector(parentSelector, childSelector).isEmpty();
            if (!ret) {
                ret = Select.selector(parentSelector, childSelector).getSize().getWidth() <= 0;
            }
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = true;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWait));
        return ret;
    }

    public boolean assertNotPresent(WebElement parentElement, By childSelector) {
        boolean ret;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        try {
            ret = Select.multiSelector(parentElement, childSelector).isEmpty();
            if (!ret) {
                ret = Select.selector(parentElement, childSelector).getSize().getWidth() <= 0;
            }
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = true;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWait));
        return ret;
    }

    public boolean assertNotPresent(WebElement element) {
        /*This method will always have a pause, because the element is searched by the external selector with the default implicitlyWait*/
        boolean ret = false;
        try {
            element.isDisplayed();
            if (element.isDisplayed()) {
                ret = element.getSize().getWidth() <= 0;
            }
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = true;
        }
        return ret;
    }

    public boolean assertNotPresent(By locator, String attributeName, String attributeValue) {
        boolean ret;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        try {
            ret = Select.selectByAttributeExact(locator, attributeName, attributeValue) == null;
            if (!ret) {
                ret = Select.selectByAttributeExact(locator, attributeName, attributeValue).getSize().getWidth() <= 0;
            }
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = true;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWait));
        return ret;
    }

    public boolean assertNotPresent(WebElement parent, By locator, String attributeName, String attributeValue) {
        boolean ret;
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(300));
        try {
            ret = Select.selectByAttributeExact(parent, locator, attributeName, attributeValue) == null;
            if (!ret) {
                ret = Select.selectByAttributeExact(parent, locator, attributeName, attributeValue).getSize().getWidth() <= 0;
            }
        } catch (NoSuchElementException | StaleElementReferenceException | NullPointerException ex) {
            ret = true;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultWait));
        return ret;
    }
}
