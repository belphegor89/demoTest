package common.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import data.StaticData;
import org.apache.commons.text.StringEscapeUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseAndFormatUtility {
    private static final Locale locale = StaticData.defaultLocale;

    //<editor-fold desc="Parse">

    public int parseInteger(String line) {
        return Integer.parseInt(line.replaceAll("\\D+", ""));
    }

    public double parseDouble(String text) {
        return Double.parseDouble(text.replaceAll("[^\\d+\\.\\,]", ""));
    }

    public double roundDouble(double input, int precision) {
        double result, tmp, factor = Math.pow(10, precision);
        result = input * factor;
        tmp = Math.round(result);
        result = tmp / factor;
        return result;
    }

    public String parseByRegex(String input, String query, int matcherGroup) {
        String result = null;
        Pattern pattern = Pattern.compile(query);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            result = matcher.group(matcherGroup);
        }
        return result;
    }

    public String parseByJSONPath(Object jsonObject, String jsonPathQuery) {
        String output = null;
        DocumentContext jsonContext;
        try {
            jsonContext = JsonPath.parse(jsonObject);
            output = jsonContext.read(jsonPathQuery).toString();
        } catch (PathNotFoundException | NullPointerException nullEx) {
            nullEx.printStackTrace();
        }
        return output;
    }


    //</editor-fold>

    //<editor-fold desc="Format">

    /**
     * Formats a price with zeros after the decimal point: 123.45
     */
    public String formatPriceWithZeros(double number) {
        return formatDouble(number, "#0.00");
    }

    /**
     * Formats a price with no zeros after the decimal point: 123.45
     */
    public String formatPriceNoZeros(double number) {
        return formatDouble(number, "#0.##");
    }

    /**
     * Formats a number with commas as a separator for thousands and no decimal part: 123,456
     */
    public String formatBigNoDecimal(double number) {
        return formatDouble(number, "###,###");
    }

    /**
     * Formats a number with commas as a separator for thousands and a dot as a separator for decimal part: 123,456.78
     *
     * @param number plain double number
     * @return formatted string
     */
    public String formatBigCommaWithDecimal(double number) {
        return formatDoubleBigNumber(number, ',');
    }

    /**
     * Formats a number with spaces as a separator for thousands and a dot as a separator for decimal part: 123 456.78
     *
     * @param number plain double number
     * @return formatted string
     */
    public String formatBigSpaceWithDecimal(double number) {
        return formatDoubleBigNumber(number, ' ');
    }

    /**
     * Formats a number with spaces as a separator for thousands and a dot as a separator for decimal part: 123 456.78
     *
     * @param number plain double number
     * @return formatted string
     */
    public String formatDoubleBigNumber(double number, char sectionSeparator) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(sectionSeparator);
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00", symbols);
        return formatDouble(number, decimalFormat);
    }

    /**
     * Formats a number with a custom pattern
     *
     * @param number        plain double number
     * @param formatPattern custom pattern
     * @return formatted string
     */
    public String formatDouble(double number, String formatPattern) {
        return new DecimalFormat(formatPattern, new DecimalFormatSymbols(locale)).format(number);
    }

    public String formatDouble(double number, DecimalFormat format) {
        return format.format(number);
    }

    public String formatHtmlUnescapeCharacters(String rawJsonText) {
        return StringEscapeUtils.unescapeHtml4(rawJsonText);
    }

    public String formatJsonUnescapeCharacters(String input) {
        return StringEscapeUtils.unescapeJson(input);
    }

    public String formatEscapeRegex(String input) {
        String[] SPECIAL_CHARS_SHORT = {"/", "?", "*", "^", "$", "[", "]", "(", ")", "{", "}", "|"};
        for (String specialChar : SPECIAL_CHARS_SHORT) {
            input = input.replace(specialChar, "\\" + specialChar);
        }
        return input;
    }
    //</editor-fold>
}
