package betterwithmods.common.damagesource;

import net.minecraft.util.DamageSource;

public class BWDamageSource extends DamageSource {
    public static final BWDamageSource saw = new BWDamageSource("saw", false);
    public static final BWDamageSource choppingBlock = new BWDamageSource("chopping_block", false);
    public static final BWDamageSource gloom = new BWDamageSource("gloom",true);
    public static final BWDamageSource growth = new BWDamageSource("growth",false);
    public static final BWDamageSource acidRain = new BWDamageSource("acid_rain", true);

    protected BWDamageSource(String name, boolean ignoreArmor) {
        super(name);
        if (ignoreArmor)
            setDamageBypassesArmor();
    }

}
