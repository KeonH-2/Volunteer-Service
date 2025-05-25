package main.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import main.VolunteerProgram;

public class UserPanel extends BasePanel {
    public UserPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
        initializePanel();
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());
        JButton myPageBtn = new JButton("마이페이지");
        add(myPageBtn, BorderLayout.NORTH);

        myPageBtn.addActionListener(e -> showMyPageDialog());
    }

    // 마이페이지(내 예약/대기 목록 및 취소)
    private void showMyPageDialog() {
        JDialog dialog = new JDialog(mainFrame, "마이페이지", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String userId = mainFrame.getLoggedInUser().getId();
        main.VolunteerProgramManager vpm = mainFrame.getProgramManager();
        main.ReservationManager rm = mainFrame.getReservationManager();

        // 내 예약/대기 목록
        List<String> myReservations = rm.getMyReservations(userId);
        List<String> myWaiting = rm.getMyWaiting(userId);

        // 실제 프로그램명 리스트(인덱스 매핑용)
        List<String> myPrograms = new ArrayList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        // 예약
        for (String programName : myReservations) {
            VolunteerProgram p = vpm.getProgramByName(programName);
            if (p != null) {
                listModel.addElement(
                    String.format("%s (%s, %s, %s) [예약]", p.getName(), p.getDate(), p.getLocation(), p.getCategory())
                );
                myPrograms.add(p.getName());
            }
        }
        // 대기
        for (String programName : myWaiting) {
            VolunteerProgram p = vpm.getProgramByName(programName);
            if (p != null) {
                listModel.addElement(
                    String.format("%s (%s, %s, %s) [대기]", p.getName(), p.getDate(), p.getLocation(), p.getCategory())
                );
                myPrograms.add(p.getName());
            }
        }

        JList<String> programList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(programList);
        dialog.add(scrollPane, BorderLayout.CENTER);

        if (listModel.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "예약/대기 중인 봉사 프로그램이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            return;
        }

        JPanel bottomPanel = new JPanel();
        JButton cancelBtn = new JButton("선택한 예약/대기 취소");
        JButton closeBtn = new JButton("닫기");

        cancelBtn.addActionListener(e -> {
            int selectedIndex = programList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedDisplay = listModel.getElementAt(selectedIndex);
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        selectedDisplay + "을(를) 취소하시겠습니까?", "예약/대기 취소 확인",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // 선택한 예약만 리스트로 만들어 넘김
                    List<String> singleProgram = new ArrayList<>();
                    singleProgram.add(myPrograms.get(selectedIndex));
                    boolean cancelled = rm.cancelReservation(userId, vpm, singleProgram, userManager);
                    if (cancelled) {
                        JOptionPane.showMessageDialog(dialog, "취소가 완료되었습니다.");
                        listModel.remove(selectedIndex);
                        myPrograms.remove(selectedIndex);
                        if (listModel.isEmpty()) dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "취소에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "예약이나 대기 내역을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        bottomPanel.add(cancelBtn);
        bottomPanel.add(closeBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
