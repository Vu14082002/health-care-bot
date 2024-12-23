package vn.edu.iuh.fit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class RagController {

    private final ChatClient aiClient;
    private final VectorStore vectorStore;
    public RagController(ChatClient aiClient, VectorStore vectorStore) {
        this.aiClient = aiClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/bot-chat")
    public ResponseEntity<ObjectResponse> generateAnswer(@RequestParam String query) {
        List<Document> similarDocuments = vectorStore.similaritySearch(query);
        String information = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
        var systemPromptTemplate = new SystemPromptTemplate(
                """
                 Bạn là một trợ lý hữu ích.
                 Bạn sẽ chỉ trả lới những câu trong phạm về nội dung gia liễu và có thể
                 sử dụng thông tin sau để trả lời câu hỏi nểu trong nội dung không có câu trả lời thì trả lời các
                 câu hỏi về nội dung gia liễu.
                 Không sử dụng bất kỳ thông tin nào khác đặc biệt chú ý
                 là không trả lời những câu hỏi ngoài phạm vi gia liễu và nội dung tui cung cấp. Nếu bạn không biết, chỉ cần trả lời: Xin lỗi, câu hỏi nằm ngoài phạm vi hiểu biết của tôi, bản có thể liên hệ qua
                 địa chỉ email suport_health_care@gmail.com để chúng tôi hỗ trợ bạn sớm nhất có thể.
                 {information}
                        """);
        var systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));
        var userPromptTemplate = new PromptTemplate("{query}");
        var userMessage = userPromptTemplate.createMessage(Map.of("query", query));
        var prompt = new Prompt(List.of(systemMessage, userMessage));

        String message = aiClient.call(prompt).getResult().getOutput().getContent();
        String chatId=  "123456789";
        ObjectResponse  objectResponse = new ObjectResponse(message,chatId);
        return ResponseEntity.ok(objectResponse);
    }
}
