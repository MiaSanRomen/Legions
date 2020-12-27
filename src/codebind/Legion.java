package codebind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Legion {
    GeneralEnum general;
    ArrayList<Cohort> cohorts = new ArrayList<>();
    Color colorLegion;
    PlayerEnum player;
    private static JPanel drawpanel;
    protected boolean isTaken;
    private JLabel cohortLabel;
    private JTextField peopleField, enduranceField, moraleField;
    public Legion enemyLegion;

    public boolean isTaken() {
        return isTaken;
    }

    public Legion()
    {

    }

    public Legion(GeneralEnum generalEnum, PlayerEnum playerEnum, JPanel panel, boolean isSelected,
                  JLabel cohortLbl, JTextField peopleFld, JTextField enduranceFld, JTextField moraleFld)
    {
        cohortLabel=cohortLbl;
        peopleField=peopleFld;
        enduranceField=enduranceFld;
        moraleField=moraleFld;
        isTaken=isSelected;
        drawpanel=panel;
        general=generalEnum;
        player=playerEnum;
        switch (general)
        {
            case CAESAR:
            {
                colorLegion=Color.RED;
                break;
            }
            case POMPEY:
            {
                colorLegion=Color.BLUE;
                break;
            }
            case CRASSUS:
            {
                colorLegion=Color.GRAY;
                break;
            }
        }
        if (player==PlayerEnum.LEFT)
            createCohorts(drawpanel);
        else
            createEnemyCohorts(drawpanel);
    }

    public void drawLegion()
    {
        for (var cohort: cohorts)
        {
            cohort.drawCohort();
        }
    }

    public void takeCohort(int x, int y, Legion enemy)
    {
        for (var cohort: cohorts)
        {
            cohort.take(x, y, this, enemy);
        }
    }

    public void redraw()
    {
        for (Cohort cohort: cohorts)
        {
            cohort.drawCohort();
            cohort.setDanger(false);
        }
    }

    public void createCohorts(JPanel panel)
    {
        Cohort cohort = new Cohort(100, 100, panel, CohortTypeEnum.CAVALRY, 1, colorLegion,
                cohortLabel, peopleField, enduranceField, moraleField);
        cohorts.add(cohort);
        cohort = new Cohort(100, 200, panel, CohortTypeEnum.INFANTRY, 1, colorLegion,
                cohortLabel, peopleField, enduranceField, moraleField);
        cohorts.add(cohort);
        cohort = new Cohort(100, 300, panel, CohortTypeEnum.INFANTRY, 2, colorLegion,
                cohortLabel, peopleField, enduranceField, moraleField);
        cohorts.add(cohort);
        cohort = new Cohort(100, 400, panel, CohortTypeEnum.SPEARMANS, 1, colorLegion,
                cohortLabel, peopleField, enduranceField, moraleField);
        cohorts.add(cohort);
    }

    public void createEnemyCohorts(JPanel panel)
    {
        Cohort cohort = new Cohort(500, 100, panel, CohortTypeEnum.CAVALRY, 1, colorLegion,
                cohortLabel, peopleField, enduranceField, moraleField);
        cohorts.add(cohort);
        cohort = new Cohort(500, 200, panel, CohortTypeEnum.INFANTRY, 1, colorLegion,
                cohortLabel, peopleField, enduranceField, moraleField);
        cohorts.add(cohort);
        cohort = new Cohort(500, 300, panel, CohortTypeEnum.INFANTRY, 2, colorLegion,
                cohortLabel, peopleField, enduranceField, moraleField);
        cohorts.add(cohort);
        cohort = new Cohort(500, 400, panel, CohortTypeEnum.SPEARMANS, 1, colorLegion,
                cohortLabel, peopleField, enduranceField, moraleField);
        cohorts.add(cohort);
    }

    public void control(MouseEvent e, Legion enemy)
    {
        var a = e.getLocationOnScreen();
        a.x-=177;
        a.y-=31;
        //Graphics g = drawpanel.getGraphics();
        //g.fillRect(a.x, a.y, 5, 5);
        enemyLegion=enemy;


        if(e.getButton()==MouseEvent.BUTTON1)
            takeCohort(a.x, a.y, enemyLegion);
        if(e.getButton()==MouseEvent.BUTTON3)
        {
            for (var cohort: cohorts)
            {
                if(cohort.isTaken())
                {
                    cohort.event(a);
                    enemyLegion=cohort.enemyLegion;
                    enemyLegion.aliveCohort();
                    aliveCohort();
                    enemyLegion.braveCohort();
                }
            }
        }
    }

    public void aliveCohort()
    {
        for (var cohort: cohorts)
        {
            if(cohort.getCohortPeople()<1)
            {
                cohort.clearCohort();
                cohorts.remove(cohort);
                enemyLegion.redraw();
                redraw();
            }
        }
    }

    public void braveCohort()
    {
        cohorts.removeIf(cohort -> cohort.getPosY() > 600);
        for (var cohort: cohorts)
        {
            cohort.retreat();
        }
    }
}
