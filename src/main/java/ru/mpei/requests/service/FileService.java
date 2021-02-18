package ru.mpei.requests.service;

import org.apache.poi.hwpf.HWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.users.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ChatService chatService;

    public String convertTextFileToString(String fileName) {
        try (Stream<String> stream
                     = Files.lines(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))) {

            return stream.collect(Collectors.joining(" "));
        } catch (IOException | URISyntaxException e) {
            return null;
        }
    }

    public HWPFDocument openFile(String name) throws IOException {
        File file = new File(uploadPath + "/" + name);
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        return new HWPFDocument(fis);
    }

    public void replaceText(HWPFDocument doc, User user, PhysicalRequest request) {
        for (HWPFParagraph p : doc.get) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains("от студента гр. ")) {
                        text += user.getPerson().getGroup();
                        r.setText(text, 0);
                    }
                }
            }
        }
    }

    public String saveModifiedFile(XWPFDocument document, boolean isStatement) throws IOException {
        String resultFileName = "";
        if(document != null) {
            String uuidFile = UUID.randomUUID().toString(); //Unique ID to prevent collisions
            resultFileName = uuidFile + "." + "Заявление.doc";
            FileOutputStream out = new FileOutputStream(resultFileName);
            document.write(out);
            out.close();
            document.close();
        }
        return resultFileName;
    }

    public String generateStatement(User user, PhysicalRequest request) throws IOException {
        XWPFDocument doc = openFile("Заявление.doc");
        replaceText(doc, user, request);
        String name = saveModifiedFile(doc, true);
        sendMessageWithFile(user, request);
        return name;
    }

    public void sendMessageWithFile(User user, PhysicalRequest request) throws IOException {
        String resultName = generateStatement(user, request);
        Long messageID = chatService.fillMessage("Файлы сгенерированы:", user, request, request.getChat(), true, false, null);
        Message message = chatService.getMessageById(messageID);
        chatService.addFilesToMessage(message, resultName);
    }
}
