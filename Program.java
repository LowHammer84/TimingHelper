public class Program {
    
    private String name;
    private int numOfActions;
    private String[] actionsNames;
    private long[] actionsTimes;
    
    public Program(String name) {
        this.name = name;
    }
    
    public void setActionsNames(String[] actionsNames) {
        this.actionsNames = actionsNames;
        numOfActions = actionsNames.length;
    }
    
    public void setActionsTimes(long[] actionsTimes) {
        this.actionsTimes = actionsTimes;
    }
    
    public String getName() {
        return name;
    }
    
    public String[] getActionsNames() {
        return actionsNames;
    }
    
    public long[] getActionsTimes() {
        return actionsTimes;
    }
    
    public int getNumOfActions() {
        return numOfActions;
    }
}
