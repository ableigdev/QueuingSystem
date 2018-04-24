public class Request 
{
    Request()
    {
        m_State = false;
        m_Time = 0;
    }
    
    public void setState(boolean value)
    {
        m_State = value;
    }
    
    public boolean getState()
    {
        return m_State;
    }
    
    public void setTime(int value)
    {
        m_Time = value > 0 ? value : 0;
    }
    
    public int getTime()
    {
        return m_Time;
    }
    
    public void updateTime(int value)
    {
        m_Time += value;
    }
    
    private boolean m_State;
    private int m_Time;
}
