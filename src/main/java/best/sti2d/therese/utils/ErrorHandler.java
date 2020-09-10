package best.sti2d.therese.utils;

import best.sti2d.therese.Therese;

import java.util.Arrays;
import java.util.logging.Level;

public class ErrorHandler {

    private final Therese therese;

    public ErrorHandler() {
        this.therese = Therese.getInstance();
    }

    public void handleException(Throwable exception){
        therese.getLogger().log(Level.SEVERE, "Une erreur est survenue !\n"+exception.getMessage());
        exception.printStackTrace();
        therese.getLogger().log(Level.SEVERE, exception.getMessage()+"\n"+Arrays.toString(exception.getStackTrace()), false);
    }



}
