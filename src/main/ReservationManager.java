package main;

import java.util.*;

public class ReservationManager {

    // ===== 필드 =====
    private List<String> volunteerPrograms = Arrays.asList("환경 정화", "노인 복지관 보조", "길거리 캠페인");
    private Map<String, List<String>> reservations = new HashMap<>();
    private Map<String, Integer> maxParticipants = new HashMap<>();
    private Map<String, String> programDates = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);
    private String choice;
    
    // 임시 출력용 리스트
    private List<String> programs = Arrays.asList("환경 정화", "길거리 캠페인");
    public ReservationManager() {
        maxParticipants.put("환경 정화", 5);
        maxParticipants.put("노인 복지관 보조", 3);
        maxParticipants.put("길거리 캠페인", 4);

        programDates.put("환경 정화", "2025-06-01");
        programDates.put("노인 복지관 보조", "2025-06-01");
        programDates.put("길거리 캠페인", "2025-06-02");
    }

    // ===== 봉사 신청 =====
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

        for (String program : volunteerPrograms) {
            List<String> users = reservations.getOrDefault(program, new ArrayList<>());
            if (users.contains(userId) && programDates.get(program).equals(selectedDate)) {
                System.out.println("이미 같은 날짜(" + selectedDate + ")에 신청한 봉사 활동이 있습니다.");
                return;
            }
        }

        List<String> currentParticipants = reservations.getOrDefault(selectedProgram, new ArrayList<>());

        // 중복 신청 방지 코드
        if (currentParticipants.contains(userId)) {
            System.out.println("이미 해당 프로그램에 신청하셨습니다.");
            return;
        }

        // 인원 초과 확인
        if (currentParticipants.size() >= maxParticipants.getOrDefault(selectedProgram, 0)) {
            System.out.println("신청 인원이 모두 찼습니다.");
            return;
        }

        currentParticipants.add(userId);
        reservations.put(selectedProgram, currentParticipants);
        System.out.println(selectedProgram + " 신청이 완료되었습니다!");
    }

    // ===== 마이페이지 =====
    public void showMyReservations(String userId) {
        // 프로그램 출력
        for (int i = 0; i < programs.size(); i++) {
            int num = i + 1;
            System.out.println(num + ". " + programs.get(i));
        }

        while (true) {
            System.out.println("봉사 예약을 취소하시겠습니까?(Y/N) >> ");
            choice = scanner.nextLine();
            if (choice.equals("Y")) {
                this.cancelReservation(userId);
                break;
            } else if (choice.equals("N")) {
                System.out.println("메인화면으로 돌아갑니다.");
                break;
            } else {
                System.out.println("Y 또는 N으로 답변해주세요.");
            }
        }
    }

    // ===== 예약 취소 =====
    public void cancelReservation(String userId) {
        int reservnum;
        while (true) {
            System.out.println("예약을 취소하고 싶은 봉사의 번호를 입력해주세요 >> ");
            try {
                reservnum = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("봉사 이름이 아닌 봉사의 번호를 입력해주세요.");
            }
        }

        reservnum = reservnum - 1;
        if (reservnum >= 0 && reservnum < programs.size()) {
            programs.remove(reservnum);
        } else {
            System.out.println("유효하지 않은 번호입니다.");
            return;
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("예약이 취소되었습니다.");
    }
}