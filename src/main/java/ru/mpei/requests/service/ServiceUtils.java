package ru.mpei.requests.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.chats.Message;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;
import ru.mpei.requests.domain.requests.RequestState;
import ru.mpei.requests.domain.users.Role;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static void createMessageFiles(Set<MultipartFile> files) {

    }
}