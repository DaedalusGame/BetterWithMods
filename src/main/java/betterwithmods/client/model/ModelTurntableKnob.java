package betterwithmods.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTurntableKnob extends ModelBase
{
    public ModelRenderer[] knobs = new ModelRenderer[4];

    public ModelTurntableKnob(int position)
    {
        float off = -4 + (position * 2);
        knobs[0] = new ModelRenderer(this, 0, 0);
        knobs[1] = new ModelRenderer(this, 0, 0);
        knobs[2] = new ModelRenderer(this, 0, 0);
        knobs[3] = new ModelRenderer(this, 0, 0);
        for(int i = 0; i < 4; i++)
            knobs[i].setTextureSize(16, 16);
        knobs[0].addBox(off, -3F, 8F, 2, 2, 1);
        knobs[1].addBox(-2F + -off, -3F, -9F, 2, 2, 1);
        knobs[2].addBox(-9F, -3F, off, 1, 2, 2);
        knobs[3].addBox(8F, -3F, -2F - off, 1, 2, 2);
    }

    public void render(float scale)
    {
        for(int i = 0; i < 4; i++)
            knobs[i].render(scale);
    }
}
