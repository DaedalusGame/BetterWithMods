package betterwithmods.integration;

import java.util.HashMap;

/**
 * Created by tyler on 5/13/17.
 */
public class CompatMap extends HashMap<String, String> {

    public CompatMap(String... s) {
        for(int i = 0; i < s.length / 2; i++)
            put(s[i * 2], s[i * 2 + 1]);
    }

    @Override
    public String put(String key, String value) {
        return super.put( key , value);
    }

}
