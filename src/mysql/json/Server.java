package mysql.json;

import mysql.Database;
import mysql.entity.Monument;
import mysql.helper.Help;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;

public class Server {
    public String getJSON(){
        List<Monument> list = Help.getMonuments();
        if (list.isEmpty())
            return "{}";
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Monument monument : list){
             JSONObject temp = new JSONObject();
             temp.put("id", monument.getId());
             temp.put("country", monument.getCountryName());
             temp.put("city", monument.getCityName());
             temp.put("monument", monument.getMonumentName());
             jsonArray.add(temp);
        }
        jsonObject.put("count", list.size());
        jsonObject.put("Monuments", jsonArray);
        return jsonObject.toJSONString();
    }


    public boolean insertNewMonument(String json) {
        Database dat = new Database();
        try {
            Object object = new JSONParser().parse(json);
            JSONObject jsonObject = (JSONObject) object;
            String countryName = (String) jsonObject.get("country");
            String cityName = (String) jsonObject.get("city");
            String monumentName = (String) jsonObject.get("monument");
            dat.insertNewMonument(Help.getCountryCode(countryName), cityName, monumentName);
        } catch (ParseException e) { e.printStackTrace(); }
        return true;
    }
}
