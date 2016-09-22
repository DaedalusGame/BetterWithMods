package betterwithmods.api;

/**
 * Used by Items and Blocks with a model location relative to their metadata.
 * 
 * @author tyler
 */
public interface IMultiLocations {
	/**
	 * Locations to be used as first parameter of {@link ModelResourceLocation}
	 * @return String array with all locations, WITHOUT modid.
	 */
    public String[] getLocations();
}
