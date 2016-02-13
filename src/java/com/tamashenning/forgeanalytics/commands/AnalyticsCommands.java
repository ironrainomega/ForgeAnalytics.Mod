package com.tamashenning.forgeanalytics.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tamashenning.forgeanalytics.AnalyticsClient;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class AnalyticsCommands implements ICommand {

	private List<String> aliases;
	public AnalyticsCommands() {
		this.aliases = new ArrayList<String>();
		this.aliases.add("forgeanalytics");
		this.aliases.add("fa");
	}
	
	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "forgeanalytics";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/forgeanalytics send";
	}

	@Override
	public List<String> getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		/*if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)sender;
			AnalyticsClient ac = new AnalyticsClient();
			String message = ac.CreateClientStartupPing();
			player.addChatMessage(new ChatComponentText(message));
		}*/
		
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		//List<String> list = Arrays.asList(MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames());
		//return list.contains(sender.getName());
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
