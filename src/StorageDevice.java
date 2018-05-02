import java.util.*;

public class StorageDevice
{
    StorageDevice()
    {
        m_State = new LinkedList<>();
        m_CounterRequest = 0;
    }

    public void addRequest(int value)
    {
        m_State.offer(value);
        m_CounterRequest = m_State.size() > m_CounterRequest ? m_State.size() : m_CounterRequest;
    }

    public int removeRequest()
    {
        return m_State.remove();
    }

    public int getSize()
    {
        return m_State.size();
    }
    
    public int counterRequest()
    {
        return m_CounterRequest;
    }
    
    private Queue<Integer> m_State;
    private int m_CounterRequest;
}