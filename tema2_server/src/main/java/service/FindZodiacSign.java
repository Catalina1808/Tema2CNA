package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
        System.out.println(date);
        DateFormat dateFormat2 = new SimpleDateFormat("dd/MM");

        for (int index = 0; index < zodiacSigns.size(); index++) {
            Date begin = dateFormat2.parse(zodiacSigns.get(index).getStartDate());
            Date end = dateFormat2.parse(zodiacSigns.get(index).getEndDate());

            if(begin.getMonth()==11 && end.getMonth()==0) {
                end.setYear(end.getYear() + 1);
                if(date.getMonth()==0)
                    date.setYear(date.getYear()+1);
                System.out.println(date);
            }

            if (date.after(begin) && date.before(end) || date.equals(begin) || date.equals(end)) {
                return zodiacSigns.get(index).getSign();
            }
        }
        return null;
    }
}
