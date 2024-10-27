package common;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.ByChained;
import pages.BasePage;

import java.util.ArrayList;
import java.util.List;

public class Selectors extends BasePage {
    //TODO add an optional parameter/setting (similar to ChromeDriverBuilder) to suppress the error messages
    public static WebElement lastElement;

    //<editor-fold desc="Select single element">
    public WebElement selector(By selector){
        WebElement element = null;
        try{
            element = driver.findElement(selector);
            lastElement = element;
        } catch (NoSuchElementException | NullPointerException noEl){
            System.err.println("Element is not present: " + selector);
        } catch (StaleElementReferenceException stEx){
            System.err.println("Stale element reference: " + selector);
        }
        return element;
    }

    public WebElement selector(By locator1, By locator2){
        WebElement element = null;
        try{
            element = driver.findElement(new ByChained(locator1, locator2));
            lastElement = element;
        } catch (NoSuchElementException | NullPointerException noEl){
            System.err.println("Elements are not present: \n1) " + locator1 + "\n2) " + locator2);
        } catch (StaleElementReferenceException stEx){
            System.err.println("Stale element reference: \n1) " + locator1 + "\n2) " + locator2);
        }
        return element;
    }

    public WebElement selector(WebElement webElement, By locator){
        WebElement element = null;
        try{
            element = webElement.findElement(locator);
            lastElement = element;
        } catch (NoSuchElementException | NullPointerException noEl){
            System.err.println("Elements are not present: \n1) " + webElement + "\n2) " + locator);
        } catch (StaleElementReferenceException stEx){
            System.err.println("Stale element reference: \n1) " + webElement + "\n2) " + locator);
        }
        return element;
    }

    public WebElement selectByTextEquals(By listSelector, String text){
        WebElement element = null;
        for (WebElement item : multiSelector(listSelector)){
            if (item.getText().equalsIgnoreCase(text)){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelector: " + listSelector + "\nText: " + text);
        }
        return element;
    }

    public WebElement selectByTextEquals(By listSelector, By relativeSelector, String text){
        WebElement element = null;
        for (WebElement item : multiSelector(listSelector, relativeSelector)){
            if (item.getText().equalsIgnoreCase(text)){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelectors\n1) " + listSelector + "\n2) " + relativeSelector + "\nText: " + text);
        }
        return element;
    }

