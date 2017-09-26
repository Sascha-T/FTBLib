package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class SidebarButton extends FinalIDObject implements ISidebarButton
{
	private Icon icon = Icon.EMPTY;
	private Boolean defaultConfig = null;
	private boolean configValue = true;
	public final Map<String, Boolean> dependencies = new HashMap<>();
	private final List<String> requiredServerMods = new ArrayList<>();
	private final List<ClickEvent> clickEvents = new ArrayList<>();
	private final List<ClickEvent> shiftClickEvents = new ArrayList<>();
	public boolean requiresOp, devOnly, hideWithNEI, loadingScreen, customText;

	public SidebarButton(ResourceLocation id)
	{
		super(id.toString().replace(':', '.'));
	}

	public SidebarButton(ResourceLocation id, JsonObject o)
	{
		this(id);

		if (o.has("icon"))
		{
			icon = Icon.getIcon(o.get("icon"));
		}

		if (icon.isEmpty())
		{
			icon = GuiIcons.ACCEPT_GRAY;
		}
		if (o.has("dependencies"))
		{
			setDependencies(o.get("dependencies").getAsString());
		}
		if (o.has("click"))
		{
			for (JsonElement e : JsonUtils.toArray(o.get("click")))
			{
				clickEvents.add(JsonUtils.deserializeClickEvent(e));
			}
		}
		if (o.has("shift_click"))
		{
			for (JsonElement e : JsonUtils.toArray(o.get("shift_click")))
			{
				shiftClickEvents.add(JsonUtils.deserializeClickEvent(e));
			}
		}
		if (o.has("config"))
		{
			defaultConfig = configValue = o.get("config").getAsBoolean();
		}

		requiresOp = o.has("requires_op") && o.get("requires_op").getAsBoolean();
		devOnly = o.has("dev_only") && o.get("dev_only").getAsBoolean();
		hideWithNEI = o.has("hide_with_nei") && o.get("hide_with_nei").getAsBoolean();
		loadingScreen = o.has("loading_screen") && o.get("loading_screen").getAsBoolean();
		customText = o.has("custom_text") && o.get("custom_text").getAsBoolean();
	}

	@Override
	public Icon getIcon()
	{
		return icon;
	}

	@Override
	@Nullable
	public Boolean getDefaultConfig()
	{
		return defaultConfig;
	}

	public void setDependencies(String deps)
	{
		dependencies.clear();

		if (!deps.isEmpty())
		{
			for (String s : deps.split(";"))
			{
				int index = s.indexOf(':');

				if (index != -1)
				{
					switch (s.substring(0, index))
					{
						case "before":
							dependencies.put(s.substring(index + 1, s.length()), true);
							break;
						case "after":
							dependencies.put(s.substring(index + 1, s.length()), false);
							break;
					}
				}
			}
		}
	}

	@Override
	public void onClicked(boolean shift)
	{
		if (loadingScreen)
		{
			new GuiLoading().openGui();
		}

		for (ClickEvent event : (shift ? shiftClickEvents : clickEvents))
		{
			GuiHelper.onClickEvent(event);
		}
	}

	@Override
	public boolean isVisible()
	{
		return configValue && FTBLibClientConfig.general.action_buttons != EnumSidebarButtonPlacement.DISABLED && isAvailable();
	}

	@Override
	public boolean isAvailable()
	{
		return !(hideWithNEI && CommonUtils.isNEILoaded()) && !(requiresOp && !FTBLibAPI.API.getClientData().isClientOP()) && !(!requiredServerMods.isEmpty() && FTBLibAPI.API.getClientData().optionalServerMods().containsAll(requiredServerMods));
	}

	@Override
	public boolean hasCustomText()
	{
		return customText;
	}

	@Override
	public boolean getConfig()
	{
		return configValue;
	}

	@Override
	public void setConfig(boolean value)
	{
		configValue = value;
	}

	@Override
	public Map<String, Boolean> getDependencies()
	{
		return dependencies;
	}
}