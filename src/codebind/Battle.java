package codebind;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Battle {
    private ArrayList<Legion> legions = new ArrayList<>();
    GeneralEnum leftGeneral, rightGeneral;
    private JLabel cohortLabel;
    private JTextField peopleField, enduranceField, moraleField;
    public Battle(GeneralEnum myGeneral, GeneralEnum enemyGeneral,
                  JLabel cohortLbl, JTextField peopleFld, JTextField enduranceFld, JTextField moraleFld)
    {
        cohortLabel=cohortLbl;
        peopleField=peopleFld;
        enduranceField=enduranceFld;
        moraleField=moraleFld;
        leftGeneral=myGeneral;
        rightGeneral=enemyGeneral;
    }

    public void start(JPanel drawpanel, JButton btn)
    {
        for (var legionIn: legions)
        {
            for (var cohort: legionIn.cohorts)
            {
                cohort.clearCohort();
            }
        }
        legions.clear();
        Legion legion = new Legion(leftGeneral, PlayerEnum.LEFT, drawpanel, false,
                cohortLabel, peopleField, enduranceField, moraleField);
        legions.add(legion);
        legion = new Legion(rightGeneral, PlayerEnum.RIGHT, drawpanel, true,
                cohortLabel, peopleField, enduranceField, moraleField);
        legions.add(legion);
        for (var legionIn: legions)
        {
            legionIn.drawLegion();
        }
        nextTurn(btn);
    }

    public void nextTurn(JButton btn)
    {
        for (var legion: legions)
        {
            if(legion.isTaken())
            {
                legion.isTaken = false;
                for (var cohort : legion.cohorts) {
                    cohort.setMine(false);
                    cohort.refreshEndurance();
                }
            }
            else
            {
                legion.isTaken=true;
                for (var cohort: legion.cohorts) {
                    cohort.setMine(true);
                    cohort.refreshEndurance();
                }
            }
        }
        for (var legion: legions)
        {
            if(legion.cohorts.size()<1)
            {
                for (var leg : legions) {
                    if (leg.cohorts.size()>0)
                    {
                        btn.setEnabled(false);
                        btn.setText("GENERAL "+leg.general + " WON!");
                    }
                }
            }
        }
    }

    public void control(MouseEvent e)
    {
        Legion enemy = new Legion();
        for (var legion: legions)
        {
            if(legion.isTaken()) {
                for (var leg : legions) {
                    if (!leg.isTaken())
                        enemy = leg;
                }
                legion.control(e, enemy);
                for (var leg : legions) {
                    if (!leg.isTaken())
                        leg = legion.enemyLegion;
                }
            }
        }
    }
}

