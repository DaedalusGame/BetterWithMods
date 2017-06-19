package li.cil.manual.client.manual.segment;

import betterwithmods.module.ModuleLoader;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Created by primetoxinz on 6/19/17.
 */
public class FeatureSegment extends TextSegment {
    public static final Predicate<String> featureEnabled = feature -> ModuleLoader.isFeatureEnabledSimple(feature);

    public FeatureSegment(@Nullable Segment parent, String feature, String enabled, String disabled) {
        super(parent, featureEnabled.test(feature) ? enabled : disabled);
    }
}
