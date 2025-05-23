package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.*;

public class ReservationManager {
    // 프로그램명 → 예약자 ID 리스트
    private Map<String, List<String>> reservations = new HashMap<>();
    // 프로그램명 → 대기자 ID 리스트
    private Map<String, List<String>> waitingList = new HashMap<>();
    private final String FILE_NAME = "reservations.json";
    private Scanner scanner = new Scanner(System.in);

    // 파일에서 예약/대기자 정보 로드
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

    // 파일로 예약/대기자 정보 저장
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

    // 내 예약 프로그램명 리스트
    public List<String> getMyReservations(String userId) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : reservations.entrySet()) {
            if (entry.getValue().contains(userId)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    // 내 대기 프로그램명 리스트
    public List<String> getMyWaiting(String userId) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : waitingList.entrySet()) {
            if (entry.getValue().contains(userId)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    // 예약 신청 (프로그램명으로)
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

    // 예약 취소 (GUI/내부 로직용, 선택한 예약만 리스트로 넘기면 됨)
    public void cancelReservation(String userId, VolunteerProgramManager vpm, List<String> myPrograms, UserManager userManager) {
        if (myPrograms == null || myPrograms.isEmpty()) return;
        String progName = myPrograms.get(0); // GUI에서는 선택한 예약 하나만 리스트로 넘김
        List<String> resList = reservations.get(progName);
        if (resList != null && resList.remove(userId)) {
            // 대기자 승격
            List<String> waitList = waitingList.get(progName);
            if (waitList != null && !waitList.isEmpty()) {
                String nextUserId = waitList.remove(0);
                resList.add(nextUserId);

                // 알림 전송
                User nextUser = userManager.getUserById(nextUserId);
                VolunteerProgram p = vpm.getProgramByName(progName);
                if (nextUser != null && p != null) {
                    nextUser.addNotification("[" + p.getName() + "] 예약이 확정되었습니다!");
                }
            }
            saveReservations();
            userManager.saveUsers();
        }
    }

    // 예약 취소 (GUI/내부 로직용, 단일 예약명)
    public boolean cancelReservation(String userId, String progName) {
        boolean changed = false;
        // 예약자에서 삭제
        List<String> resList = reservations.get(progName);
        if (resList != null && resList.remove(userId)) {
            changed = true;
            // 대기자 승격
            List<String> waitList = waitingList.get(progName);
            if (waitList != null && !waitList.isEmpty()) {
                String next = waitList.remove(0);
                resList.add(next);
                if (waitList.isEmpty()) waitingList.remove(progName);
            }
        }
        // 대기자에서 삭제
        List<String> waitList = waitingList.get(progName);
        if (waitList != null && waitList.remove(userId)) {
            changed = true;
            if (waitList.isEmpty()) waitingList.remove(progName);
        }
        if (changed) saveReservations();
        return changed;
    }
}
