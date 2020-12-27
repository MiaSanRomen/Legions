package codebind;

import javax.swing.*;
import java.awt.event.*;

public class App {
    private JPanel panelMain;
    private JButton buttonGame;
    private JButton buttonTurn;
    public JPanel drawpanel;
    private JButton buttonStart;
    private JTextField enduranceField;
    private JTextField moraleField;
    private JTextField peopleField;
    public JLabel cohortLabel;
    private JComboBox cbLeft;
    private JComboBox cbRight;
    public Battle battle;

    public App()
    {
        buttonGame.addActionListener(actionEvent -> {
            cbReady(cbLeft);
            cbReady(cbRight);
            buttonStart.setEnabled(true);
        });
        buttonStart.addActionListener(actionEvent -> {
            if(cbLeft.getSelectedItem()!=cbRight.getSelectedItem())
            {
                battle = new Battle((GeneralEnum) cbLeft.getSelectedItem(), (GeneralEnum)cbRight.getSelectedItem(),
                        cohortLabel, peopleField, enduranceField, moraleField);
                battle.start(drawpanel, buttonTurn);
                buttonTurn.setEnabled(true);
            }
        });
        buttonTurn.addActionListener(actionEvent -> battle.nextTurn(buttonTurn));

        drawpanel.addMouseListener(new MouseAdapter()  {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                battle.control(e);
            }
        });

    }

    public void cbReady(JComboBox comboBox)
    {
        comboBox.setModel(new DefaultComboBoxModel<>(GeneralEnum.values()));
        comboBox.setEnabled(true);
    }

    public static void main(String[] args)
    {
        JFrame jFrame = new JFrame("App");
        jFrame.setContentPane(new App().panelMain);
        jFrame.setSize(900, 600);
        jFrame.setVisible(true);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}


