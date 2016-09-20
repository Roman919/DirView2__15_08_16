package UserContainer;

import java.util.Objects;

/**
 * Created by Roman on 03.07.2016.
 */
public class Queue <T > implements IQueue <T>
{
private QueueNode<T> headNode,lastNode;
    private int countNodes;

    public Queue()
    {
        countNodes=0;
        headNode=lastNode=null;

    }

    public Queue(T[] list)
    {
        countNodes=0;
        headNode=lastNode=null;
        for(T elem: list)
        {
        pushBack(elem);
        }

    }

    @Override
    public synchronized void pushBack(T data)
    {
      if(countNodes==0)
      {
          headNode = new QueueNode<>(data);
          lastNode=headNode;
      }
      else
      {
          QueueNode<T>  previouslyNode = lastNode;
          lastNode = new QueueNode<>(data);
          previouslyNode.next=lastNode;
      }
        countNodes++;
    }



    @Override
    public synchronized T pop() throws QueueEmptyException
    {
        if (countNodes>0)
        {
            QueueNode<T> popingNode = headNode;
            headNode = popingNode.next;
            countNodes--;
            return (popingNode.data);
        }
        else
        {
            throw new QueueEmptyException();
        }

    }

    @Override
    public synchronized int getSize()
    {
        return countNodes;
    }

    @Override
    public synchronized boolean isEmpty()
    {
        return (countNodes==0);

    }
}
