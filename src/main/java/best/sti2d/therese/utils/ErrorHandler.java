package best.sti2d.therese.utils;

import best.sti2d.therese.Therese;

import java.util.Arrays;

public class ErrorHandler {

    private final Therese therese;

    public ErrorHandler() {
        this.therese = Therese.getInstance();
    }

    public void handleException(Throwable exception){
        therese.getLogger().error("Une erreur est survenue !\n"+exception.getMessage());
        exception.printStackTrace();
        therese.getLogger().error(exception.getMessage()+"\n"+Arrays.toString(exception.getStackTrace()), false);
    }



}
