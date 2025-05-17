package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservationManager {
    // <프로그램명, 예약자ID리스트>
    private Map<String, List<String>> reservations = new HashMap<>();
    // <프로그램명, 대기자ID리스트>
    private Map<String, List<String>> waitingList = new HashMap<>();
    private final String FILE_NAME = "reservations.json";
    private Scanner scanner = new Scanner(System.in);

    public void loadReservations() {
        reservations.clear();
        waitingList.clear();
        try (FileReader reader = new FileReader(FILE_NAME)) {
            JSONObject obj = (JSONObject) new JSONParser().parse(reader);
            JSONObject resObj = (JSONObject) obj.get("reservations");
            JSONObject waitObj = (JSONObject) obj.get("waitingList");
            if (resObj != null) {
                for (Object key : resObj.keySet()) {
                    String program = (String) key;
                    JSONArray arr = (JSONArray) resObj.get(program);
                    List<String> ids = new ArrayList<>();
                    for (Object o : arr) ids.add((String) o);
                    reservations.put(program, ids);
                }
            }
            if (waitObj != null) {
                for (Object key : waitObj.keySet()) {
                    String program = (String) key;
                    JSONArray arr = (JSONArray) waitObj.get(program);
                    List<String> ids = new ArrayList<>();
                    for (Object o : arr) ids.add((String) o);
                    waitingList.put(program, ids);
                }
            }
        } catch (Exception e) { /* 파일 없을 때 등 무시 */ }
    }

    public void saveReservations() {
        JSONObject obj = new JSONObject();
        JSONObject resObj = new JSONObject();
        JSONObject waitObj = new JSONObject();
        for (String program : reservations.keySet()) {
            JSONArray arr = new JSONArray();
            for (String id : reservations.get(program)) arr.add(id);
            resObj.put(program, arr);
        }
        for (String program : waitingList.keySet()) {
            JSONArray arr = new JSONArray();
            for (String id : waitingList.get(program)) arr.add(id);
            waitObj.put(program, arr);
        }
        obj.put("reservations", resObj);
        obj.put("waitingList", waitObj);
        try (FileWriter file = new FileWriter(FILE_NAME)) {
            file.write(obj.toJSONString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 봉사 신청
    public void makeReservation(String userId, VolunteerProgramManager vpm) {
        List<VolunteerProgram> programs = vpm.getPrograms();
        System.out.println("[봉사 프로그램 목록]");
        for (int i = 0; i < programs.size(); i++) {
            VolunteerProgram p = programs.get(i);
            List<String> participants = reservations.getOrDefault(p.getName(), new ArrayList<>());
            int current = participants.size();
            int max = p.getMaxParticipants();
            System.out.printf("%d. %s (%s, %s, %s) - 신청자 수: %d/%d\n",
                    i + 1, p.getName(), p.getDate(), p.getLocation(), p.getCategory(), current, max);
        }
        System.out.print("신청할 프로그램 번호를 입력하세요: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해주세요.");
            return;
        }
        if (choice < 0 || choice >= programs.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }
        VolunteerProgram selectedProgram = programs.get(choice);
        String programName = selectedProgram.getName();
        String programDate = selectedProgram.getDate();

        // 같은 날짜 중복 신청 방지
        for (String prog : reservations.keySet()) {
            VolunteerProgram p = vpm.getProgramByName(prog);
            if (p != null && reservations.get(prog).contains(userId) && p.getDate().equals(programDate)) {
                System.out.println("이미 같은 날짜(" + programDate + ")에 신청한 봉사 활동이 있습니다.");
                return;
            }
        }
        // 중복 신청 방지
        List<String> currentParticipants = reservations.getOrDefault(programName, new ArrayList<>());
        if (currentParticipants.contains(userId)) {
            System.out.println("이미 해당 프로그램에 신청하셨습니다.");
            return;
        }
        // 인원 초과 시 대기자 등록
        if (currentParticipants.size() >= selectedProgram.getMaxParticipants()) {
            List<String> waiters = waitingList.getOrDefault(programName, new ArrayList<>());
            if (waiters.contains(userId)) {
                System.out.println("이미 대기자로 등록되어 있습니다.");
                return;
            }
            waiters.add(userId);
            waitingList.put(programName, waiters);
            saveReservations();
            System.out.println("정원이 가득 찼습니다. 대기자로 등록되었습니다.");
            return;
        }
        // 정상 신청
        currentParticipants.add(userId);
        reservations.put(programName, currentParticipants);
        saveReservations();
        System.out.println(programName + " 신청이 완료되었습니다!");
    }

    // 마이페이지: 내 예약 보기 및 취소
    public void showMyReservations(String userId, VolunteerProgramManager vpm) {
        List<String> myPrograms = new ArrayList<>();
        for (String program : reservations.keySet()) {
            if (reservations.get(program).contains(userId)) myPrograms.add(program);
        }
        if (myPrograms.isEmpty()) {
            System.out.println("예약된 봉사 프로그램이 없습니다.");
            return;
        }
        for (int i = 0; i < myPrograms.size(); i++) {
            VolunteerProgram p = vpm.getProgramByName(myPrograms.get(i));
            System.out.printf("%d. %s (%s, %s, %s)\n", i + 1, p.getName(), p.getDate(), p.getLocation(), p.getCategory());
        }
        System.out.println("봉사 예약을 취소하시겠습니까?(Y/N) >> ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("Y")) {
            cancelReservation(userId, vpm, myPrograms);
        } else {
            System.out.println("메인화면으로 돌아갑니다.");
        }
    }

    // 예약 취소
    public void cancelReservation(String userId, VolunteerProgramManager vpm, List<String> myPrograms) {
        System.out.println("예약을 취소하고 싶은 봉사의 번호를 입력해주세요 >> ");
        int reservnum;
        try {
            reservnum = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("번호를 입력해주세요.");
            return;
        }
        if (reservnum >= 0 && reservnum < myPrograms.size()) {
            String programToCancel = myPrograms.get(reservnum);
            reservations.get(programToCancel).remove(userId);
            // 대기자 승격
            List<String> waiters = waitingList.getOrDefault(programToCancel, new ArrayList<>());
            if (!waiters.isEmpty()) {
                String nextUser = waiters.remove(0);
                reservations.get(programToCancel).add(nextUser);
                waitingList.put(programToCancel, waiters);
                System.out.println("대기자 " + nextUser + "님이 예약자로 승격되었습니다.");
            }
            saveReservations();
            System.out.println("예약이 취소되었습니다.");
        } else {
            System.out.println("유효하지 않은 번호입니다.");
        }
    }

    // 24시간 전 봉사 확정
    public void confirmReservations(VolunteerProgramManager vpm, UserManager um) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (VolunteerProgram p : vpm.getPrograms()) {
            LocalDate date = LocalDate.parse(p.getDate(), formatter);
            LocalDateTime programDateTime = date.atStartOfDay();
            long hoursDiff = Duration.between(now, programDateTime).toHours();
            if (hoursDiff <= 24 && reservations.containsKey(p.getName())) {
                // 예약자 확정 및 봉사 시간 누적
                List<String> confirmed = reservations.get(p.getName());
                for (String userId : confirmed) {
                    User user = um.getUserById(userId);
                    if (user != null) {
                        user.addVolunteerHours(p.getHours());
                        System.out.println(user.getName() + "님의 [" + p.getName() + "] 봉사 예약이 확정되었습니다! 누적 봉사 시간: " + user.getTotalVolunteerHours());
                    }
                }
                // 확정 후 예약/대기자 삭제
                reservations.remove(p.getName());
                waitingList.remove(p.getName());
            }
        }
        saveReservations();
        um.saveUsers();
    }
}
