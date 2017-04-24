import java.util.ArrayList;

public class Main {
    
    private static final long MINUTE = 1000 * 60;
    
    public static void main(String[] args) {
        
        String[] actions1 = {"Задание 1","Задание 2","Задание 3","Задание 4","Задание 5"};
        String[] actions2 = {"Залить компоненты","Добавить медь","Добавить воду","Перемешать","Выключить"};
        String[] actions3 = {"Включить","Добавить","Выключить","Перемешать","Убрать"};
        
        long[] times = {MINUTE * 1, MINUTE * 1, MINUTE * 1, MINUTE * 1, MINUTE * 1};
        
        Program program1 = new Program("Программа № 1");
        program1.setActionsNames(actions1);
        program1.setActionsTimes(times);
        
        Program program2 = new Program("Программа № 2");
        program2.setActionsNames(actions2);
        program2.setActionsTimes(times);
    
        Program program3 = new Program("Программа № 3");
        program3.setActionsNames(actions3);
        program3.setActionsTimes(times);
    
        ArrayList<Program> programs = new ArrayList<>();
        programs.add(program1);
        programs.add(program2);
        programs.add(program3);
        
        Window window = new Window(programs);
        window.create();
        
    }

    
}
