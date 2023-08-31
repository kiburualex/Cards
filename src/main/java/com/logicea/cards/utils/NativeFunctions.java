package com.logicea.cards.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alex Kiburu
 */
@Slf4j
@Component
public class NativeFunctions {

    public String formatEndDate(String endDate){
        if(null == endDate)return null;
        String formattedDate = "";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(endDate));
            c.add(Calendar.DATE, 1);  // number of days to add
            formattedDate = sdf.format(c.getTime());
        }catch(Exception ex){
            log.error("Error converting date time", ex);
        }

        return formattedDate;
    }

    public boolean isDateFormatValid(String dateStr, String dateFormat) {
        log.info("isDateFormatValid dateStr=" + dateStr + ", dateFormat=" + dateFormat);
        DateFormat sdf = new SimpleDateFormat("" + dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean isColorCodeValid(String colorCode){
        String HEX_WEBCOLOR_PATTERN = "^#([a-fA-F0-9]{6})$";
        Pattern pattern = Pattern.compile(HEX_WEBCOLOR_PATTERN);
        Matcher matcher = pattern.matcher(colorCode);
        return matcher.matches();
    }
}
