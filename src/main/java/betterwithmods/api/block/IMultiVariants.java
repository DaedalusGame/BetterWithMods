package betterwithmods.api.block;

/**
 * Used by Items and Blocks with multiple variants to
 *  set which path should be parsed in the blockstate ressource file.
 * 
 */
public interface IMultiVariants {
	/**
	 * Locations to be used as second parameter of {@link #ModelResourceLocation}
	 * Will end up being used in {@link BWMItems#setInventoryModel(Item)}
	 * @return String array with all variants settings.
	 */
    public String[] getVariants();
}
