package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.internal.FTBLibTeamPermissions;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdRemAlly extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "rem_ally";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Universe.INSTANCE.teams.keySet());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        IForgePlayer p = getForgePlayer(ep);
        IForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.TEAM_NO_TEAM.commandError();
        }
        else if(!team.hasPermission(p.getProfile().getId(), FTBLibTeamPermissions.MANAGE_ALLIES))
        {
            throw FTBLibLang.COMMAND_PERMISSION.commandError();
        }

        checkArgs(args, 1, "<teamID>");
        IForgeTeam team1 = getTeam(args[0]);

        if(team.removeAllyTeam(team1.getName()))
        {
            ep.addChatMessage(new TextComponentString("Removed ally team: " + team1.getName()));
        }
    }
}
