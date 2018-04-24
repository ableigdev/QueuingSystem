public class Channel
{
	Channel()
	{
            m_State = 0;
            m_Request = 0;
            m_Time = 0; 
	}
	
	public void addRequest(int value, int time)
	{
            m_Request = value;
            setTime(time);
            work();        
	}
        
        public void setTime(int value)
        {
            m_Time = value > 0 ? value : 0;
        }
        
        public int getTime()
        {
            return m_Time;
        }
	
	public int removeRequest()
	{
            return m_Request;
	}
	
        public void setState(int value)
        {
            m_State = value >= 0 && value <= 2 ? value : 0;
        }
        
	public int getState()
	{
            return m_State;
	}
        
        private void work()
        {
            while (m_Time != 0)
            {
                --m_Time;
            }
        }
	
	private int m_State;
        private int m_Request;
        private int m_Time;
}