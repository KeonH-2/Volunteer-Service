package main;

import java.io.*;
import java.util.*;

public class VolunteerProgramManager {
    private List<String> volunteerPrograms = new ArrayList<>();
    private Map<String, String> programDates = new HashMap<>();
    private Map<String, Integer> maxParticipants = new HashMap<>();
    private final String FILE_NAME = "programs.txt";

    public VolunteerProgramManager() {
        loadPrograms();
    }

    public void uploadProgram(Scanner scanner) {
        System.out.println("[새 봉사 프로그램 업로드]");
        System.out.print("봉사 이름: ");
        String name = scanner.nextLine();

        System.out.print("봉사 날짜 (예: 2025-06-15): ");
        String date = scanner.nextLine();

        System.out.print("최대 신청 인원: ");
        int max;
        try {
            max = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해주세요.");
            return;
        }

        if (volunteerPrograms.contains(name)) {
            System.out.println("이미 존재하는 봉사 프로그램입니다.");
            return;
        }

        volunteerPrograms.add(name);
        programDates.put(name, date);
        maxParticipants.put(name, max);

        savePrograms();
        System.out.println("[" + name + "] 봉사 프로그램이 업로드되었습니다.");
    }

    public void savePrograms() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String program : volunteerPrograms) {
                String date = programDates.getOrDefault(program, "날짜 없음");
                int max = maxParticipants.getOrDefault(program, 0);
                writer.write(program + "," + date + "," + max);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("프로그램 정보를 저장하는 중 오류 발생");
        }
    }

    public void loadPrograms() {
        volunteerPrograms.clear();
        programDates.clear();
        maxParticipants.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String date = parts[1];
                    int max = Integer.parseInt(parts[2]);

                    volunteerPrograms.add(name);
                    programDates.put(name, date);
                    maxParticipants.put(name, max);
                }
            }
        } catch (IOException e) {
            System.out.println("봉사 프로그램 파일을 불러올 수 없습니다.");
        }
    }

    public List<String> getPrograms() {
        return volunteerPrograms;
    }

    public String getDate(String program) {
        return programDates.get(program);
    }

    public int getMaxParticipants(String program) {
        return maxParticipants.get(program);
    }

    public boolean contains(String program) {
        return volunteerPrograms.contains(program);
    }
}
