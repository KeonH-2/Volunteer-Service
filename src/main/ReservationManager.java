package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.*;

public class ReservationManager {
    private Map<String, List<String>> reservations = new HashMap<>();
    private Map<String, List<String>> waitingList = new HashMap<>();
    private final String FILE_NAME = "reservations.json";
    private Scanner scanner = new Scanner(System.in);
    private UserManager userManager;
    private VolunteerProgramManager programManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    public void setProgramManager(VolunteerProgramManager programManager) {
        this.programManager = programManager;
    }

    public void loadReservations() {
        reservations.clear();
        waitingList.clear();
        try (FileReader reader = new FileReader(FILE_NAME)) {
            JSONObject root = (JSONObject) new JSONParser().parse(reader);
            // 예약자
            JSONObject resObj = (JSONObject) root.get("reservations");
            if (resObj != null) {
                for (Object key : resObj.keySet()) {
                    String prog = (String) key;
                    JSONArray arr = (JSONArray) resObj.get(prog);
                    List<String> list = new ArrayList<>();
                    for (Object o : arr) list.add((String) o);
                    reservations.put(prog, list);
                }
            }
            // 대기자
            JSONObject waitObj = (JSONObject) root.get("waitingList");
            if (waitObj != null) {
                for (Object key : waitObj.keySet()) {
                    String prog = (String) key;
                    JSONArray arr = (JSONArray) waitObj.get(prog);
                    List<String> list = new ArrayList<>();
                    for (Object o : arr) list.add((String) o);
                    waitingList.put(prog, list);
                }
            }
        } catch (Exception e) { /* 파일 없을 때 등 무시 */ }
    }

