import com.alibaba.fastjson.JSONObject;
import org.apache.zeppelin.echarts.command.WebSocketClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ethan Xiao on 2017/3/25.
 */
public class InterpreterTest {

	private static final String ws = "";
	private static final String noteBookId = "";
	private static final String paragraphId = "";

	@Test
	public void runParagraphCode() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject runResult = client.runParagraph(noteBookId, paragraphId, "%sh\necho hello, zeppelin echarts");
		String code = WebSocketClient.ResultUtil.getParagraphCode(runResult);
		Assert.assertNotNull(code);
		Assert.assertEquals(code, "SUCCESS");
		client.close();
	}

	@Test
	public void runParagraphResults() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject runResult = client.runParagraph(noteBookId, paragraphId, "%sh\necho hello, zeppelin echarts");
		String msg = WebSocketClient.ResultUtil.getParagraphAllMessages(runResult);
		Assert.assertNotNull(msg);
		Assert.assertEquals(msg, "hello, zeppelin echarts\n");
		client.close();
	}

	@Test
	public void runParagraphType() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject runResult = client.runParagraph(noteBookId, paragraphId, "%sh\necho hello, zeppelin echarts");
		List<String> types = WebSocketClient.ResultUtil.getParagraphTypes(runResult);
		Assert.assertNotNull(types);
		Assert.assertEquals(types.size(), 1);
		Assert.assertEquals(types.get(0), "TEXT");
		client.close();
	}

	@Test
	public void getParagraphResult() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject paragraphResults = client.getNote(noteBookId);
		String msg = WebSocketClient.ResultUtil.getParagraphAllMessages(paragraphResults, paragraphId);
		Assert.assertNotNull(msg);
		Assert.assertEquals(msg, "hello, zeppelin echarts\n");
		client.close();
	}

	@Test
	public void getParagraphResults() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject paragraphResults = client.getNote(noteBookId);
		List<String> msgs = WebSocketClient.ResultUtil.getParagraphMessages(paragraphResults, paragraphId);
		Assert.assertNotNull(msgs);
		Assert.assertEquals(msgs.size(), 1);
		Assert.assertEquals(msgs.get(0), "hello, zeppelin echarts\n");
		client.close();
	}

	@Test
	public void getParagraphCode() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject paragraphResults = client.getNote(noteBookId);
		String code = WebSocketClient.ResultUtil.getParagraphCode(paragraphResults, paragraphId);
		Assert.assertNotNull(code);
		Assert.assertEquals(code, "SUCCESS");
		client.close();
	}

	@Test
	public void getParagraphTypes() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject paragraphResults = client.getNote(noteBookId);
		List<String> types = WebSocketClient.ResultUtil.getParagraphTypes(paragraphResults, paragraphId);
		Assert.assertNotNull(types);
		Assert.assertEquals(types.size(), 1);
		Assert.assertEquals(types.get(0), "TEXT");
		client.close();
	}

	@Test
	public void getParagraphIndexResults() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject paragraphResults = client.getNote(noteBookId);
		List<String> msgs = WebSocketClient.ResultUtil.getParagraphMessages(paragraphResults, 0);
		Assert.assertNotNull(msgs);
		Assert.assertEquals(msgs.size(), 1);
		Assert.assertEquals(msgs.get(0), "hello, zeppelin echarts\n");
		client.close();
	}

	@Test
	public void getParagraphIndexCode() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject paragraphResults = client.getNote(noteBookId);
		String code = WebSocketClient.ResultUtil.getParagraphCode(paragraphResults, 0);
		Assert.assertNotNull(code);
		Assert.assertEquals(code, "SUCCESS");
		client.close();
	}

	@Test
	public void getParagraphIndexTypes() throws ExecutionException, InterruptedException, IOException {
		WebSocketClient client = new WebSocketClient(ws, 99999, 10);
		JSONObject paragraphResults = client.getNote(noteBookId);
		List<String> types = WebSocketClient.ResultUtil.getParagraphTypes(paragraphResults, 0);
		Assert.assertNotNull(types);
		Assert.assertEquals(types.size(), 1);
		Assert.assertEquals(types.get(0), "TEXT");
		client.close();
	}
}
