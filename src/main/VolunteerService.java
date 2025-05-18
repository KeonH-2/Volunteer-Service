package main;

import java.util.*;

public class VolunteerService {
    private static Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static VolunteerProgramManager programManager = new VolunteerProgramManager();
    private static ReservationManager reservationManager = new ReservationManager();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        userManager.loadUsers();
        programManager.loadPrograms();
        reservationManager.loadReservations();
        
        reservationManager.confirmReservations(programManager, userManager);
        
        while (true) {
            System.out.println("\n==== 봉사 신청 시스템 ====");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 프로그램 종료");
            System.out.print("선택 > ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userManager.registerUser();
                    break;
                case "2":
                    loggedInUser = userManager.login();
                    if (loggedInUser != null) {
                        if (loggedInUser.isAdmin()) {
                            adminLoop();
                        } else {
                            userLoop();
                        }
                    }
                    break;
                case "3":
                    userManager.saveUsers();
                    programManager.savePrograms();
                    reservationManager.saveReservations();
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // 관리자 메뉴
    private static void adminLoop() {
        while (true) {
            System.out.println("\n==== 관리자 메뉴 ====");
            System.out.println("1. 봉사 프로그램 등록");
            System.out.println("2. 봉사 프로그램 조회/필터");
            System.out.println("3. 로그아웃");
            System.out.print("선택 > ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    programManager.uploadProgram();
                    break;
                case "2":
                    filterAndShowPrograms();
                    break;
                case "3":
                    loggedInUser = null;
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // 봉사자 메뉴
    private static void userLoop() {
        while (true) {
            System.out.println("\n==== 봉사자 메뉴 ====");
            System.out.println("1. 봉사 프로그램 조회/필터");
            System.out.println("2. 예약 신청");
            System.out.println("3. 마이페이지(내 예약/취소)");
            System.out.println("4. 회원정보 수정");
            System.out.println("5. 로그아웃");
            System.out.print("선택 > ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    filterAndShowPrograms();
                    break;
                case "2":
                    reservationManager.makeReservation(loggedInUser.getId(), programManager);
                    break;
                case "3":
                    reservationManager.showMyReservations(loggedInUser.getId(), programManager, userManager);
                    break;
                case "4":
                    userManager.updateUserInfo(loggedInUser);
                    break;
                case "5":
                    loggedInUser = null;
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // 봉사 프로그램 필터/조회
    private static void filterAndShowPrograms() {
        System.out.print("장소(구 단위, 예: 강남구, 전체면 엔터): ");
        String location = scanner.nextLine();
        System.out.print("날짜(yyyy-MM-dd, 전체면 엔터): ");
        String date = scanner.nextLine();
        System.out.print("카테고리(예: 환경/교육/복지, 전체면 엔터): ");
        String category = scanner.nextLine();

        List<VolunteerProgram> filtered = programManager.filterPrograms(location, date, category);
        if (filtered.isEmpty()) {
            System.out.println("조건에 맞는 봉사 프로그램이 없습니다.");
        } else {
            System.out.println("[봉사 프로그램 목록]");
            for (VolunteerProgram p : filtered) {
                System.out.printf("- %s | %s | %s | %s | 인원: %d | 시간: %d\n",
                        p.getName(), p.getDate(), p.getLocation(), p.getCategory(),
                        p.getMaxParticipants(), p.getHours());
            }
        }
    }
}
