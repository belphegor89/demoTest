package common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class RandomUtility {
    private final ParseAndFormatUtility FormatUtil = new ParseAndFormatUtility();

    public Object getRandomElement(List list) {
        if (!list.isEmpty()) {
            return list.get(new Random().nextInt(list.size()));
        } else {
            return null;
        }
    }

    public Object getRandomElement(Stream listStream) {
        List list = listStream.toList();
        if (!list.isEmpty()) {
            return list.get(new Random().nextInt(list.size()));
        } else {
            return null;
        }
    }

    public Object getRandomElement(Object[] array) {
        if (array.length > 0) {
            return array[new Random().nextInt(array.length)];
        } else {
            return null;
        }
    }

    public <T extends Enum<?>> T getRandomEnumValue(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        int r = getRandomInt(0, values.length);
        return values[r];
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<?>> T getRandomEnumValue(Class<T> enumClass, Object... exclude) {
        T[] values = enumClass.getEnumConstants();
        List<Object> exList = Arrays.stream(exclude).toList();
        List<T> res = Arrays.stream(values).filter(f -> !exList.contains(f)).toList();
        return (T) getRandomElement(res);
    }

    public List getRandomSublist(List list, int sublistSize) {
        if (!list.isEmpty()) {
            int start = getRandomInt(0, list.size() - sublistSize);
            return list.subList(start, start + sublistSize);
        } else {
            return null;
        }
    }

    public int getRandomInt(int min, int max) {
        return new Random().ints(min, max).findFirst().getAsInt();
    }

    public double getRandomDouble(int min, int max) {
        return new Random().doubles(min, max).findFirst().getAsDouble();
    }

    public double getRandomDoubleRounded(int min, int max, int precision) {
        double randomDouble = new Random().doubles(min, max).findFirst().getAsDouble();
        return FormatUtil.roundDouble(randomDouble, precision);
    }

    public boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }

    public String getRandomString(int length) {
        String salt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(salt.charAt(getRandomInt(0, salt.length())));
        }
        return str.toString();
    }

}