    public WebElement selectByTextEquals(WebElement parentSelector, By relativeSelector, String text){
        WebElement element = null;
        for (WebElement item : multiSelector(parentSelector, relativeSelector)){
            if (item.getText().equalsIgnoreCase(text)){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelectors\n1) " + parentSelector + "\n2) " + relativeSelector + "\nText: " + text);
        }
        return element;
    }

    public WebElement selectParentByTextEquals(By listSelector, By relativeSelector, String text){
        WebElement element = null;
        for (WebElement item : multiSelector(listSelector)){
            if (item.findElement(relativeSelector).getText().equalsIgnoreCase(text)){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelectors\n1) " + listSelector + "\n2) " + relativeSelector + "\nText: " + text);
        }
        return element;
    }

    public WebElement selectByTextContains(By listSelector, String text){
        WebElement element = null;
        for (WebElement item : multiSelector(listSelector)){
            if (item.getText().toLowerCase().contains(text.toLowerCase())){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelectors\n1) " + listSelector + "\nText: " + text);
        }
        return element;
    }

    public WebElement selectByTextContains(By listSelector, By relativeSelector, String text){
        WebElement element = null;
        for (WebElement item : multiSelector(listSelector, relativeSelector)){
            if (item.getText().toLowerCase().contains(text.toLowerCase())){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelectors\n1) " + listSelector + "\n2) " + relativeSelector + "\nText: " + text);
        }
        return element;
    }

    public WebElement selectParentByTextContains(By listSelector, By relativeSelector, String text){
        WebElement element = null;
        for (WebElement item : multiSelector(listSelector)){
            if (item.findElement(relativeSelector).getText().contains(text)){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelectors\n1) " + listSelector + "\n2) " + relativeSelector + "\nText: " + text);
        }
        return element;
    }

    public WebElement selectByAttributeIgnoreCase(By listSelector, String attrName, Object attrValue){
        WebElement element = null;
        List<WebElement> multiList = multiSelector(listSelector);
        if (multiList != null){
            for (WebElement item : multiList){
                if (item.getAttribute(attrName).equalsIgnoreCase(String.valueOf(attrValue))){
                    element = item;
                    lastElement = element;
                    break;
                }
            }
            if (element == null){
                System.err.println("Element is not present:\nSelector: " + listSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
            }
        }
        return element;
    }

    public WebElement selectByAttributeIgnoreCase(By listSelector, By relativeSelector, String attrName, Object attrValue){
        WebElement element = null;
        for (WebElement item : multiSelector(listSelector, relativeSelector)){
            if (item.getAttribute(attrName).equalsIgnoreCase(String.valueOf(attrValue))){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelector 1: " + listSelector + "\nSelector 2: " + relativeSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public WebElement selectByAttributeIgnoreCase(WebElement parent, By relativeSelector, String attrName, Object attrValue){
        WebElement element = null;
        for (WebElement item : multiSelector(parent, relativeSelector)){
            if (item.getAttribute(attrName).equalsIgnoreCase(String.valueOf(attrValue))){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelector 1: " + parent + "\nSelector 2: " + relativeSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public WebElement selectByAttributeExact(WebElement parent, By relativeSelector, String attrName, Object attrValue){
        WebElement element = null;
        for (WebElement item : multiSelector(parent, relativeSelector)){
            if (item.getAttribute(attrName).equals(String.valueOf(attrValue))){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelector 1: " + parent + "\nSelector 2: " + relativeSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public WebElement selectByAttributeExact(By selector, String attrName, Object attrValue){
        WebElement element = null;
        for (WebElement item : multiSelector(selector)){
            if (item.getAttribute(attrName).equals(String.valueOf(attrValue))){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelector: " + selector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public WebElement selectByAttributeContains(By listSelector, String attrName, Object attrValue){
        WebElement element = null;
        for (WebElement item : multiSelector(listSelector)){
            if (item.getAttribute(attrName).contains(String.valueOf(attrValue))){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelector: " + listSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public WebElement selectByAttributeContains(WebElement parentElement, By relativeSelector, String attrName, Object attrValue){
        WebElement element = null;
        for (WebElement item : multiSelector(parentElement, relativeSelector)){
            if (item.getAttribute(attrName).contains(String.valueOf(attrValue))){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nParent element: " + parentElement + "\nRelative selector: " + relativeSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public WebElement selectParentByAttributeContains(By parentSelector, By relativeSelector, String attrName, Object attrValue){
        WebElement element = null;
        for (WebElement item : multiSelector(parentSelector)){
            if (item.findElement(relativeSelector).getAttribute(attrName).contains(String.valueOf(attrValue))){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelectors\n1) " + parentSelector + "\n2) " + relativeSelector + "\nAttribute: " + attrName + " = " + attrValue);
        }
        return element;
    }

    public WebElement selectParentByAttributeExact(By parentSelector, By relativeSelector, String attrName, Object attrValue){
        WebElement element = null;
        for (WebElement item : multiSelector(parentSelector)){
            if (item.findElement(relativeSelector).getAttribute(attrName).equals(String.valueOf(attrValue))){
                element = item;
                lastElement = element;
                break;
            }
        }
        if (element == null){
            System.err.println("Element is not present:\nSelectors\n1) " + parentSelector + "\n2) " + relativeSelector + "\nAttribute: " + attrName + " = " + attrValue);
        }
        return element;
    }

    //</editor-fold>

    //<editor-fold desc="Select list of elements">
    public List<WebElement> multiSelector(By selector){
        List<WebElement> elements = null;
        try{
            elements = driver.findElements(selector);
            lastElement = (elements != null) ? (!elements.isEmpty() ? elements.get(0) : null) : null;
        } catch (NoSuchElementException | IndexOutOfBoundsException exc){
            System.err.println("There are no elements: " + selector);
            System.err.println("Error: \n" + exc);
        } catch (StaleElementReferenceException stEx){
            System.err.println("Stale element reference: " + selector);
        }
        return elements;
    }

    public List<WebElement> multiSelector(WebElement webElement, By locator){
        List<WebElement> elements = null;
        try{
            elements = webElement.findElements(locator);
            lastElement = (elements != null) ? (!elements.isEmpty() ? elements.get(0) : null) : null;
        } catch (NoSuchElementException | IndexOutOfBoundsException exc){
            System.err.println("Elements are not present: \n1) " + webElement + "\n2) " + locator);
            System.err.println("Error: \n" + exc);
        } catch (StaleElementReferenceException stEx){
            System.err.println("Stale element reference: \n1) " + webElement + "\n2) " + locator);
        }
        return elements;
    }

    public List<WebElement> multiSelector(By locator1, By locator2){
        List<WebElement> elements = null;
        try{
            elements = driver.findElements(new ByChained(locator1, locator2));
            lastElement = (elements != null) ? (!elements.isEmpty() ? elements.get(0) : null) : null;
        } catch (NoSuchElementException | IndexOutOfBoundsException exc){
            System.err.println("Elements are not present: \n1) " + locator1 + "\n2) " + locator2);
            System.err.println("Error: \n" + exc);
        } catch (StaleElementReferenceException stEx){
            System.err.println("Stale element reference: \n1) " + locator1 + "\n2) " + locator2);
        }
        return elements;
    }

    public List<WebElement> selectListByAttributeContains(By listSelector, String attrName, Object attrValue){
        List<WebElement> element = new ArrayList<>();
        for (WebElement item : multiSelector(listSelector)){
            if (item.getAttribute(attrName).contains(String.valueOf(attrValue))){
                element.add(item);
                lastElement = element.get(element.size() - 1);
            }
        }
        if (element.isEmpty()){
            System.err.println("Element is not present:\nSelector: " + listSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public List<WebElement> selectListByAttributeContains(By parent, By child, String attrName, Object attrValue){
        List<WebElement> element = new ArrayList<>();
        for (WebElement item : multiSelector(parent, child)){
            if (item.getAttribute(attrName).contains(String.valueOf(attrValue))){
                element.add(item);
                lastElement = element.get(element.size() - 1);
            }
        }
        if (element.isEmpty()){
            System.err.println("Can't find element:\nParent: " + parent + "\nChild: " + child + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public List<WebElement> selectListByAttributeContains(WebElement parent, By child, String attrName, Object attrValue){
        List<WebElement> element = new ArrayList<>();
        for (WebElement item : multiSelector(parent, child)){
            if (item.getAttribute(attrName).contains(String.valueOf(attrValue))){
                element.add(item);
                lastElement = element.get(element.size() - 1);
            }
        }
        if (element.isEmpty()){
            System.err.println("Can't find element:\nParent: " + parent + "\nChild: " + child + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public List<WebElement> selectListByAttributeNotContains(By parent, By child, String attrName, Object attrValue){
        List<WebElement> element = new ArrayList<>();
        for (WebElement item : multiSelector(parent, child)){
            if (!item.getAttribute(attrName).contains(String.valueOf(attrValue))){
                element.add(item);
                lastElement = element.get(element.size() - 1);
            }
        }
        if (element.isEmpty()){
            System.err.println("Can't find element:\nParent: " + parent + "\nChild: " + child + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public List<WebElement> selectListByAttributeNotContains(WebElement parent, By child, String attrName, Object attrValue){
        List<WebElement> element = new ArrayList<>();
        for (WebElement item : multiSelector(parent, child)){
            if (!item.getAttribute(attrName).contains(String.valueOf(attrValue))){
                element.add(item);
                lastElement = element.get(element.size() - 1);
            }
        }
        if (element.isEmpty()){
            System.err.println("Can't find element:\nParent: " + parent + "\nChild: " + child + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public List<WebElement> selectListByAttributeExact(By listSelector, String attrName, Object attrValue){
        List<WebElement> element = new ArrayList<>();
        for (WebElement item : multiSelector(listSelector)){
            if (item.getAttribute(attrName).equals(String.valueOf(attrValue))){
                element.add(item);
                lastElement = element.get(element.size() - 1);
            }
        }
        if (element.isEmpty()){
            System.err.println("Element is not present:\nSelector: " + listSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    public List<WebElement> selectListByAttributeExact(By parent, By listSelector, String attrName, Object attrValue){
        List<WebElement> element = new ArrayList<>();
        for (WebElement item : multiSelector(parent, listSelector)){
            if (item.getAttribute(attrName).equals(String.valueOf(attrValue))){
                element.add(item);
                lastElement = element.get(element.size() - 1);
            }
        }
        if (element.isEmpty()){
            System.err.println("Element is not present:\nSelector: " + listSelector + "\nAttribute: " + attrName + "\nValue: " + attrValue);
        }
        return element;
    }

    //</editor-fold>

    //<editor-fold desc="Imitate mouse action">
    public void actionMouseScroll(By selector){
        Actions action = new Actions(driver);
        try{
            action.scrollToElement(selector(selector)).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseScroll(WebElement element){
        Actions action = new Actions(driver);
        try{
            action.scrollToElement(element).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseScrollClick(WebElement element) {
        Actions action = new Actions(driver);
        try {
            action.scrollToElement(element).pause(500).click().build().perform();
        } catch (Exception ex) {
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseOver(By selector){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(selector(selector)).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseOver(By selector, By relativeSelector){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(selector(selector, relativeSelector)).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseOver(By selector, By relativeSelector, int adjustX, int adjustY){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(selector(selector, relativeSelector), adjustX, adjustY).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseOver(WebElement element){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(element).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseOver(WebElement element, By relativeSelector){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(selector(element, relativeSelector)).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseOver(WebElement element, By relativeSelector, int adjustX, int adjustY){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(selector(element, relativeSelector), adjustX, adjustY).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionMouseClickDouble(WebElement element){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(element).doubleClick().build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionClickWithAdjust(By selector, int adjustX, int adjustY){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(selector(selector), adjustX, adjustY).click().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionClickWithAdjust(WebElement element, int adjustX, int adjustY){
        Actions action = new Actions(driver);
        try{
            action.moveToElement(element, adjustX, adjustY).click().build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionClickOnCoordinates(int x, int y) {
        Actions action = new Actions(driver);
        try{
            action.moveByOffset(x, y).click().build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    //</editor-fold>

    //<editor-fold desc="Imitate keyboard action">
    public void actionPressEnter(By selector){
        Actions action = new Actions(driver);
        try{
            action.sendKeys(selector(selector), Keys.ENTER).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionTypeKeys(By selector, String text){
        Actions action = new Actions(driver);
        try{
            action.sendKeys(selector(selector), text).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionTypeKeys(WebElement element, String text){
        Actions action = new Actions(driver);
        try{
            action.sendKeys(element, text).build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }

    public void actionSelectDeleteAll(By selector){
        Actions action = new Actions(driver);
        try{
            action.click(selector(selector))
                    .sendKeys(selector(selector), Keys.CONTROL + "a")
                    .sendKeys(selector(selector), Keys.DELETE)
                    .build().perform();
        } catch (Exception ex){
            System.err.println("Cannot perform action: \n");
            ex.printStackTrace();
        }
    }
    //</editor-fold>

    private void markClickDot(WebElement element, int adjustX, int adjustY, long time) throws InterruptedException {
        Point elementLocation = element.getLocation();
        int clickX = elementLocation.getX() + adjustX;
        int clickY = elementLocation.getY() + adjustY;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String drawDotScript = String.format(
                "var dot = document.createElement('div');" +
                        "dot.style.position = 'absolute';" +
                        "dot.style.left = '%dpx';" +
                        "dot.style.top = '%dpx';" +
                        "dot.style.width = '10px';" +
                        "dot.style.height = '10px';" +
                        "dot.style.backgroundColor = 'red';" +
                        "dot.style.borderRadius = '50%%';" +
                        "dot.style.zIndex = '10000';" +
                        "document.body.appendChild(dot);",
                clickX, clickY
        );
        js.executeScript(drawDotScript);
        // Pause to visualize the dot
        Thread.sleep(time);

    }
}
