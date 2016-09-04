package betterwithmods.client.entity;

import betterwithmods.BWRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class DynamiteRenderFactory implements IRenderFactory
{

	@Override
	public Render createRenderFor(RenderManager manager) 
	{
		return new RenderSnowball(manager, BWRegistry.dynamite, Minecraft.getMinecraft().getRenderItem());
	}

}
