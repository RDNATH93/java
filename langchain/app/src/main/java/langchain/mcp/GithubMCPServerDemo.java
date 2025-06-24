package langchain.mcp;

import java.util.List;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;

interface Bot {

    String chat(String prompt);
}

public class GithubMCPServerDemo {
    public static void main(String[] args) throws Exception {

        ChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();

        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("/usr/bin/docker", "run", "-e", "GIT_HUB_MCP_SERVER_TOKEN", "-i", "mcp/github"))
                .logEvents(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        Bot bot = AiServices.builder(Bot.class)
                .chatModel(model)
                .toolProvider(toolProvider)
                .build();

        try {
            String response = bot.chat("Summarize the last 3 commits of the LangChain4j GitHub repository");
            System.out.println("RESPONSE: " + response);
        } finally {
            mcpClient.close();
        }
    }
}
