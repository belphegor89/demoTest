package common;

import common.utils.BrowserUtility;
import io.qameta.allure.Allure;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;

import java.util.Map;

public class SoftAssertCustom extends SoftAssert {
    private final BrowserUtility BROWSER_UTILITY = new BrowserUtility();
    private final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
    private static final String DEFAULT_SOFT_ASSERT_MESSAGE = "The following asserts failed:";

    @Override
    protected void doAssert(IAssert<?> a) {
        onBeforeAssert(a);
        try {
            a.doAssert();
            onAssertSuccess(a);
        } catch (AssertionError ex) {
            onAssertFailure(a, ex);
            m_errors.put(ex, a);
            //TODO: find an appropriate way to get test name not from BaseSuite class
            byte[] screenshot = BROWSER_UTILITY.takeFullPageScreenshot("Test" + "_" + System.currentTimeMillis() + "_assertFail");
            Allure.getLifecycle().addAttachment("Assert fail", "image/png", "png", screenshot);
        } finally {
            onAfterAssert(a);
        }
    }

    public void assertAll() {
        assertAll(null);
    }

    public void assertAll(String message) {
        if (!m_errors.isEmpty()) {
            StringBuilder sb = new StringBuilder(null == message ? DEFAULT_SOFT_ASSERT_MESSAGE : message);
            boolean first = true;
            for (AssertionError error : m_errors.keySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append("\n\t");
                sb.append(getErrorDetails(error));
            }
            throw new AssertionError(sb.toString());
        }
    }

    //<editor-fold desc="Custom asserts">
    public void assertMatches(String actual, String expected, String message) {
        String errorMessage = message + "\nExpected: " + expected + "\nActual: " + actual;
        if (!actual.matches(expected)) {
            fail(errorMessage);
        }
    }
    //</editor-fold>

}
