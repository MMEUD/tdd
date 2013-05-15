package universe.laws.commandPattern;

import universe.CellWorld;

/**
 * Created with IntelliJ IDEA.
 * User: Ancuta Gheorghe
 * Date: 5/14/13
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DieByLonelinessLaw implements Law {

    private LifeCycle lifeCycle;

    public DieByLonelinessLaw(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @Override
    public void execute() {
        lifeCycle.die();
    }
}
