package main;

public class VolunteerService {
	public static void main(String[] args) {
        // user load 하는 func

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
                    // sign in func
                    break;
                case "2":
                    // login func
                    break;
                case "3":
                    // 예약신청 func
                    break;
                case "4":
                    // mypage 여기서 신청된 봉사 확인 및 취소 func
                    break;
                case "5":
                    // 사용자 정보 저장하는 func
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
}
