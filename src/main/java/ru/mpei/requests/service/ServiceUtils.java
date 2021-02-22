package ru.mpei.requests.service;

import org.springframework.stereotype.Service;
import ru.morpher.ws3.Client;
import ru.morpher.ws3.ClientBuilder;
import ru.morpher.ws3.russian.DeclensionResult;
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ServiceUtils {
    public static <T> List<T> collideLists(List<T> a, List<T> b) { //Utility that adds members of list B to list A
        for (T typeObject: b) {
            if (!a.contains(typeObject))
                a.add(typeObject);
        }
        return a;
    }

    public static Calendar parseStringToCalendar(String date) throws ParseException {
        Calendar result = new GregorianCalendar();
        String[] resultStrings = date.split("-");
        result.set(Calendar.YEAR, Integer.parseInt(resultStrings[0]));
        result.set(Calendar.MONTH, Integer.parseInt(resultStrings[1])-1);
        result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(resultStrings[2]));
        return result;
    }

    public static String getCalendarAsString(GregorianCalendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(calendar.getTime());
    }

    public static String getCalendarAsStringForFiles(GregorianCalendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat(("dd.MM.yyyy"));
        return format.format(calendar.getTime());
    }

    public static List<Request> getCollidedRequestList(List<OrganisationRequest> organisationRequests,
                                                 List<PhysicalRequest> physicalRequests) {
        List<Request> resultList = new ArrayList<Request>();
        resultList.addAll(organisationRequests);
        resultList.addAll(physicalRequests);
        return resultList;
    }

    public static String getEducationFormat(String education) {
        switch(education) {
            case "school-not-finished" :
                return "Неоконченное среднее";
            case "school-finished":
                return "Среднее";
            case "college-not-finished":
                return "Среднее профессиональное неоконченное";
            case "college-finished":
                return "Среднее профессиональное оконченное";
            case "university-not-finished":
                return "Высшее неоконченное";
            case "university-bachelor":
                return "Высшее (бакалавр)";
            case "university-master":
                return "Высшее (магистр)";
            case "university-candidate":
                return "Высшее (кандидат наук)";
            case "university-doctor":
                return "Высшее (доктор наук)";
            default:
                return education;
        }
    }

    public static String getCalendarForMessage(GregorianCalendar calendar) {
        String result = "";
        switch (calendar.get(GregorianCalendar.DAY_OF_WEEK)) {
            case 1:
                result += "Воскресенье, ";
                break;
            case 2:
                result += "Понедельник, ";
                break;
            case 3:
                result += "Вторник, ";
                break;
            case 4:
                result += "Среда, ";
                break;
            case 5:
                result += "Четверг, ";
                break;
            case 6:
                result += "Пятница, ";
                break;
            case 7:
                result += "Суббота, ";
                break;
            default:
                result += "ДЕНЬ_НЕДЕЛИ, ";
        }
        result += calendar.get(GregorianCalendar.DAY_OF_MONTH);
        switch (calendar.get(GregorianCalendar.MONTH)) {
            case 0:
                result += " января, ";
                break;
            case 1:
                result += " февраля, ";
                break;
            case 2:
                result += " марта, ";
                break;
            case 3:
                result += " апреля, ";
                break;
            case 4:
                result += " мая, ";
                break;
            case 5:
                result += " июня, ";
                break;
            case 6:
                result += " июля, ";
                break;
            case 7:
                result += " августа, ";
                break;
            case 8:
                result += " сентября, ";
                break;
            case 9:
                result += " октября, ";
                break;
            case 10:
                result += " ноября, ";
                break;
            case 11:
                result += " декабря, ";
                break;
            default:
                result += " МЕСЯЦ, ";
        }
        result += calendar.get(GregorianCalendar.HOUR_OF_DAY);
        result += ":";
        if (calendar.get(GregorianCalendar.MINUTE) < 10) {
            result += "0";
            result += calendar.get(GregorianCalendar.MINUTE);
        } else {
            result += calendar.get(GregorianCalendar.MINUTE);
        }
        return result;
    }

    public static String getRequestStateAsString(RequestState state) {
        if (state == RequestState.NO_EXECUTER)
            return "Без исполнителя";
        if (state == RequestState.IN_PROCESS)
            return "В процессе";
        if (state == RequestState.COMPLETE)
            return "Завершённая";
        if (state == RequestState.DELETED)
            return "Удалённая";
        if (state == RequestState.FROZEN)
            return "Замороженная";
        if (state == RequestState.PROTOTYPE)
            return "Прототип";
        return "";
    }

    public static Message getLastMessage(Request request) {
        int size = request.getChat().getMessages().size();
        Message[] messageArray = new Message[size];
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 1488);
        Object[] messages = request.getChat().getMessages().toArray();
        Message message = null;
        for (int i = 0; i < size; ++i) {
            messageArray[i] = (Message) messages[i];
            if (messageArray[i].getTimeOfSending().after(calendar)) { //Looking for the last message
                message = messageArray[i];
                calendar = message.getTimeOfSending();
            }
        }
        return message;
    }

    public static String getInstitute(User user) {
        String group = user.getPerson().getGroup();
        if (group.contains("ИЭ")) {
            return "ИнЭИ";
        }
        if (group.contains("А")) {
            return "ИВТИ";
        }
        if (group.contains("ИГ")) {
            return "ИГВИЭ";
        }
        if (group.contains("ЭР")) {
            return "ИРЭ";
        }
        if (group.contains("ГП")) {
            return "ГПИ";
        }
        if (group.contains("ТФ")) {
            return "ИТАЭ";
        }
        if (group.contains("ЭЛ")) {
            return "ИЭТЭ";
        }
        if (group.contains("С")) {
            return "ЭнМИ";
        }
        if (group.contains("ФП")) {
            return "ИЭВТ";
        }
        return "ИЭЭ";
    }

    public static String getDirector(User user) {
        String group = user.getPerson().getGroup();
        if (group.contains("ИЭ")) {
            return "Невскому А.Ю.";
        }
        if (group.contains("А")) {
            return "Вишнякову С.В.";
        }
        if (group.contains("ИГ")) {
            return "Шестопаловой Т.А.";
        }
        if (group.contains("ЭР")) {
            return "Мирошниковой И.Н.";
        }
        if (group.contains("ГП")) {
            return "Родину А.Б.";
        }
        if (group.contains("ТФ")) {
            return "Дедову А.В.";
        }
        if (group.contains("ЭЛ")) {
            return "Погребисскому М.Я.";
        }
        if (group.contains("С")) {
            return "Меркурьеву И.В.";
        }
        if (group.contains("ФП")) {
            return "Захарову С.В.";
        }
        return "Тульскому В.Н.";
    }
}