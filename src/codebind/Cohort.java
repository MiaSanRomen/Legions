package codebind;

import javafx.scene.shape.Circle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

public  class Cohort
{
    private int posX, posY, vecX=3, vecY=1, rightnow=0, destinationX, destinationY, steps,
            cohortPeople=100, cohortEndurance, cohortMorale=50, cohortNumber, distance, attackAbility=1;
    private  double deviation=3, direction=0;
    private static JPanel drawpanel, showpanel;
    private  Color colorCohort;
    private  CohortTypeEnum cohortType;
    private  boolean isTaken = false, isGoing=false, isMine=false, isInDanger=false;
    private  double destinationFull;
    private JLabel cohortLabel;
    private JTextField peopleField, enduranceField, moraleField;
    public Legion myLegion, enemyLegion;

    public Cohort(int x, int y, JPanel panel, CohortTypeEnum typeEnum, int number, Color color,
                  JLabel cohortLbl, JTextField peopleFld, JTextField enduranceFld, JTextField moraleFld)
    {
        posX = x;
        posY = y;
        drawpanel = panel;
        cohortType = typeEnum;
        cohortNumber = number;
        colorCohort=color;
        cohortLabel=cohortLbl;
        peopleField=peopleFld;
        enduranceField=enduranceFld;
        moraleField=moraleFld;
    }

    public int getCohortNumber() {
        return cohortNumber;
    }

    public void setDanger(boolean danger){isInDanger=danger;}

    public boolean isInDanger(){return isInDanger;}

    public int  getPosX() {return posX;}

    public int getPosY() {
        return posY;
    }

    public void setMine(Boolean my) {isMine = my;}

    public boolean isTaken() {
        return isTaken;
    }

    public int getCohortPeople() {
        return cohortPeople;
    }

    public void killPeople(double a)
    {
        cohortPeople=(int)(cohortPeople*(1-a));
        cohortMorale=(int)(cohortMorale*(1-a));
    }

    public int getCohortMorale() {
        return cohortMorale;
    }

    public void setCohortMorale(double a)
    {
        cohortMorale+=(int)(cohortMorale*a);
    }

    public void setAttackAbility(int ability){attackAbility=ability;}

    public int getAttackAbility(){return attackAbility;}

    public void refreshEndurance()
    {
        isTaken=false;
        cohortEndurance=distance;
        attackAbility=1;
        drawCohort();
    }

    public CohortTypeEnum getCohortType() {
        return cohortType;
    }

    Timer timer = new Timer("Timer");
    long delay  = 500L;
    long period = 50L;

    public void drawCohort()
    {
        Graphics g = drawpanel.getGraphics();
        Graphics2D g2d = (Graphics2D)g;
        Rectangle rect = new Rectangle(posX, posY, 60, 80);
        g2d.rotate(Math.toRadians(direction), posX+30, posY+40);
        g2d.setColor(colorCohort);
        g2d.draw(rect);
        g2d.fill(rect);
        g.setColor(Color.WHITE);
        g.fillOval(posX, posY+10, 60, 60);
        g.setColor(colorCohort);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        switch (cohortType)
        {
            case INFANTRY:
            {
                distance=50;
                g2.drawLine(posX+20, posY+40, posX+30, posY+50);
                g2.drawLine(posX+15, posY+55, posX+45, posY+25);
                break;
            }
            case CAVALRY:
            {
                distance=140;
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(posX+15, posY+55, posX+25, posY+30);
                g2.drawLine(posX+25, posY+30, posX+30, posY+25);
                g2.drawLine(posX+30, posY+25, posX+40, posY+30);
                g2.drawLine(posX+40, posY+30, posX+40, posY+35);
                g2.drawLine(posX+40, posY+35, posX+30, posY+35);
                g2.drawLine(posX+30, posY+35, posX+30, posY+50);
                g2.drawLine(posX+30, posY+50, posX+15, posY+55);
                g2.drawLine(posX+25, posY+30, posX+25, posY+20);
                g2.drawLine(posX+25, posY+20, posX+30, posY+25);
                break;
            }
            case SPEARMANS:
            {
                distance=80;
                g2.drawLine(posX+15, posY+55, posX+45, posY+25);
                g2.drawLine(posX+45, posY+25, posX+45, posY+30);
                g2.drawLine(posX+45, posY+25, posX+40, posY+25);
                break;
            }
        }
        drawBorder();
    }

