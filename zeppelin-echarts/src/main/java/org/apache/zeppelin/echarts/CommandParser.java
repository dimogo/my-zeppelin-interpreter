package org.apache.zeppelin.echarts;

import org.apache.zeppelin.echarts.command.Command;
import org.apache.zeppelin.echarts.command.CommandBuilder;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class CommandParser {
	private static class CommandParserHolder {
		private static CommandParser parser = new CommandParser();
	}

	public static CommandParser getInstance() {
		return CommandParserHolder.parser;
	}

	private static final char COMMAND_LINE_SEPARATOR_HEAD = '%';
	private static final String COMMAND_LINE_SEPARATOR_FOOT = "\n";

	/**
	 * 解析Zeppelin传入的所有执行内容
	 * @param cmd 传入的所有执行内容
	 * @return Command 命令执行集合
	 * @throws Exception
	 */
	public Command parse(String cmd) throws Exception {
		int commandIndex = 0;
		int commandLength = 0;
		int bodyIndex = 0;
		int bodyLength = 0;
		int bodyOffset = 0;
		char bodyLastChart;
		String commandLine = null;
		String commandBody = null;
		Command command = new Command();
		while (cmd.indexOf(COMMAND_LINE_SEPARATOR_HEAD, commandIndex) == commandIndex) {
			commandBody = null;
			bodyOffset = 0;
			commandIndex = cmd.indexOf(COMMAND_LINE_SEPARATOR_HEAD, commandIndex);
			commandLength = cmd.indexOf(COMMAND_LINE_SEPARATOR_FOOT, commandIndex) - commandIndex;
			if (commandLength < 0) {
				commandLength = cmd.length() - commandIndex;
			}
			commandLine = cmd.substring(commandIndex, commandIndex + commandLength).replaceAll("\\r", "");
			commandIndex += commandLength + 1;
			if (commandIndex >= cmd.length() || cmd.charAt(commandIndex) == COMMAND_LINE_SEPARATOR_HEAD) {
				CommandBuilder.getInstance().build(command, commandLine, commandBody);
				continue;
			}
			bodyIndex = commandIndex;
			bodyLength = 0;
			while (cmd.indexOf(COMMAND_LINE_SEPARATOR_FOOT, bodyIndex + bodyLength) > -1) {
				bodyLength = cmd.indexOf(COMMAND_LINE_SEPARATOR_FOOT, bodyIndex + bodyLength) + 1;
				if (bodyLength >= cmd.length() || cmd.charAt(bodyLength) == COMMAND_LINE_SEPARATOR_HEAD) {
					commandIndex = bodyLength;
					break;
				}
			}
			bodyLength -= bodyIndex;
			while (bodyLength > 0) {
				bodyLastChart = cmd.charAt(bodyIndex + bodyLength - bodyOffset - 1);
				if (bodyLastChart == '\r' || bodyLastChart == '\n') {
					bodyOffset++;
					continue;
				}
				break;
			}
			commandBody = bodyLength - bodyOffset <= 0 ? "" : cmd.substring(bodyIndex, bodyIndex + bodyLength - bodyOffset);
			CommandBuilder.getInstance().build(command, commandLine, commandBody);
		}
		return command;
	}

}
