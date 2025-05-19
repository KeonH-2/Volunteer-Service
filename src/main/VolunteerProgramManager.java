package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.*;

public class VolunteerProgramManager {
    private List<VolunteerProgram> programs = new ArrayList<>();
    private final String FILE_NAME = "programs.json";
    private Scanner scanner = new Scanner(System.in);

    public void loadPrograms() {
        programs.clear();
        try (FileReader reader = new FileReader(FILE_NAME)) {
            JSONArray arr = (JSONArray) new JSONParser().parse(reader);
            for (Object o : arr) {
                JSONObject obj = (JSONObject) o;
                String name = (String) obj.get("name");
                String date = (String) obj.get("date");
                String location = (String) obj.get("location");
                String category = (String) obj.get("category");
                int max = ((Long) obj.get("maxParticipants")).intValue();
                int hours = ((Long) obj.get("hours")).intValue();
                programs.add(new VolunteerProgram(name, date, location, category, max, hours));
            }
        } catch (Exception e) { /* 파일 없을 때 등 무시 */ }
    }

    public void savePrograms() {
        JSONArray arr = new JSONArray();
        for (VolunteerProgram p : programs) {
            JSONObject obj = new JSONObject();
            obj.put("name", p.getName());
            obj.put("date", p.getDate());
            obj.put("location", p.getLocation());
            obj.put("category", p.getCategory());
            obj.put("maxParticipants", p.getMaxParticipants());
            obj.put("hours", p.getHours());
            arr.add(obj);
        }
        try (FileWriter file = new FileWriter(FILE_NAME)) {
            file.write(arr.toJSONString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void uploadProgram() {
        System.out.println("[새 봉사 프로그램 업로드]");
        System.out.print("봉사 이름: ");
        String name = scanner.nextLine();
        System.out.print("봉사 날짜 (예: 2025-06-15): ");
        String date = scanner.nextLine();
        System.out.print("장소(예: 서울 강남구): ");
        String location = scanner.nextLine();
        System.out.print("카테고리(예: 환경/교육/복지): ");
        String category = scanner.nextLine();
        System.out.print("최대 신청 인원: ");
        int max = Integer.parseInt(scanner.nextLine());
        System.out.print("봉사 시간(시간): ");
        int hours = Integer.parseInt(scanner.nextLine());

        for (VolunteerProgram p : programs) {
            if (p.getName().equals(name)) {
                System.out.println("이미 존재하는 봉사 프로그램입니다.");
                return;
            }
        }
        programs.add(new VolunteerProgram(name, date, location, category, max, hours));
        savePrograms();
        System.out.println("[" + name + "] 봉사 프로그램이 업로드되었습니다.");
    }

    // 필터 기능
    public List<VolunteerProgram> filterPrograms(String location, String date, String category) {
        List<VolunteerProgram> filtered = new ArrayList<>();
        for(VolunteerProgram check : programs){
            boolean match = true;
            if (!location.isEmpty() && !check.getLocation().equals(location)) { match = false; }
            if (!date.isEmpty() && !check.getDate().equals(date)) { match = false; }
            if (!category.isEmpty() && !check.getCategory().equals(category)) { match = false; }   
            if(match){ filtered.add(check); } 
        }
        return filtered;
    }
    public VolunteerProgram getProgramByName(String name) {
        for (VolunteerProgram p : programs) if (p.getName().equals(name)) return p;
        return null;
    }
    public List<VolunteerProgram> getPrograms() { return programs; }
}
