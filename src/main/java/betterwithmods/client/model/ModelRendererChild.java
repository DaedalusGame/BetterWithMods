package betterwithmods.client.model;

import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;

public class ModelRendererChild extends ModelRenderer
{
	private int childPosition;
	private int color = 0;
	private float[] colors = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(color));
	public ModelRendererChild(ModelBase model, int x, int y, int position)
	{
		super(model, x, y);
		this.childPosition = position;
	}
	
	public void renderWithColor(float scale, int color)
	{
		float[] colorIndex = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(color));
		GlStateManager.color(colorIndex[0], colorIndex[1], colorIndex[2]);
		super.render(scale);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void render(float scale)
	{
		super.render(scale);
	}
	
	public void setColor(int color)
	{
		this.color = color;
	}
}
