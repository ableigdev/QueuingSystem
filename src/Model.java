import java.util.Vector;
import java.io.*;
import java.math.*;

enum ChannelState { Free, Work, Lock }

public class Model
{
    enum NumberSensor { SENSOR20_80, SENSOR50_150, SENSOR20_40, SENSOR40_80 }
    
   
    public static void main(String[] args)
    {
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
        
        int fullTime = 245;
        int currentTime = 0;
        int counterRequest = 0;
        int fullTimeSystem = 0;

        NumberSensor numSensor = null;
        Sensors sen = new Sensors();
        ChannelState state = null; 
        WriteIntoFile writer = new WriteIntoFile();
        WritelntoFile writer1 = new WritelntoFile();
        
        System.out.println("Выберите режим работы: ");
        System.out.println("0 - Статический;\n1 - Случайный");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int variant = 0;
        try
        {
            String variantStr = reader.readLine();
            
            while (!variantStr.equals("0") && !variantStr.equals("1"))
            {
                System.out.println("Некорректный ввод!");
                System.out.println("Выберите режим работы: ");
                System.out.println("0 - Статический;\n1 - Случайный");
                variantStr = reader.readLine();
            }
            
            variant = Integer.parseInt(variantStr);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
            if (reader != null)
                reader.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        
        if (variant == 1)
        {
            Request firstRequest = new Request();
            firstRequest.setTime(60);
            channel1.addRequest(firstRequest, 60);
            channel1.setState(state.Work);
            ++counterRequest;
        }
        
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
                        Request value = channel4.removeRequest();
                        fullTimeSystem += currentTime;
                        if (variant == 1)
                        {
                            if (channel1.getState() == state.Free)
                            {
                                int time = sen.sensor(numSensor.SENSOR20_80.ordinal());
                                value.setTime(0);
                                value.updateTime(time);
                                channel1.addRequest(value, time);
                                channel1.setState(state.Work);
                                channel4.setState(state.Free);
                                currentTime += time;
                            }
                            else
                            {
                                storageDevice1.addRequest(value);
                            }
                            currentTime = 0;
                        }
                        else
                        {
                            channel4.setState(state.Free);
                        }
                        ++counterRequest;
                        
                    }
                }

                // 5. Моделирование перехода заявки из 3 фазы в 4 фазу
                if (channel3.getState() != state.Free)
                {
                    if (currentTime <= fullTime)
                    {
                        if (channel4.getState() == state.Free)
                        {
                            Request value = channel3.removeRequest();
                            int time = variant == 1 ? sen.sensor(numSensor.SENSOR40_80.ordinal()) : 60;
                            value.updateTime(time);
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
                        Request value = storageDevice3.removeRequest();
                        int time = variant == 1 ? sen.sensor(numSensor.SENSOR20_40.ordinal()) : 30;
                        value.updateTime(time);
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
                                Request value = channel2.get(i).removeRequest();
                                int time = variant == 1 ? sen.sensor(numSensor.SENSOR20_40.ordinal()) : 30; 
                                value.updateTime(time);
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
                            Request value = storageDevice2.removeRequest();
                            int time = variant == 1 ? sen.sensor(numSensor.SENSOR50_150.ordinal()) : 100;
                            value.updateTime(time);
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
                                Request value = channel1.removeRequest();
                                int time = variant == 1 ? sen.sensor(numSensor.SENSOR50_150.ordinal()) : 100;
                                value.updateTime(time);
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
                
                if (variant == 0)
                {
                    Request newRequest = new Request();
                    newRequest.setTime(50);
                    if (channel1.getState() == state.Free)
                    {
                        channel1.addRequest(newRequest, 50);
                        channel1.setState(state.Work);
                    }
                    else
                    {
                        storageDevice1.addRequest(newRequest);
                    }
                }
            }
            
            //System.out.println("Количество заявок в системе: " + Math.abs(counterRequest - channel1.getCounterRequest()));
            writer1.write(Math.abs(counterRequest - channel1.getCounterRequest()));
        }
        
        System.out.println("Количество обработанных заявок: " + counterRequest + "\n");
        
        System.out.println("Количество обработанных заявок в 1-ом канале: " + channel1.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 1-ом канале: " + channel1.getFullTimeRequest() / channel1.getCounterRequest());
        System.out.printf("Процент загруженности канала: %8.2f", (double)channel1.getFullTimeRequest() / fullTimeSystem * 100);
        System.out.println("%\n");
        
        for (int i = 0; i < channel2.size(); ++i)
        {
            System.out.println("Количество обработанных заявок в канале (2, " + (i + 1) + "): " + channel2.get(i).getCounterRequest());
            System.out.println("Среднее время обработки заявки в канале (2, " + (i + 1) + "): " + channel2.get(i).getFullTimeRequest() / channel1.getCounterRequest());
            System.out.print("Процент загруженности канала (2, " + (i + 1) + "): ");
            System.out.printf("%8.2f", (double)channel2.get(i).getFullTimeRequest() / fullTimeSystem * 100);
            System.out.println("%");
        }
        
        System.out.println("\nКоличество обработанных заявок в 3-ом канале: " + channel3.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 3-ом канале: " + channel3.getFullTimeRequest() / channel3.getCounterRequest());
        System.out.printf("Процент загруженности канала: %8.2f", (double)channel3.getFullTimeRequest() / fullTimeSystem * 100);
        System.out.println("%");
        
        System.out.println("\nКоличество обработанных заявок в 4-ом канале: " + channel4.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 4-ом канале: " + channel4.getFullTimeRequest() / channel4.getCounterRequest());
        System.out.printf("Процент загруженности канала: %8.2f", (double)channel4.getFullTimeRequest() / fullTimeSystem * 100);
        System.out.println("%");
        
        System.out.println("\nКоличество заявок в 1 накопителе: " + storageDevice1.counterRequest());
        System.out.println("Количество заявок в 2 накопителе: " + storageDevice2.counterRequest());
        System.out.println("Количество заявок в 3 накопителе: " + storageDevice3.counterRequest());
    }
}