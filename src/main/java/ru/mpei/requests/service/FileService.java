package ru.mpei.requests.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.morpher.ws3.AccessDeniedException;
import ru.morpher.ws3.ArgumentEmptyException;
import ru.morpher.ws3.Client;
import ru.morpher.ws3.ClientBuilder;
import ru.morpher.ws3.russian.ArgumentNotRussianException;
import ru.morpher.ws3.russian.DeclensionResult;
import ru.morpher.ws3.russian.InvalidFlagsException;
import ru.morpher.ws3.russian.NumeralsDeclensionNotSupportedException;
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.users.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.GregorianCalendar;
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

    public String getUnderlines(int size) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < size; i++) {
            s.append("_");
        }
        return  s.toString();
    }

    public XWPFDocument openFile(String name) throws IOException {
        File file = new File(uploadPath + "/" + name);
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        return new XWPFDocument(fis);
    }

    public void replaceText(XWPFDocument doc, User user, Request request) throws InvalidFlagsException, IOException, ArgumentNotRussianException, AccessDeniedException, ArgumentEmptyException, NumeralsDeclensionNotSupportedException {
        //Paragraphs
        //2: Институт
        //3.1: ФИО директора
        //5.1: от студента гр.
        //7: фамилия
        //8: имя
        //9: отчество
        //14: Заявление
        //16: Дисциплина
        //20.1: Почта
        //26.3: Дата подачи заявления
        XWPFParagraph paragraph;
        XWPFRun run;
        Client client = new ClientBuilder().build();
        DeclensionResult result;
        String genitiveCase;

        paragraph = doc.getParagraphs().get(2); //Institute
        run = paragraph.getRuns().get(0);
        run.setText(ServiceUtils.getInstitute(user) +  getUnderlines(run.getText(0).length() - user.getPerson().getGroup().length() - 1), 0);

        paragraph = doc.getParagraphs().get(3); //Director
        run = paragraph.getRuns().get(1);
        run.setText(ServiceUtils.getDirector(user) +  getUnderlines(run.getText(0).length() - user.getPerson().getGroup().length() - 4), 0);

        paragraph = doc.getParagraphs().get(5); //Group
        run = paragraph.getRuns().get(1);
        run.setText(user.getPerson().getGroup() +  getUnderlines(run.getText(0).length() - user.getPerson().getGroup().length()), 0);

        paragraph = doc.getParagraphs().get(7); //Last name
        run = paragraph.getRuns().get(0);
        result = client.russian().declension(request.getClient().getPerson().getLastName());
        genitiveCase = result.genitive;
        run.setText(genitiveCase + getUnderlines(run.getText(0).length() - genitiveCase.length()), 0);

        paragraph = doc.getParagraphs().get(8); //Fist name
        run = paragraph.getRuns().get(0);
        result = client.russian().declension(request.getClient().getPerson().getFirstName());
        genitiveCase = result.genitive;
        run.setText(genitiveCase + getUnderlines(24 - genitiveCase.length()), 0);

        paragraph = doc.getParagraphs().get(9); //Second name
        run = paragraph.getRuns().get(0);
        result = client.russian().declension(request.getClient().getPerson().getSecondName());
        genitiveCase = result.genitive;
        run.setText(genitiveCase + getUnderlines(24 - genitiveCase.length()), 0);

        paragraph = doc.getParagraphs().get(16); //Theme
        run = paragraph.getRuns().get(0);
        result = client.russian().declension(request.getTheme());
        genitiveCase = result.genitive;
        int length = (77 - genitiveCase.length()) / 2;
        run.setText(getUnderlines(length) + genitiveCase + getUnderlines(length), 0);

        paragraph = doc.getParagraphs().get(20); //E-mail
        run = paragraph.getRuns().get(1);
        run.setText(user.getUsername() + getUnderlines(45 - user.getUsername().length()), 0);

        paragraph = doc.getParagraphs().get(26); //Date
        run = paragraph.getRuns().get(3);
        genitiveCase = ServiceUtils.getCalendarAsStringForFiles(new GregorianCalendar());
        run.setText(genitiveCase + getUnderlines(14 - genitiveCase.length()), 0);
    }

    public String saveModifiedFile(XWPFDocument document, boolean isStatement) throws IOException {
        String resultFileName = "";
        if(document != null) {
            String uuidFile = UUID.randomUUID().toString(); //Unique ID to prevent collisions
            resultFileName = uuidFile + "." + "Заявление.docx";
            FileOutputStream out = new FileOutputStream(new File(uploadPath + File.separator + "files" + File.separator + resultFileName));
            document.write(out);
            out.close();
        }
        return resultFileName;
    }

    public String generateStatement(User user, PhysicalRequest request) throws IOException, InvalidFlagsException, AccessDeniedException, NumeralsDeclensionNotSupportedException, ArgumentNotRussianException, ArgumentEmptyException {
        XWPFDocument doc = openFile("Zayavlenie.docx");
        replaceText(doc, user, request);
        return saveModifiedFile(doc, true);
    }

    public void sendMessageWithFile(User user, PhysicalRequest request) throws IOException, InvalidFlagsException, AccessDeniedException, NumeralsDeclensionNotSupportedException, ArgumentNotRussianException, ArgumentEmptyException {
        String resultName = generateStatement(user, request);
        Long messageID = chatService.fillMessage("Файлы сгенерированы:", user, request, request.getChat(), true, false, null);
        Message message = chatService.getMessageById(messageID);
        chatService.addFilesToMessage(message, resultName);
    }
}
