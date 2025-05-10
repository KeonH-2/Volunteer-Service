package main;

import java.util.*;
import java.util.Scanner;
//import java.io.BufferedWriter;
//import java.io.FileWriter;

public class ReservationManager {
//    private List<String> volunteerPrograms = Arrays.asList("환경 정화", "노인 복지관 보조", "길거리 캠페인");
//    private Map<String, List<String>> reservations = new HashMap<>();
    
    private String choice;
    private List<String> programs = Arrays.asList("환경 정화", "길거리 캠페인");
    
    //public void makeReservation(String userId) {
        // 예약 로직
    //}

    public void showMyReservations(String userId) {
        // 마이페이지 조회
    	
    	// 아이디에 따라 예약 내역 출력
    	for (int i=0 ; i<programs.size() ; i++) {
    		int num=i+1;
    		System.out.println(num+". "+programs.get(i));
    	};
    	
    	//취소할지 물어보기
    	Scanner scanner = new Scanner(System.in);
    	while (true) {
    		System.out.println("봉사 예약을 취소하시겠습니까?(Y/N) >> ");
    		choice = scanner.nextLine();
    		if (choice.equals("Y")) {
    			this.cancelReservation(userId);
    			break;
    		}
    		else if(choice.equals("N")) {
    			System.out.println("메인화면으로 돌아갑니다.");
    			break;
    		}
    		else {
    			System.out.println("Y 또는 N으로 답변해주세요.");
    		}
    	}
    }

    public void cancelReservation(String userId) {
        // 예약 취소 로직
    	Scanner scanner = new Scanner(System.in);
    	int reservnum;
    	while(true) {
    		System.out.println("예약을 취소하고 싶은 봉사의 번호를 입력해주세요 >> ");
        	try {
        		reservnum = Integer.parseInt(scanner.nextLine());
        		break;
        	}
        	catch(NumberFormatException e) {
        		System.out.println("봉사 이름이 아닌 봉사의 번호를 입력해주세요.");
        	}
    	}
    	reservnum = reservnum-1;
    	if (reservnum >= 0 && reservnum < programs.size()) {
    	    programs.remove(reservnum);
    	} else {
    	    System.out.println("유효하지 않은 번호입니다.");
    	}
    	
    	//안내 메세지
    	try {
    	    Thread.sleep(1500);
    	} catch (InterruptedException e) {
    	    e.printStackTrace(); 
    	}

    	System.out.println("예약이 취소되었습니다.");
    }
}