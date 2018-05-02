public class Channel
{
	Channel()
	{
            m_State = m_State.Free;
            m_Request = 0;
            m_Time = 0; 
            m_CounterRequest = 0;
            m_FullTimeRequest = 0;
	}
	
	public void addRequest(int value, int time)
	{
            m_Request = value;
            setTime(time);
            work();  
            ++m_CounterRequest;
            m_FullTimeRequest += time;
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
	
        public void setState(ChannelState value)
        {
            m_State = value;
        }
        
	public ChannelState getState()
	{
            return m_State;
	}
        
        public int getCounterRequest()
        {
            return m_CounterRequest;
        }
        
        public int getFullTimeRequest()
        {
            return m_FullTimeRequest;
        }
        
        private void work()
        {
            while (m_Time != 0)
            {
                --m_Time;
            }
        }
	
	private ChannelState m_State;
        private int m_Request;
        private int m_Time;
        private int m_CounterRequest;
        private int m_FullTimeRequest;
}