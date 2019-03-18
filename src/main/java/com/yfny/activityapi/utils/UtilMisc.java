package com.yfny.activityapi.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Created  by  jinboYu  on  2019/3/13
 */
public class UtilMisc {

    public static <V, V1 extends V, V2 extends V> Map<String, V> toMap(String flowId, V1 value1, String activityStartTime, V2 value2) {
        return populateMap(new HashMap<String, V>(), flowId, value1, activityStartTime, value2);
    }
    @SuppressWarnings("unchecked")
    private static <K, V> Map<String, V> populateMap(Map<String, V> map, Object... data) {
        for (int i = 0; i < data.length;) {
            map.put((String) data[i++], (V) data[i++]);
        }
        return map;
    }
}
