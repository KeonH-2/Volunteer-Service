package main.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Collections;
import main.VolunteerProgram;

public class UserPanel extends BasePanel {
    public UserPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());
        JButton myPageBtn = new JButton("마이페이지");
        add(myPageBtn, BorderLayout.NORTH);

        myPageBtn.addActionListener(e -> showMyPageDialog());
    }

    // 마이페이지(내 예약 목록 및 취소)
    private void showMyPageDialog() {
        JDialog dialog = new JDialog(mainFrame, "마이페이지", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String userId = mainFrame.getLoggedInUser().getId();
        main.VolunteerProgramManager vpm = mainFrame.getProgramManager();
        main.ReservationManager rm = mainFrame.getReservationManager();
        
        // 내 예약 목록
        List<String> myPrograms = rm.getMyReservations(userId);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String programName : myPrograms) {
            VolunteerProgram p = vpm.getProgramByName(programName);
            if (p != null) {
                listModel.addElement(
                    String.format("%s (%s, %s, %s)", p.getName(), p.getDate(), p.getLocation(), p.getCategory())
                );
            }
        }

        JList<String> programList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(programList);
        dialog.add(scrollPane, BorderLayout.CENTER);

        if (myPrograms.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "예약된 봉사 프로그램이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            return;
        }

        JPanel bottomPanel = new JPanel();
        JButton cancelBtn = new JButton("선택한 예약 취소");
        JButton closeBtn = new JButton("닫기");

        cancelBtn.addActionListener(e -> {
            int selectedIndex = programList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedProgramName = myPrograms.get(selectedIndex);
                int confirm = JOptionPane.showConfirmDialog(dialog,
                    selectedProgramName + " 예약을 취소하시겠습니까?", "예약 취소 확인",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // 선택한 예약만 리스트로 넘김
                    List<String> singleProgram = Collections.singletonList(selectedProgramName);
                    rm.cancelReservation(userId, vpm, singleProgram, mainFrame.getUserManager());
                    JOptionPane.showMessageDialog(dialog, "예약이 취소되었습니다.");
                    dialog.dispose(); // 취소 후 다이얼로그 닫기
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "예약을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        bottomPanel.add(cancelBtn);
        bottomPanel.add(closeBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