    public void saveReservations() {
        JSONObject root = new JSONObject();
        JSONObject resObj = new JSONObject();
        JSONObject waitObj = new JSONObject();

        for (String prog : reservations.keySet()) {
            JSONArray arr = new JSONArray();
            for (String id : reservations.get(prog)) arr.add(id);
            resObj.put(prog, arr);
        }
        for (String prog : waitingList.keySet()) {
            JSONArray arr = new JSONArray();
            for (String id : waitingList.get(prog)) arr.add(id);
            waitObj.put(prog, arr);
        }
        root.put("reservations", resObj);
        root.put("waitingList", waitObj);

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write(root.toJSONString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<String> getMyReservations(String userId) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : reservations.entrySet()) {
            if (entry.getValue().contains(userId)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public List<String> getMyWaiting(String userId) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : waitingList.entrySet()) {
            if (entry.getValue().contains(userId)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public boolean makeReservation(String userId, VolunteerProgramManager vpm, String progName) {
        VolunteerProgram prog = vpm.getProgramByName(progName);
        if (prog == null) return false;

        // 이미 예약/대기 중인지 체크
        List<String> resList = reservations.getOrDefault(progName, new ArrayList<>());
        List<String> waitList = waitingList.getOrDefault(progName, new ArrayList<>());
        if (resList.contains(userId) || waitList.contains(userId)) return false;

        // 정원 체크
        if (resList.size() < prog.getMaxParticipants()) {
            resList.add(userId);
            reservations.put(progName, resList);
            saveReservations();
            return true;
        } else {
            waitList.add(userId);
            waitingList.put(progName, waitList);
            saveReservations();
            return true;
        }
    }

//    // 예약 취소 (CLI)
//    public boolean cancelReservation(String userId, VolunteerProgramManager vpm, List<String> myPrograms, UserManager userManager) {
//        System.out.print("예약을 취소하고 싶은 봉사의 번호를 입력해주세요 >> ");
//        int reservnum;
//        try {
//            reservnum = Integer.parseInt(scanner.nextLine()) - 1;
//        } catch (NumberFormatException e) {
//            System.out.println("번호를 입력해주세요.");
//            return false;
//        }
//        if (reservnum >= 0 && reservnum < myPrograms.size()) {
//            String programToCancel = myPrograms.get(reservnum);
//            List<String> resList = reservations.get(programToCancel);
//            if (resList != null && resList.remove(userId)) {
//                // 대기자 승격
//                List<String> waiters = waitingList.getOrDefault(programToCancel, new ArrayList<>());
//                if (!waiters.isEmpty()) {
//                    String nextUser = waiters.remove(0);
//                    resList.add(nextUser);
//                    waitingList.put(programToCancel, waiters);
//                    // 승격된 사용자에게 알림 추가
//                    User promotedUser = userManager.getUserById(nextUser);
//                    VolunteerProgram p = vpm.getProgramByName(programToCancel);
//                    if (promotedUser != null && p != null) {
//                        promotedUser.addNotification("[" + p.getName() + "] 봉사 예약이 대기에서 확정되었습니다!");
//                        userManager.saveUsers();
//                    }
//                }
//                saveReservations();
//                return true; // 성공
//            } else {
//                System.out.println("이미 취소되었거나 잘못된 요청입니다.");
//                return false;
//            }
//        } else {
//            System.out.println("유효하지 않은 번호입니다.");
//            return false;
//        }
//    }
    // 예약 취소 (GUI/내부 로직용, 단일 예약명)
    public boolean cancelReservation(String userId, VolunteerProgramManager vpm, List<String> singleProgram, UserManager userManager) {
        // singleProgram: 취소할 프로그램명만 1개 담긴 리스트
        if (singleProgram == null || singleProgram.isEmpty()) {
            System.out.println("취소할 프로그램이 없습니다.");
            return false;
        }
        String programToCancel = singleProgram.get(0);
        List<String> resList = reservations.get(programToCancel);
        if (resList != null && resList.remove(userId)) {
            // 대기자 승격
            List<String> waiters = waitingList.getOrDefault(programToCancel, new ArrayList<>());
            if (!waiters.isEmpty()) {
                String nextUser = waiters.remove(0);
                resList.add(nextUser);
                waitingList.put(programToCancel, waiters);
                // 승격된 사용자에게 알림 추가
                User promotedUser = userManager.getUserById(nextUser);
                VolunteerProgram p = vpm.getProgramByName(programToCancel);
                if (promotedUser != null && p != null) {
                    promotedUser.addNotification("[" + p.getName() + "] 봉사 예약이 대기에서 확정되었습니다!");
                    userManager.saveUsers();
                }
            }
            saveReservations();
            return true; // 성공
        } else {
            System.out.println("이미 취소되었거나 잘못된 요청입니다.");
            return false;
        }
    }

    public void showMyReservations(String userId, VolunteerProgramManager vpm, UserManager userManager) {
        List<String> myPrograms = new ArrayList<>();
        for (String program : reservations.keySet()) {
            List<String> resList = reservations.get(program);
            if (resList != null && resList.contains(userId)) myPrograms.add(program);
        }
        if (myPrograms.isEmpty()) {
            System.out.println("예약된 봉사 프로그램이 없습니다.");
            return;
        }
        for (int i = 0; i < myPrograms.size(); i++) {
            VolunteerProgram p = vpm.getProgramByName(myPrograms.get(i));
            if (p != null) {
                System.out.printf("%d. %s (%s, %s, %s)\n", i + 1, p.getName(), p.getDate(), p.getLocation(), p.getCategory());
            } else {
                System.out.printf("%d. %s (프로그램 정보 없음)\n", i + 1, myPrograms.get(i));
            }
        }
        System.out.print("봉사 예약을 취소하시겠습니까?(Y/N) >> ");
        String choice = scanner.nextLine();
        if (choice != null && choice.equalsIgnoreCase("Y")) {
            cancelReservation(userId, vpm, myPrograms, userManager);
        } else {
            System.out.println("메인화면으로 돌아갑니다.");
        }
    }


    public void makeReservation(String userId, VolunteerProgramManager vpm) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("예약할 프로그램명을 입력하세요: ");
        String progName = scanner.nextLine().trim();
        boolean result = makeReservation(userId, vpm, progName);
        if (result) {
            System.out.println("예약 요청이 완료되었습니다. (정원 초과 시 대기자로 등록됩니다.)");
        } else {
            System.out.println("이미 예약 또는 대기 중이거나 신청할 수 없습니다.");
        }
    }
}
