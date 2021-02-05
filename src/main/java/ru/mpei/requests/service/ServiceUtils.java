package ru.mpei.requests.service;

import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;

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
}
