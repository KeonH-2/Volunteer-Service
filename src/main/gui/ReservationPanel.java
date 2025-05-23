package main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import main.VolunteerProgram;

public class ReservationPanel extends BasePanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public ReservationPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());
        String[] columns = {"프로그램명", "날짜", "상태"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton cancelBtn = new JButton("예약취소");
        JButton refreshBtn = new JButton("새로고침");
        JButton backBtn = new JButton("메인으로");

        JPanel btnPanel = new JPanel();
        btnPanel.add(cancelBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> cancelReservation());
        refreshBtn.addActionListener(e -> refreshTable());
        backBtn.addActionListener(e -> mainFrame.showMainMenuPanel());
    }

    @Override
    public void onShow() {
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        if (loggedInUser == null) {
            showMessage("로그인 후 이용하세요.");
            return;
        }

        // 예약 목록
        List<String> myPrograms = null;
        List<String> myWaiting = null;
        try {
            myPrograms = reservationManager.getMyReservations(loggedInUser.getId());
            myWaiting = reservationManager.getMyWaiting(loggedInUser.getId());
        } catch (Exception e) {
            showMessage("예약 정보를 불러올 수 없습니다.");
            return;
        }

        if (myPrograms != null) {
            for (String prog : myPrograms) {
                VolunteerProgram p = programManager.getProgramByName(prog);
                if (p != null) {
                    tableModel.addRow(new Object[]{p.getName(), p.getDate(), "예약"});
                }
            }
        }
        if (myWaiting != null) {
            for (String prog : myWaiting) {
                VolunteerProgram p = programManager.getProgramByName(prog);
                if (p != null) {
                    tableModel.addRow(new Object[]{p.getName(), p.getDate(), "대기"});
                }
            }
        }
    }

    private void cancelReservation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showMessage("취소할 예약을 선택하세요.");
            return;
        }
        if (loggedInUser == null) {
            showMessage("로그인 후 이용하세요.");
            return;
        }
        String prog = (String) tableModel.getValueAt(row, 0);
        boolean ok = false;
        try {
            ok = reservationManager.cancelReservation(loggedInUser.getId(), prog);
        } catch (Exception e) {
            showMessage("예약 취소 중 오류가 발생했습니다.");
            return;
        }
        if (ok) {
            showMessage("예약이 취소되었습니다.");
            refreshTable();
        } else {
            showMessage("취소 실패(이미 확정되었거나 없는 예약)");
        }
    }
}
