package codebind;

public class Fight {
    public Cohort attackCohort, defendCohort;
    public Fight(Cohort attackCoh, Cohort defendCoh)
    {
        attackCohort=attackCoh;
        defendCohort=defendCoh;
    }

    public void simulateFight()
    {
        double deathChanceAt = (double)attackCohort.getCohortPeople()/(attackCohort.getCohortPeople()+defendCohort.getCohortPeople())*
                (double)attackCohort.getCohortMorale()/(attackCohort.getCohortMorale()+defendCohort.getCohortMorale());
        double deathChanceDef = (double)defendCohort.getCohortPeople()/(attackCohort.getCohortPeople()+defendCohort.getCohortPeople())*
                (double)defendCohort.getCohortMorale()/(attackCohort.getCohortMorale()+defendCohort.getCohortMorale());
        attackCohort.killPeople(deathChanceDef);
        attackCohort.setCohortMorale(deathChanceDef);
        defendCohort.killPeople(deathChanceAt);
        defendCohort.setCohortMorale(deathChanceAt);
    }
}
