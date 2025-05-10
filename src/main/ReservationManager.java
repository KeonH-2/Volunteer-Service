package main;

import java.util.*;

public class ReservationManager {
    private List<String> volunteerPrograms = Arrays.asList("환경 정화", "노인 복지관 보조", "길거리 캠페인");
    private Map<String, List<String>> reservations = new HashMap<>();
    private Map<String, Integer> maxParticipants = new HashMap<>();
    private Map<String, String> programDates = new HashMap<>();
    private Scanner scanner = new Scanner(System.in); 

    public void makeReservation(String userId) {
        System.out.println("[봉사 프로그램 목록]");
        for (int i = 0; i < volunteerPrograms.size(); i++) {
            String program = volunteerPrograms.get(i);
            List<String> participants = reservations.getOrDefault(program, new ArrayList<>());
            int current = participants.size();
            int max = maxParticipants.getOrDefault(program, 0);
            System.out.printf("%d. %s (%s) - 신청자 수: %d/%d\n",
                    i + 1, program, programDates.getOrDefault(program, "날짜 미정"), current, max);
        }

        System.out.print("신청할 프로그램 번호를 입력하세요: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해주세요.");
            return;
        }

        if (choice < 0 || choice >= volunteerPrograms.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        String selectedProgram = volunteerPrograms.get(choice);
        String selectedDate = programDates.get(selectedProgram);

        // 이미 같은 날짜에 신청했는지 확인
        for (String program : volunteerPrograms) {
            List<String> users = reservations.getOrDefault(program, new ArrayList<>());
            if (users.contains(userId) && programDates.get(program).equals(selectedDate)) {
                System.out.println("이미 같은 날짜(" + selectedDate + ")에 신청한 봉사 활동이 있습니다.");
                return;
            }
        }

        // 인원 초과 확인
        List<String> currentParticipants = reservations.getOrDefault(selectedProgram, new ArrayList<>());
        if (currentParticipants.size() >= maxParticipants.getOrDefault(selectedProgram, 0)) {
            System.out.println("신청 인원이 모두 찼습니다.");
            return;
        }

        // 신청 처리
        currentParticipants.add(userId);
        reservations.put(selectedProgram, currentParticipants);
        System.out.println(selectedProgram + " 신청이 완료되었습니다!");
    }

    public void showMyReservations(String userId) {
        // 마이페이지 조회 구현 예정
    }

    public void cancelReservation(String userId) {
        // 예약 취소 구현 예정
    }
}
