// package main; // 삭제

import java.io.*;
import java.util.*;

public class VolunteerProgramManager {
    private List<VolunteerProgram> programs;
    private static final String PROGRAM_FILE = "programs.txt";

    public VolunteerProgramManager() {
        programs = new ArrayList<>();
    }

    public void loadPrograms() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PROGRAM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String name = parts[0];
                    String date = parts[1];
                    String location = parts[2];
                    String category = parts[3];
                    int maxParticipants = Integer.parseInt(parts[4]);
                    int hours = Integer.parseInt(parts[5]);
                    programs.add(new VolunteerProgram(name, date, location, category, maxParticipants, hours));
                }
            }
        } catch (IOException e) {
            System.out.println("프로그램 데이터 로드 실패: " + e.getMessage());
        }
    }

    public void savePrograms() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PROGRAM_FILE))) {
            for (VolunteerProgram program : programs) {
                writer.println(String.format("%s,%s,%s,%s,%d,%d",
                    program.getName(), program.getDate(), program.getLocation(),
                    program.getCategory(), program.getMaxParticipants(), program.getHours()));
            }
        } catch (IOException e) {
            System.out.println("프로그램 데이터 저장 실패: " + e.getMessage());
        }
    }

    public void uploadProgram(String name, String date, String location, String category, int maxParticipants, int hours) {
        programs.add(new VolunteerProgram(name, date, location, category, maxParticipants, hours));
        savePrograms();
    }

    public List<VolunteerProgram> filterPrograms(String location, String date, String category) {
        List<VolunteerProgram> filtered = new ArrayList<>();
        for (VolunteerProgram program : programs) {
            boolean matchLocation = location.isEmpty() || program.getLocation().contains(location);
            boolean matchDate = date.isEmpty() || program.getDate().equals(date);
            boolean matchCategory = category.isEmpty() || program.getCategory().equals(category);
            
            if (matchLocation && matchDate && matchCategory) {
                filtered.add(program);
            }
        }
        return filtered;
    }

    public List<VolunteerProgram> getPrograms() {
        return programs;
    }
}
