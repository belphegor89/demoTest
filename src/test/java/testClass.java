import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class testClass{
    public static final Locale defaultLocale = Locale.US;

    public static void main(String[] args){
        testLocale("US");
        //        testLocale("DZ");
        //        testLocale("NE");
        //        testLocale("CF");
    }

    public static void testLocale(String countryISO2){
        //        String name = new Locale(loc.toString()).getDisplayLanguage(Locale.ENGLISH); // English
        //        System.out.println(name);
        Locale country = new Locale(Locale.ENGLISH.getLanguage(), countryISO2);
        System.out.println("1:");
        System.out.println(country.getCountry());
        //        System.out.println(country.getISO3Country());
        System.out.println(country.getDisplayCountry(defaultLocale));
        System.out.println(country.getDisplayCountry());
        System.out.println(country.getVariant());
        System.out.println(country.getDisplayVariant());
        System.out.println(country.getDisplayVariant(Locale.US));
        //        System.out.println(country.getDisplayName());

        //        Locale country2 = new Locale("uk", countryISO2);
        //        System.out.println("2:");
        //        System.out.println(country2.getCountry());
        //        System.out.println(country2.getISO3Country());
        //        System.out.println(country2.getDisplayCountry(defaultLocale));
    }

    public static void formatNumbersInt(int number){
        System.out.println("Clear int:\n" + number);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        System.out.println("Formatted int:\n" + formatter.format(number));

        DecimalFormat priceFormat1 = new DecimalFormat("##.00", new DecimalFormatSymbols(Locale.US));
        System.out.println("Cusstom format 1:\n" + priceFormat1.format(number));
        DecimalFormat priceFormat2 = new DecimalFormat("###,###.00", new DecimalFormatSymbols(Locale.US));
        System.out.println("Cusstom format 2:\n" + priceFormat2.format(number));
        DecimalFormat bigFormat = new DecimalFormat("###,###.##", new DecimalFormatSymbols(Locale.US));
        System.out.println("Cusstom format 3:\n" + bigFormat.format(number));
    }

    public static void formatNumbersDouble(double number){
        System.out.println("Clear double:\n" + number);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        System.out.println("Formatted double:\n" + formatter.format(number));

        DecimalFormat priceFormat1 = new DecimalFormat("##.00", new DecimalFormatSymbols(Locale.US));
        System.out.println("Cusstom format 1:\n" + priceFormat1.format(number));
        DecimalFormat priceFormat2 = new DecimalFormat("###,###.00", new DecimalFormatSymbols(Locale.US));
        System.out.println("Cusstom format 2:\n" + priceFormat2.format(number));
        DecimalFormat bigFormat = new DecimalFormat("###,###.##", new DecimalFormatSymbols(Locale.US));
        System.out.println("Cusstom format 3:\n" + bigFormat.format(number));
        DecimalFormat priceFormat3 = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Locale.US));
        System.out.println("Cusstom format 4:\n" + priceFormat3.format(number));
        DecimalFormat priceFormat5 = new DecimalFormat("###,##0.##", new DecimalFormatSymbols(Locale.US));
        System.out.println("Cusstom format 5:\n" + priceFormat3.format(number));
    }

}
