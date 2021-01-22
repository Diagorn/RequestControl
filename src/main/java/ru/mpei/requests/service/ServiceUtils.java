package ru.mpei.requests.service;

import org.springframework.stereotype.Service;
import ru.mpei.requests.domain.requests.OrganisationRequest;
import ru.mpei.requests.domain.requests.PhysicalRequest;
import ru.mpei.requests.domain.requests.Request;

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

    public static Calendar parseStringToCalendar(String date) {
        Calendar result = new GregorianCalendar();
        result.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 3)));
        result.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 6)));
        result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8, 9)));
        return result;
    }

    public static String getCalendarAsString(GregorianCalendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(calendar.getTime());
    }

    public static List<Request> getCollidedRequestList(List<OrganisationRequest> organisationRequests,
                                                 List<PhysicalRequest> physicalRequests) {
        List<Request> resultList = new ArrayList<Request>();
        for (OrganisationRequest r: organisationRequests) {
            resultList.add((Request)r);
        }
        for (PhysicalRequest r: physicalRequests) {
            resultList.add((Request)r);
        }
        return resultList;
    }
}
