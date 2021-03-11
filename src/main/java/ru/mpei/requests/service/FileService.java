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
import ru.mpei.requests.domain.chats.MessageFile;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.MessageFileRepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {
    @Autowired
    private MessageFileRepo messageFileRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MailSender mailSender;

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

    public void replaceTextStatement(XWPFDocument doc, User user, Request request) throws InvalidFlagsException, IOException, ArgumentNotRussianException, AccessDeniedException, ArgumentEmptyException, NumeralsDeclensionNotSupportedException {
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
        Client client = new ClientBuilder().useToken("c497c341-6c6d-408b-b900-a97f7fc85499").build();
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
        result = client.russian().declension(request.getClient().getPerson().getLastName()); //If it stopped working here, go to https://morpher.ru/My/Account.aspx and buy a subscription
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
        if (isStatement) {
            if (document != null) {
                String uuidFile = UUID.randomUUID().toString(); //Unique ID to prevent collisions
                resultFileName = uuidFile + "." + "Заявление.docx";
                FileOutputStream out = new FileOutputStream(new File(uploadPath + File.separator + "files" + File.separator + resultFileName));
                document.write(out);
                out.close();
            }
        } else {
            if (document != null) {
                String uuidFile = UUID.randomUUID().toString(); //Unique ID to prevent collisions
                resultFileName = uuidFile + "." + "Анкета.docx";
                FileOutputStream out = new FileOutputStream(new File(uploadPath + File.separator + "files" + File.separator + resultFileName));
                document.write(out);
                out.close();
            }
        }
        return resultFileName;
    }

    public String generateFile(User user, PhysicalRequest request, boolean isStatement) throws IOException, InvalidFlagsException, AccessDeniedException, NumeralsDeclensionNotSupportedException, ArgumentNotRussianException, ArgumentEmptyException {
        if (isStatement) {
            XWPFDocument doc = openFile("Zayavlenie.docx");
            replaceTextStatement(doc, user, request);
            return saveModifiedFile(doc, true);
        }
        XWPFDocument doc = openFile("Anketa.docx");
        replaceTextWorksheet(doc, user, request);
        return saveModifiedFile(doc, false);
    }

    public void replaceTextWorksheet(XWPFDocument doc, User user, Request request) throws InvalidFlagsException, IOException, ArgumentNotRussianException, AccessDeniedException, ArgumentEmptyException, NumeralsDeclensionNotSupportedException {
        //4.3: Discipline
        //5.3: Last name nice
        //6.4: First name nice
        //7.3: Second name nice
        //8.3: phone nice
        //8.8: E-mail nice
        //9.3: DOB
        //9.5: Sex
        //9.7: Citizenship
        //10.7: passport series nice
        //10.11: passport number nice
        //10.0: passport giving organ nice
        //11.0: passport date nice
        //12.0: registration adress nice
        //13.5: index nice
        //---------------------------------------------
        //15.6: среднее профессиональное (14 пробелов)
        //15.12: основное общее (школа) (5 пробелов)
        //16.3: бакалавр (43 пробела)
        //16.8: специалист (1 пробел)
        //16.11: магистр (7 пробелов)
        //19.4: Уволенный с военной службы (12 пробелов)
        //19.6: Направленный служебной занятости (4 пробела)
        //---------------------------------------------
        //21.2: Специальность студента
        //22.5: Группа студента
        //31.2: Дата заполнения

        XWPFParagraph paragraph;
        XWPFRun run;
        Client client = new ClientBuilder().build();
        DeclensionResult result;
        String genitiveCase;

        paragraph = doc.getParagraphs().get(4); //Discipline
        run = paragraph.getRuns().get(3);
        run.setText(request.getTheme() +  getUnderlines(run.getText(0).length() - request.getTheme().length()), 0);

        paragraph = doc.getParagraphs().get(5); //Last name
        run = paragraph.getRuns().get(3);
        run.setText(user.getPerson().getLastName() + getUnderlines(run.getText(0).length() - user.getPerson().getLastName().length()), 0);

        paragraph = doc.getParagraphs().get(6); //First name
        run = paragraph.getRuns().get(4);
        run.setText(user.getPerson().getFirstName() + getUnderlines(run.getText(0).length() - user.getPerson().getFirstName().length()), 0);

        if (user.getPerson().getSecondName() != null && !user.getPerson().getSecondName().isEmpty()) {
            paragraph = doc.getParagraphs().get(7); //Second name
            run = paragraph.getRuns().get(3);
            run.setText(user.getPerson().getSecondName() + getUnderlines(run.getText(0).length() - user.getPerson().getLastName().length() - 7), 0);
        }

        paragraph = doc.getParagraphs().get(5); //Last name
        run = paragraph.getRuns().get(3);
        run.setText(user.getPerson().getLastName() + getUnderlines(run.getText(0).length() - user.getPerson().getLastName().length()), 0);

        paragraph = doc.getParagraphs().get(8); //Phone
        run = paragraph.getRuns().get(3);
        run.setText(user.getPerson().getPhoneNumber() + getUnderlines(run.getText(0).length() - user.getPerson().getPhoneNumber().length()), 0);

        paragraph = doc.getParagraphs().get(8); //E-mail
        run = paragraph.getRuns().get(8);
        run.setText(user.getUsername() + getUnderlines(run.getText(0).length() - user.getUsername().length()), 0);

        paragraph = doc.getParagraphs().get(9); //DOB
        run = paragraph.getRuns().get(3);
        run.setText(ServiceUtils.getCalendarAsStringForFiles(user.getPerson().getDOB()) + getUnderlines(20), 0);
        run.setFontSize(10);

        paragraph = doc.getParagraphs().get(9); //Sex
        run = paragraph.getRuns().get(5);
        if (user.getPerson().getSex()) {
            run.setText("пол: " + "М", 0); //Citizenship + 4
        } else {
            run.setText("пол: " + "Ж", 0); //Citizenship + 4
        }

        paragraph = doc.getParagraphs().get(9); //Citizenship
        run = paragraph.getRuns().get(7);
        if (user.getPerson().getCitizenship().toLowerCase().equals("Российская Федерация".toLowerCase())) {
            run.setText("Гражданство_РФ" + getUnderlines(16), 0);
        } else {
            run.setText("Гражданство_" + user.getPerson().getCitizenship() + getUnderlines(run.getText(0).length() - user.getPerson().getCitizenship().length() + 4 - 1));
        }

        paragraph = doc.getParagraphs().get(10); //Passport series
        run = paragraph.getRuns().get(7);
        run.setText(user.getPerson().getPassport().substring(0, 4) + getUnderlines(run.getText(0).length() - user.getPerson().getPassport().substring(0, 4).length()), 0);

        paragraph = doc.getParagraphs().get(10); //Passport number
        run = paragraph.getRuns().get(11);
        run.setText(user.getPerson().getPassport().substring(4) + getUnderlines(run.getText(0).length() - user.getPerson().getPassport().substring(4).length()), 0);

        paragraph = doc.getParagraphs().get(11); //Passport giving organ
        run = paragraph.getRuns().get(0);
        run.setText(user.getPerson().getPassportGivingOrgan() + getUnderlines(50 - user.getPerson().getPassportGivingOrgan().length()) + ", дата выдачи " + ServiceUtils.getCalendarAsStringForFiles(user.getPerson().getPassportDate()) + getUnderlines(11), 0);

//        paragraph = doc.getParagraphs().get(11); //Passport date
//        run = paragraph.getRuns().get(0);
//        run.setText(ServiceUtils.getCalendarAsStringForFiles(user.getPerson().getPassportDate()) + getUnderlines(run.getText(0).length() - ServiceUtils.getCalendarAsStringForFiles(user.getPerson().getPassportDate()).length()), 0);

        paragraph = doc.getParagraphs().get(12); //Registration adress
        run = paragraph.getRuns().get(0);
        run.setText("зарегистрирован по адресу: " + user.getPerson().getRegistrationAdress() + getUnderlines(run.getText(0).length() - user.getPerson().getRegistrationAdress().length() - 27), 0);

        paragraph = doc.getParagraphs().get(13); //Index
        run = paragraph.getRuns().get(5);
        run.setText("ндекс: " + user.getPerson().getIndex() + getUnderlines(run.getText(0).length() - user.getPerson().getIndex().length() - 7), 0);

        paragraph = doc.getParagraphs().get(21); //Speciality
        run = paragraph.getRuns().get(2);
        run.setText("_" + user.getPerson().getSpeciality() + getUnderlines(run.getText(0).length() - user.getPerson().getSpeciality().length() - 1), 0);

        paragraph = doc.getParagraphs().get(22); //Group
        run = paragraph.getRuns().get(5);
        run.setText("_" + user.getPerson().getGroup() + getUnderlines(run.getText(0).length() - user.getPerson().getGroup().length() - 1), 0);

        paragraph = doc.getParagraphs().get(31); //Last name
        run = paragraph.getRuns().get(2);
        run.setText(ServiceUtils.getCalendarAsStringForFiles(new GregorianCalendar()) + getUnderlines(run.getText(0).length() - ServiceUtils.getCalendarAsStringForFiles(new GregorianCalendar()).length()), 0);

        String uniChar = "\u0082";
    }

    public Message sendMessageWithFiles(User user, PhysicalRequest request) throws IOException, InvalidFlagsException, AccessDeniedException, NumeralsDeclensionNotSupportedException, ArgumentNotRussianException, ArgumentEmptyException {
        String resultStatementName = generateFile(user, request, true);
        String resultWorksheetName = generateFile(user, request, false);
        Long messageID = chatService.fillMessage("Файлы сгенерированы:", user, request, request.getChat(), true, false, null);
        Message message = chatService.getMessageById(messageID);
        chatService.addFilesToMessage(message, resultStatementName, resultWorksheetName);
        return message;
    }

    public void sendEmailWithFiles(User user, Message message) {
        mailSender.send(user, message);
        deleteFilesForMessage(message);
    }

    private void deleteFilesForMessage(Message message) {
        Iterator<MessageFile> iter = message.getMessageFiles().iterator();
        while (iter.hasNext()) {
            MessageFile f = iter.next();
            File file = new File(uploadPath + File.separator + "files" + File.separator + f.getNewFileName());
            if (file.delete()) {
                iter.remove();
                messageFileRepo.delete(f);
            }
        }
    }
}
