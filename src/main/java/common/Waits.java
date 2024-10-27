package common;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import pages.BasePage;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Waits extends BasePage {
    private final Duration explicitDuration = Duration.of(EXPLICIT_TIMEOUT, ChronoUnit.SECONDS);

    private Wait<WebDriver> baseFluent(WebDriver driver){
        return new FluentWait<>(driver)
                .withTimeout(explicitDuration)
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(UnreachableBrowserException.class)
                .ignoring(WebDriverException.class)
                .ignoring(NullPointerException.class);
    }

    private Wait<WebDriver> baseFluent(WebDriver driver, Duration timeout){
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(UnreachableBrowserException.class)
                .ignoring(WebDriverException.class)
                .ignoring(NullPointerException.class);
    }

    public void waitForClickable(By selector){
        baseFluent(driver).until(ExpectedConditions.elementToBeClickable(selector));
    }

    public void waitForClickable(WebElement element){
        baseFluent(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    public void attributeNotContains(By selector, String attribute, String value){
        baseFluent(driver).until(ExpectedConditions.not(ExpectedConditions.attributeContains(selector, attribute, value)));
    }

    public void attributeNotContains(WebElement element, String attribute, String value){
        baseFluent(driver).until(ExpectedConditions.not(ExpectedConditions.attributeContains(element, attribute, value)));
    }

    public void waitForVisibility(By selector){
        baseFluent(driver).until(ExpectedConditions.visibilityOfElementLocated(selector));
    }

    public void waitForVisibility(By parent, By child){
        baseFluent(driver).until(ExpectedConditions.visibilityOf(driver.findElement(parent).findElement(child)));
    }

    public void waitForNotVisible(By selector){
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        baseFluent(driver).until(ExpectedConditions.invisibilityOfElementLocated(selector));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_TIMEOUT));
    }

    public void waitForNotVisible(By selector, int timeout){
        baseFluent(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.invisibilityOfElementLocated(selector));
    }

    public void waitForNotVisible(WebElement element){
        baseFluent(driver).until(ExpectedConditions.invisibilityOf(element));
    }

    public void visibilityOfAllElementsLocatedBy(By selector){
        baseFluent(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(selector));
    }

    public void waitForTextToBe(By selector, String newText){
        baseFluent(driver).until(ExpectedConditions.textToBe(selector, newText));
    }

    public void waitForTextToBe(WebElement element, String text){
        baseFluent(driver).until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public void waitForTextChange(By selector, String oldText){
        baseFluent(driver).until(ExpectedConditions.not(ExpectedConditions.textToBe(selector, oldText)));
    }

    public void waitForTextChange(WebElement element, String oldText){
        baseFluent(driver).until(ExpectedConditions.refreshed(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, oldText))));
    }

    public void waitForDisappearElementWithText(By selector, String text){
        baseFluent(driver).until(ExpectedConditions.invisibilityOfElementWithText(selector, text));
    }

    public void attributeToBe(By selector, String attribute, String value){
        baseFluent(driver).until(ExpectedConditions.attributeToBe(selector, attribute, value));
    }

    public void attributeToBe(WebElement element, String attribute, String value){
        baseFluent(driver).until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public void attributeNotToBe(WebElement element, String attribute, String value){
        baseFluent(driver).until(ExpectedConditions.not(ExpectedConditions.attributeToBe(element, attribute, value)));
    }

    public void attributeNotToBe(By selector, String attribute, String value){
        baseFluent(driver).until(ExpectedConditions.not(ExpectedConditions.attributeToBe(selector, attribute, value)));
    }

    public void attributeToBeNotEmpty(WebElement element, String attribute){
        baseFluent(driver).until(ExpectedConditions.attributeToBeNotEmpty(element, attribute));
    }

    public void attributeContains(By selector, String attribute, String value){
        baseFluent(driver).until(ExpectedConditions.attributeContains(selector, attribute, value));
    }

    public void attributeContains(WebElement element, String attribute, String value){
        baseFluent(driver).until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    public void waitForStaleness(WebElement element){
        baseFluent(driver).until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForNumberOfElementsToBe(By selector, int count){
        baseFluent(driver).until(ExpectedConditions.numberOfElementsToBe(selector, count));
    }

    public void waitForNumberOfElementsMoreThan(By selector, int count){
        baseFluent(driver).until(ExpectedConditions.numberOfElementsToBeMoreThan(selector, count));
    }

    public void waitForNumberOfElementsLessThan(By selector, int count){
        baseFluent(driver).until(ExpectedConditions.numberOfElementsToBeLessThan(selector, count));
    }

    public void waitForUrlContains(String URL){
        baseFluent(driver).until(ExpectedConditions.urlContains(URL));
    }

    public void textToBePresentInElementLocated(By selector, String text){
        baseFluent(driver).until(ExpectedConditions.textToBePresentInElementLocated(selector, text));
    }

    public void waitIFrameToLoadAndSwitch(By selector){
        baseFluent(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(selector));
    }

    public void waitForFilePresent(File file){
        baseFluent(driver).until((ExpectedCondition<Boolean>) webDriver -> file.exists());
    }

    public void waitForTabClose(){
        int oldCnt = driver.getWindowHandles().size();
        baseFluent(driver).until((ExpectedCondition<Boolean>) webDriver -> driver.getWindowHandles().size() < oldCnt);
    }

    public void sleep(long millisToWait){
        try{
            Thread.sleep(millisToWait);
        } catch (InterruptedException e){
            System.err.println("Wait was interrupted: \n" + e);
        }
    }

}
