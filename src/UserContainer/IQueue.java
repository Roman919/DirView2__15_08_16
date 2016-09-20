package UserContainer;

/**
 * Created by Roman on 03.07.2016.
 */
public interface IQueue <T>
{
    // поместить символ в очередь
    void pushBack(T data);
    // извлечь символ из очереди
    T pop() throws QueueEmptyException;
    int getSize();
    boolean isEmpty();
}
