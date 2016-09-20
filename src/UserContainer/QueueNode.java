package UserContainer;

/**
 * Created by Roman on 03.07.2016.
 */
public class QueueNode <T>
{
    T data;
    QueueNode<T> next;

    public QueueNode(T data) {

        this.data = data;
        this.next = null;
    }
}
