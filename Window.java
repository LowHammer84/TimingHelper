import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;

public class Window extends JFrame {
    
    ArrayList<Program> programs;
    ArrayList<JLabel[]> labels;
    ArrayList<JProgressBar[]> progressBars;
    Program currentProgram;
    JLabel[] currentLabels;
    WarningDialog warningDialog;
    JProgressBar[] currentProgressBars;
    JComboBox programChooser;
    JPanel actionsPanel;
    JPanel mainPanel;
    JButton startButton;
    JButton stopButton;
    Timer timer;
    Timer progressTimer;
    boolean isProgramRunning = false;
    int counter;
    boolean setFull = false;
    
    
    public Window(ArrayList<Program> programs) {
        this.programs = programs;
        currentProgram = programs.get(0);
        labels = new ArrayList();
        for (int i = 0; i < programs.size(); i++) {
            JLabel[] labelsTemp = new JLabel[programs.get(i).getNumOfActions()];
            for (int j = 0; j < programs.get(i).getNumOfActions(); j++) {
                labelsTemp[j] = new JLabel(programs.get(i).getActionsNames()[j]);
                labelsTemp[j].setAlignmentX(JLabel.LEFT_ALIGNMENT);
            }
            labels.add(labelsTemp);
        }
        currentLabels = labels.get(0);

        progressBars = new ArrayList<>();
        for (int i = 0; i < programs.size(); i++) {
            JProgressBar[] progressBarsTemp = new JProgressBar[programs.get(i).getNumOfActions()];
            for (int j = 0; j < programs.get(i).getNumOfActions(); j++) {
                progressBarsTemp[j] = new JProgressBar(0, 100);
            }
            progressBars.add(progressBarsTemp);
        }
        currentProgressBars = progressBars.get(0);
    }
    
    public void create() {
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        
        JPanel comboPanel = createComboPanel();
        actionsPanel = createActionsPanel(0);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1,2, 5, 5));
        startButton = new JButton("Старт");
        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startProgram();
            }
        });
        buttonPanel.add(startButton);
        stopButton = new JButton("Стоп");
        stopButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopProgram();
            }
        });
        stopButton.setEnabled(false);
        buttonPanel.add(stopButton);
        buttonPanel.setMaximumSize(new Dimension(1700, 40));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(comboPanel);
        mainPanel.add(actionsPanel);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        JProgressBar mainProgress = new JProgressBar(0,100);
        
        bottomPanel.add(buttonPanel);
        bottomPanel.add(mainProgress);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        add(BorderLayout.SOUTH, bottomPanel);
        
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createComboPanel() {
        JPanel comboPanel = new JPanel();
        String[] programsNames = new String[programs.size()];
        for (int i = 0; i < programs.size(); i++) {
            programsNames[i] = programs.get(i).getName();
        }
        programChooser = new JComboBox(programsNames);
        programChooser.setPreferredSize(new Dimension(400, 40));
        programChooser.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLabels(programChooser.getSelectedIndex());
                currentProgram = programs.get(programChooser.getSelectedIndex());
            }
        });
        comboPanel.add(new Label("Выберите программу:"));
        comboPanel.add(programChooser);
        comboPanel.setBackground(Color.GRAY);
        comboPanel.setMaximumSize(new Dimension(1700,30));
        return comboPanel;
        
    }
    
    private JPanel createActionsPanel(int index) {
        
        int rows = programs.get(index).getNumOfActions();
        JPanel panel = new JPanel(new GridLayout(rows, 2,5,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        for (int i = 0; i < labels.get(index).length; i++) {
            panel.add(labels.get(index)[i]);
            panel.add(currentProgressBars[i]);
        }
        return panel;
    }
    
    private void updateLabels(int index) {
        
        mainPanel.remove(actionsPanel);
        actionsPanel = createActionsPanel(index);
        mainPanel.add(actionsPanel);
        mainPanel.validate();
        
    }
    
    private void startProgram() {

        timer = new Timer();
        counter = 0;
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        isProgramRunning = true;
        if (warningDialog == null) {
            warningDialog = new WarningDialog(this, "Начать выполнение программы " + currentProgram.getName() + "?");
        }
        warningDialog.setVisible(true);
    }

    private void stopProgram() {
        stopButton.setEnabled(false);
        isProgramRunning = false;
        startButton.setEnabled(true);
        progressTimer.cancel();
    }

    private void doProgram() {

        progressTimer = new Timer();
        long currentTime = currentProgram.getActionsTimes()[counter];
        long onePercent = currentTime / 100;
        progressTimer.schedule(new TimerTask() {
            int value = 0;
            @Override
            public void run() {
                currentProgressBars[counter].setValue(value++);
                if (value == 100) {
                    this.cancel();
                }
            }
        }, 0, onePercent);


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (counter < currentProgram.getNumOfActions()) {
                    warningDialog.setText(currentProgram.getActionsNames()[counter]);
                    currentProgressBars[counter].setValue(100);
                    warningDialog.setVisible(true);
                    progressTimer.cancel();
                    counter++;
                } else {
                    warningDialog.setText("Программа завершена!");
                    warningDialog.setVisible(true);
                    isProgramRunning = false;
                    warningDialog.getButton().setEnabled(false);
                }

            }
        }, currentProgram.getActionsTimes()[counter]);

    }



    class WarningDialog extends JDialog {

        JLabel dialogText;
        JButton no;

        public WarningDialog(JFrame owner, String message) {
            super(owner, "Подтвердите действие", true);

            dialogText = new JLabel(message);
            dialogText.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(dialogText, BorderLayout.CENTER);
            setLocationRelativeTo(null);

            JButton yes = new JButton("Продолжить");
            yes.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    doProgram();
                    setVisible(false);
                }
            });

            no = new JButton("Отменить");
            if (isProgramRunning = false) {
                no.setEnabled(false);
            }
            no.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopProgram();
                    dispose();
                }
            });

            JPanel panel = new JPanel();
            panel.add(yes);
            panel.add(no);
            add(panel, BorderLayout.SOUTH);
            pack();
        }
        public void setText(String message) {
            dialogText.setText(message);
        }
        public JButton getButton(){
            return no;
        }

    }
    
    
}
