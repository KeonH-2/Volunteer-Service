package main;

import java.util.*;

public class VolunteerService {
    private static Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static ReservationManager reservationManager = new ReservationManager();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        userManager.loadUsers();

        while (true) {
            System.out.println("\n==== 봉사 신청 시스템 ====");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 예약신청");
            System.out.println("4. 마이페이지");
            System.out.println("5. 프로그램 종료");
            System.out.print("선택 > ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userManager.registerUser();
                    break;
                case "2":
                    loggedInUser = userManager.login();
                    break;
                case "3":
                    if (loggedInUser != null)
                        reservationManager.makeReservation(loggedInUser.getId());
                    else
                        System.out.println("먼저 로그인해주세요.");
                    break;
                case "4":
                    if (loggedInUser != null)
                        reservationManager.showMyReservations(loggedInUser.getId());
                    else
                        System.out.println("먼저 로그인해주세요.");
                    break;
                case "5":
                    userManager.saveUsers();
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
}