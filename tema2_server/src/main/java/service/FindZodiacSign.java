package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FindZodiacSign {

    public ArrayList<ZodiacSign> readFromFile(String fileText) throws FileNotFoundException {
        ArrayList<ZodiacSign> zodiacSigns= new ArrayList<>();
        try {
            File file = new File(fileText);
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String sign = sc.next();
                String startDate = sc.next();
                String endDate = sc.next();
                ZodiacSign zodiacSign = new ZodiacSign(sign, startDate, endDate);
                zodiacSigns.add(zodiacSign);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR" + e);
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
        return zodiacSigns;
    }

    public String findZodiac(String dateString, ArrayList<ZodiacSign> zodiacSigns) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date date = dateFormat.parse(dateString);

        Calendar calendarDate = Calendar.getInstance();
        Calendar calendarBegin = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();

        DateFormat dateFormat2 = new SimpleDateFormat("dd/MM");

        for (int index = 0; index < zodiacSigns.size(); index++) {
            Date begin = dateFormat2.parse(zodiacSigns.get(index).getStartDate());
            Date end = dateFormat2.parse(zodiacSigns.get(index).getEndDate());

            calendarBegin.setTime(begin);
            calendarEnd.setTime(end);
            calendarDate.setTime(date);

            if(calendarBegin.get(Calendar.MONTH)==11 && calendarEnd.get(Calendar.MONTH)==0) {
                calendarEnd.set(Calendar.YEAR, calendarEnd.get(Calendar.YEAR)+1);
                if(calendarDate.get(Calendar.MONTH)==0)
                    calendarDate.set(Calendar.YEAR, calendarDate.get(Calendar.YEAR)+1);
            }

            if (calendarDate.after(calendarBegin) && calendarDate.before(calendarEnd) ||
                    calendarDate.equals(calendarBegin) || calendarDate.equals(calendarEnd)) {
                return zodiacSigns.get(index).getSign();
            }
        }
        return null;
    }
}
