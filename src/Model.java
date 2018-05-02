import java.util.Vector;

enum ChannelState { Free, Work, Lock }

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
        
        Vector<Channel> channel2 = new Vector();
        channel2.add(channel21);
        channel2.add(channel22);
        
        int fullTime = 250;
        int currentTime = 0;
        int counterRequest = 0;

        NumberSensor numSensor = null;
        Sensors sen = new Sensors();
        ChannelState state = null;
        
        channel1.addRequest(1, 60);
        channel1.setState(state.Work);
        
        for (int z = 0; z < 100; ++z)
        {
            currentTime = 0;
            while (currentTime <= fullTime)
            {
                // 4. Моделирование выхода заявки из системы
                if (channel4.getState() != state.Free)
                {
                    if (currentTime <= fullTime)
                    {
                        int value = channel4.removeRequest();
                        System.out.println("Время обслуживание заявки: " + currentTime);
                        if (channel1.getState() == state.Free)
                        {
                            int time = sen.sensor(numSensor.SENSOR20_80.ordinal());
                            channel1.addRequest(value, time);
                            channel1.setState(state.Work);
                            channel4.setState(state.Free);
                            currentTime += time;
                        }
                        else
                        {
                            storageDevice1.addRequest(value);
                        }
                        System.out.println("Обработано заявок: " + ++counterRequest);
                        System.out.println("Iter: " + z);
                        currentTime = 0;
                    }
                }

                // 5. Моделирование перехода заявки из 3 фазы в 4 фазу
                if (channel3.getState() != state.Free)
                {
                    if (currentTime <= fullTime)
                    {
                        if (channel4.getState() == state.Free)
                        {
                            int value = channel3.removeRequest();
                            int time = sen.sensor(numSensor.SENSOR40_80.ordinal());
                            channel4.addRequest(value, time);
                            channel4.setState(state.Work);
                            channel3.setState(state.Free);
                            currentTime += time;
                        }
                        else
                        {
                            channel3.setState(state.Lock);
                        }
                    }
                }

                // 6. Обслуживание заявки в 3 фазе
                if (storageDevice3.getSize() > 0)
                {
                    if (channel3.getState() == state.Free)
                    {
                        int value = storageDevice3.removeRequest();
                        int time = sen.sensor(numSensor.SENSOR20_40.ordinal());
                        channel3.addRequest(value, time);
                        channel3.setState(state.Work);
                        currentTime += time;
                    }
                }

                // 7. Моделирование перехода из 2 в 3 фазу
                for (int i = 0; i < channel2.size(); ++i)
                {
                    if (channel2.get(i).getState() != state.Free)
                    {
                        if (currentTime <= fullTime)
                        {
                            if (channel3.getState() == state.Free)
                            {
                                int value = channel2.get(i).removeRequest();
                                int time = sen.sensor(numSensor.SENSOR20_40.ordinal());
                                channel3.addRequest(value, time);
                                channel3.setState(state.Work);
                                channel2.get(i).setState(state.Free);
                                currentTime += time;
                            }
                            else
                            {
                                storageDevice3.addRequest(channel2.get(i).removeRequest());
                                channel2.get(i).setState(state.Free);
                            }
                        }
                    }
                }

                // 8. Обслуживание заявки во 2 фазе
                if (storageDevice2.getSize() > 0)
                {
                    for (int i = 0; i < channel2.size(); ++i)
                    {
                        if (channel2.get(i).getState() == state.Free)
                        {
                            int value = storageDevice2.removeRequest();
                            int time = sen.sensor(numSensor.SENSOR50_150.ordinal());
                            channel2.get(i).addRequest(value, time);
                            channel2.get(i).setState(state.Work);
                            currentTime += time;
                        }
                    }
                }

                // 9. Моделирование перехода заявки из 1 во 2 фазу
                if (channel1.getState() != state.Free)
                {
                    if (currentTime <= fullTime)
                    {
                        boolean flag = false;
                        for (int i = 0; i < channel2.size(); ++i)
                        {
                            if (channel2.get(i).getState() == state.Free)
                            {
                                int value = channel1.removeRequest();
                                int time = sen.sensor(numSensor.SENSOR50_150.ordinal());
                                channel2.get(i).addRequest(value, time);
                                channel2.get(i).setState(state.Work);
                                channel1.setState(state.Free);
                                currentTime += time;
                                flag = true;
                                break;
                            }
                        }

                        if (!flag)
                        {
                            storageDevice2.addRequest(channel1.removeRequest());
                            channel1.setState(state.Free);
                        }
                    }
                }
            }
        
        }
        
        System.out.println("Количество обработанных заявок: " + counterRequest);
        System.out.println("Количество обработанныз заявок в 1-ом канале: " + channel1.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 1-ом канале: " + channel1.getFullTimeRequest() / channel1.getCounterRequest());
        
        for (int i = 0; i < channel2.size(); ++i)
        {
            System.out.println("Количество обработанныз заявок в 2-ом канале: " + channel2.get(i).getCounterRequest());
            System.out.println("Среднее время обработки заявки в 2-ом канале: " + channel2.get(i).getFullTimeRequest() / channel1.getCounterRequest());
        }
        
        System.out.println("Количество обработанныз заявок в 3-ом канале: " + channel3.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 3-ом канале: " + channel3.getFullTimeRequest() / channel3.getCounterRequest());
        
        System.out.println("Количество обработанныз заявок в 4-ом канале: " + channel4.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 4-ом канале: " + channel4.getFullTimeRequest() / channel4.getCounterRequest());
        
        System.out.println("Количество заявок в 1 накопителе: " + storageDevice1.counterRequest());
        System.out.println("Количество заявок в 2 накопителе: " + storageDevice2.counterRequest());
        System.out.println("Количество заявок в 3 накопителе: " + storageDevice3.counterRequest());
    }
}