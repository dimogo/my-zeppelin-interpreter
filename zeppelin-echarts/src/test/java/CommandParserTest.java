import org.apache.zeppelin.echarts.CommandParser;
import org.apache.zeppelin.echarts.command.Command;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ethan Xiao on 2017/1/12.
 */
public class CommandParserTest {
	@Test
	public void inout() throws Exception {
		String cmd = "%in \t\t html\r\n\r\n"
				+ "<html><head></head>\r\n"
				+ "<body></body>\r\n"
				+ "%out html\r\n"
				+ "</html>\r\n\r\n";
		Command command = CommandParser.getInstance().parse(cmd);
		Assert.assertNotNull(command);
		InterpreterResult result = command.execute(null, null);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.code() == InterpreterResult.Code.SUCCESS);
		Assert.assertTrue(result.message().length() == 1);
		String text = result.message();
		Assert.assertEquals(text,
				"\r\n<html><head></head>\r\n"
				+ "<body></body>"
				+ "</html>");
	}

	@Test
	public void echarts() throws Exception {
		String cmd = "%in html\r\n"
				+ "%proc echarts\r\n"
				+ "%para option title.text\r\n"
				+ "\"test title\"\r\n"
				+ "%para option title.subtext\r\n"
				+ "\"test sub title\"\r\n"
				+ "%out html";
		Command command = CommandParser.getInstance().parse(cmd);
		Assert.assertNotNull(command);
	}

	@Test
	public void httpPost() throws Exception {
		String cmd = "%in http post http://www.google.com\r\n"
				+ "%para header Content-Type application/json\r\n"
				+ "%para raw\r\n"
				+ "{\"starts\":\"2017-01-01\",\n" +
				"\"ends\":\"2017-12-31\"}"
				+ "%para conf SocketTimeout 30000\r\n"
				+ "%para conf ConnectTimeout 30000\r\n"
				+ "%para conf ConnectionRequestTimeout 30000\r\n"
				+ "%out html";
		Command command = CommandParser.getInstance().parse(cmd);
		Assert.assertNotNull(command);
	}
}
