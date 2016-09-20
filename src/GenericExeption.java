/**
 * Created by Roman on 07.08.2016.
 */
public class GenericExeption extends Exception
{
    public GenericExeption( String exc)
    {
        message = exc;
    }


    @Override
    public String toString() {
        return message;
    }

    private String message;
}
