public class Channel
{
	Channel()
	{
            m_State = m_State.Free;
            m_Request = null;
            m_Time = 0; 
            m_WorkTime = 0;
            m_CounterRequest = 0;
            m_FullTimeRequest = 0;
	}
	
	public void addRequest(Request value, int time)
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
            m_WorkTime = m_Time;
        }
        
        public int getTime()
        {
            return m_Time;
        }
        
        public int getWorkTime()
        {
            return m_WorkTime;
        }
	
	public Request removeRequest()
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
        private Request m_Request;
        private int m_Time;
        private int m_WorkTime;
        private int m_CounterRequest;
        private int m_FullTimeRequest;
}