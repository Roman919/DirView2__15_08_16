package UserContainer;

/**
 * Created by Roman on 03.07.2016.
 */
public class QueueEmptyException extends Exception
{
    public String toString() {
    return "\nQueue is empty.";
}
}