    public  void clearCohort() {
        Color color = new Color(94, 187, 19);
        Graphics g = drawpanel.getGraphics();
        Graphics2D g2d = (Graphics2D)g;
        Rectangle rect = new Rectangle(posX-3, posY-3, 64, 84);
        g2d.rotate(Math.toRadians(direction), posX+30, posY+40);
        g2d.setColor(color);
        g2d.draw(rect);
        g2d.fill(rect);
    }
    /*public void rotateCohort(int x, int y)
    {
        TimerTask rotateTask = new TimerTask() {
            public void run() {
                clearCohort();
                direction = direction + deviation;
                drawCohort();
                rightnow--;
                if(rightnow<1)
                {

                    cancel();
                }
            }
        };
        destinationX=x-posX-30;
        destinationY=y-posY-40;
        destinationFull = Math.sqrt(Math.pow(destinationX, 2)+Math.pow(destinationY, 2));
        int angle=(int)Math.sin((destinationX/destinationFull));
        rightnow = angle;
        timer.scheduleAtFixedRate(rotateTask, delay, period);
    }*/

    public void moveCohort(int x, int y)
    {
        TimerTask moveTask = new TimerTask() {
            public void run() {
                if(rightnow<1 || cohortEndurance<1)
                {
                    cancel();
                    isGoing=false;
                    drawBorder();
                }
                clearCohort();
                posX=posX+destinationX/steps;
                posY=posY+destinationY/steps;
                enemyLegion.redraw();
                myLegion.redraw();
                nearEnemies();
                drawCohort();
                rightnow--;
                cohortEndurance-=Math.abs(destinationX/steps);
                cohortEndurance-=Math.abs(destinationY/steps);
                displayCohort();
            }
        };
        destinationX=x-posX-30;
        destinationY=y-posY-40;
        destinationFull = Math.sqrt(Math.pow(destinationX, 2)+Math.pow(destinationY, 2));
        steps=(int)(destinationFull/3);
        rightnow=steps;
        if(cohortEndurance>0 && !isGoing)
        {
            isGoing=true;
            timer.scheduleAtFixedRate(moveTask, delay, period);
        }
    }

    public void drawBorder()
    {
        Graphics g = drawpanel.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        if(isTaken)
            g2.setColor(Color.YELLOW);
        else if(isMine && cohortEndurance>0)
            g2.setColor(Color.GREEN);
        else if(cohortEndurance > 0)
            g2.setColor(Color.WHITE);
        else
            g2.setColor(Color.PINK);
        Rectangle rectangle = new Rectangle(posX, posY, 60, 80);
        g2.rotate(Math.toRadians(direction), posX+30, posY+40);
        g2.draw(rectangle);
    }

    public void take(int x, int y, Legion my, Legion enemy)
    {
        if (x>posX && x<posX+60 && y>posY+10 && y<posY+70)
        {
            enemy.redraw();
            my.redraw();
            isTaken=true;
            displayCohort();
            myLegion=my;
            enemyLegion=enemy;
            nearEnemies();
        }
        else {
            isTaken = false;
        }
        drawCohort();
    }

    public void displayCohort()
    {
        if(isTaken)
        {
            cohortLabel.setText(cohortType + " " + cohortNumber +". cohort");
            peopleField.setText(Integer.toString(cohortPeople));
            if (cohortEndurance<0)
                enduranceField.setText("0");
            else
                enduranceField.setText(Integer.toString(cohortEndurance));
            moraleField.setText(Integer.toString(cohortMorale));
        }
    }

    public void nearEnemies()
    {
        if(attackAbility>0) {
            for (var cohort : enemyLegion.cohorts) {
                if (posX > cohort.getPosX() - 70 && posX < cohort.getPosX() + 70 &&
                        posY > cohort.getPosY() - 90 && posY < cohort.getPosY() + 90) {
                    cohort.drawCohort();
                    cohort.setDanger(true);
                    Graphics g = drawpanel.getGraphics();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(colorCohort);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawOval(cohort.getPosX() + 20, cohort.getPosY() + 30, 20, 20);
                }

            }
        }
    }

    public void event(Point a)
    {
        for (var cohort: enemyLegion.cohorts)
        {
            if((a.x>cohort.getPosX() && a.x<cohort.getPosX() +60 &&
                    a.y>cohort.getPosY() +10 && a.y<cohort.getPosY() +70 ))
            {
                if(attackAbility>0 && cohort.isInDanger())
                {
                    enemyLegion.redraw();
                    myLegion.redraw();
                    Fight fight = new Fight(this, cohort);
                    fight.simulateFight();
                    cohortPeople=fight.attackCohort.cohortPeople;
                    cohortMorale=fight.attackCohort.cohortMorale;
                    cohort.cohortPeople=fight.defendCohort.cohortPeople;
                    cohort.cohortMorale=fight.defendCohort.cohortMorale;
                    displayCohort();
                    attackAbility=0;
                }
                return;
            }
        }
        moveCohort(a.x, a.y);
    }

   public void retreat()
    {
        if(cohortMorale<-10)
        {
            colorCohort=Color.WHITE;
            period = 10L;
            moveCohort(posX, 1000);
        }
    }
}
