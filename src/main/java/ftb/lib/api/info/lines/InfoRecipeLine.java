package ftb.lib.api.info.lines;

import com.google.gson.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.info.InfoPage;
import ftb.lib.mod.client.gui.info.*;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoRecipeLine extends InfoExtendedTextLine
{
	public String type;
	public Map<Integer, ItemStack> items;
	public ItemStack result;
	
	public InfoRecipeLine(InfoPage c)
	{
		super(c, null);
	}
	
	@SideOnly(Side.CLIENT)
	public ButtonInfoTextLine createWidget(GuiInfo gui)
	{ return new ButtonInfoRecipe(gui, this); }
	
	public void func_152753_a(JsonElement e)
	{
		super.func_152753_a(e);
		
		JsonObject o = e.getAsJsonObject();
		
		
	}
	
	public JsonElement getSerializableElement()
	{
		JsonObject o = (JsonObject) super.getSerializableElement();
		
		return o;
	}
}