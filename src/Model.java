public class Model
{
    enum NumberSensor { SENSOR20_80, SENSOR50_150, SENSOR20_40, SENSOR40_80 } 
   
    public static void main(String[] args)
    {
        /*
        System.out.println(sen.sensor(numSensor.SENSOR20_80.ordinal()));
        System.out.println(sen.sensor(numSensor.SENSOR50_150.ordinal()));
        System.out.println(sen.sensor(numSensor.SENSOR20_40.ordinal()));
        System.out.println(sen.sensor(numSensor.SENSOR40_80.ordinal()));        
        */
        Channel channel1 = new Channel();
        Channel channel21 = new Channel();
        Channel channel22 = new Channel();
        Channel channel3 = new Channel();
        Channel channel4 = new Channel();

        StorageDevice storageDevice1 = new StorageDevice();
        StorageDevice storageDevice2 = new StorageDevice();
        StorageDevice storageDevice3 = new StorageDevice();
        
        int fullTime = 190;
        int currentTime = 0;

        NumberSensor numSensor = null;
        Sensors sen = new Sensors();
        
        // 4. ћоделирование выхода за€вки из системы
        if (channel4.getState() != 0)
        {
            if (currentTime <= fullTime)
            {
                int value = channel4.removeRequest();
                
                if (channel1.getState() == 0)
                {
                    int time = sen.sensor(numSensor.SENSOR20_80.ordinal());
                    channel1.addRequest(value, time);
                    channel1.setState(1);
                    channel4.setState(0);
                    currentTime += time;
                }
                else
                {
                    storageDevice1.addRequest(value);
                }
            }
        }
        
        // 5. ћоделирование перехода за€вки из 3 фазы в 4 фазу
        
        
        
        
        
    }
}