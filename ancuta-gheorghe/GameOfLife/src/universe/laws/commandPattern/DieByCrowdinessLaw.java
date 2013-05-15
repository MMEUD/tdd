package universe.laws.commandPattern;

import universe.CellWorld;

/**
 * Created with IntelliJ IDEA.
 * User: Ancuta Gheorghe
 * Date: 5/14/13
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DieByCrowdinessLaw implements Law{

    private LifeCycle lifeCycle;

    public DieByCrowdinessLaw(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @Override
    public void execute() {
        lifeCycle.live();
    }
}
