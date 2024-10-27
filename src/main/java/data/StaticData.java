package data;

import data.dataobject.UserProfileDO;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StaticData {
    public static final Locale defaultLocale = Locale.US;

    public static final String placementBackfillExample = "<script src=\"https://securepubads.g.doubleclick.net/tag/js/gpt.js\" crossorigin=\"anonymous\" async></script>\n" +
            "<div id=\"gpt-passback\">\n" +
            "  <script>\n" +
            "    window.googletag = window.googletag || {cmd: []};\n" +
            "    googletag.cmd.push(function() {\n" +
            "        googletag.defineSlot('/6355419/Travel/Europe', [728, 90], 'gpt-passback')\n" +
            "          .addService(googletag.pubads());\n" +
            "        googletag.enableServices();\n" +
            "        googletag.display('gpt-passback');\n" +
            "    });\n" +
            "  </script>\n" +
            "</div>";

    public static final Map<String, String> recaptchaTestKeysMap = new HashMap<>(){{
        put("public_key", "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI");
        put("private_key", "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe");
    }};

    public static final UserProfileDO supportDefaultUser = new UserProfileDO(UserEnum.userRoleEnum.ADM, "Support", "Support", "support@teqblaze.com", "!kELCYitr3IRr4IJ1pE0F56XGFAJvCxrf", "SmartyAds");
    public static class loginData{
        public static final String
                adminPasswordHash = "89fc225f106447120b7045b0c08d84ec",
                publisherEmail = "default.publisher@autotest.sma",
                defaultUserPassword = "1234567",
                defaultUserPasswordHash = "fcea920f7412b5da7be0cf42b8c93759";
    }

    public static final String GMAIL_TEST_EMAIL = "maksym.kozachenko@teqblaze.com";
    public static final String GMAIL_APP_PASSWORD = "vdgk tijb mjiw gynv";

    public static String getEmailAddress(int additionalId) {
        return GMAIL_TEST_EMAIL.replace("@", "+" + additionalId + "@");
    }

}
