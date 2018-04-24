import java.util.*;

public class StorageDevice
{
    StorageDevice()
    {
        m_State = new LinkedList<>();
    }

    public void addRequest(int value)
    {
        m_State.offer(value);
    }

    public int removeRequest()
    {
        return m_State.remove();
    }

    public int getSize()
    {
        return m_State.size();
    }
    
    private Queue<Integer> m_State;
}