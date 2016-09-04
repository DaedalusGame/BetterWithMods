package betterwithmods.damagesource;

import net.minecraft.util.DamageSource;

public class BWDamageSource extends DamageSource
{
	public static BWDamageSource saw = new BWDamageSource("saw", false);
	public static BWDamageSource choppingBlock = new BWDamageSource("choppingBlock", false);
	
	protected BWDamageSource(String name, boolean ignoreArmor)
	{
		super(name);
		if(ignoreArmor)
			setDamageBypassesArmor();
	}
}
