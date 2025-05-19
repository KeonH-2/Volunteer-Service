// package main; // 삭제

import java.io.*;
import java.util.*;

public class ReservationManager {
    private List<Reservation> reservations;
    private static final String RESERVATION_FILE = "reservations.txt";

    public ReservationManager() {
        reservations = new ArrayList<>();
    }

    public void loadReservations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String userId = parts[0];
                    String programName = parts[1];
                    boolean confirmed = Boolean.parseBoolean(parts[2]);
                    reservations.add(new Reservation(userId, programName, confirmed));
                }
            }
        } catch (IOException e) {
            System.out.println("예약 데이터 로드 실패: " + e.getMessage());
        }
    }

    public void saveReservations() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESERVATION_FILE))) {
            for (Reservation reservation : reservations) {
                writer.println(String.format("%s,%s,%b",
                    reservation.getUserId(), reservation.getProgramName(), reservation.isConfirmed()));
            }
        } catch (IOException e) {
            System.out.println("예약 데이터 저장 실패: " + e.getMessage());
        }
    }

    public boolean makeReservation(String userId, String programName, VolunteerProgramManager programManager) {
        // 중복 예약 체크
        for (Reservation r : reservations) {
            if (r.getUserId().equals(userId) && r.getProgramName().equals(programName)) {
                return false;
            }
        }
        
        // 프로그램 인원 체크
        int currentReservations = 0;
        for (Reservation r : reservations) {
            if (r.getProgramName().equals(programName)) {
                currentReservations++;
            }
        }
        
        VolunteerProgram program = null;
        for (VolunteerProgram p : programManager.getPrograms()) {
            if (p.getName().equals(programName)) {
                program = p;
                break;
            }
        }
        
        if (program != null && currentReservations < program.getMaxParticipants()) {
            reservations.add(new Reservation(userId, programName, false));
            saveReservations();
            return true;
        }
        return false;
    }

    public void confirmReservations(VolunteerProgramManager programManager, UserManager userManager) {
        for (Reservation r : reservations) {
            if (!r.isConfirmed()) {
                VolunteerProgram program = null;
                for (VolunteerProgram p : programManager.getPrograms()) {
                    if (p.getName().equals(r.getProgramName())) {
                        program = p;
                        break;
                    }
                }
                if (program != null) {
                    User user = userManager.getUserById(r.getUserId());
                    if (user != null) {
                        user.addNotification("[" + program.getName() + "] 봉사 프로그램이 승인되었습니다.");
                        r.setConfirmed(true);
                    }
                }
            }
        }
        saveReservations();
    }

    public List<Reservation> getMyReservations(String userId) {
        List<Reservation> myReservations = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getUserId().equals(userId)) {
                myReservations.add(r);
            }
        }
        return myReservations;
    }

    public boolean cancelReservation(String userId, String programName) {
        for (Reservation r : reservations) {
            if (r.getUserId().equals(userId) && r.getProgramName().equals(programName)) {
                reservations.remove(r);
                saveReservations();
                return true;
            }
        }
        return false;
    }
}