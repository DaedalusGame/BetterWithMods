package betterwithmods.items;

/**
 * Created by tyler on 9/10/16.
 */
public interface IBWMItem {
    default String getLocation(int meta) { return "inventory"; }
    default int getMaxMeta() { return 1;}
}
