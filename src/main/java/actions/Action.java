
package actions;

public interface Action {

    void execute();   // انجام اکشن
    void undo();      // برگرداندن اکشن
    ActionType getType();
}
