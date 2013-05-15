package universe.laws.commandPattern;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Ancuta Gheorghe
 * Date: 5/14/13
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
//Invoker class
public class Agent {

    private ArrayList<Law> lawsQueue = new ArrayList<Law>();

    public Agent(){

    }

    public void addLaw(Law law){
        lawsQueue.add(law);
        law.execute();
    }

}
