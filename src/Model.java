import java.util.Vector;
import java.io.*;
import java.math.*;
import java.util.Random;

enum ChannelState { Free, Work, Lock }

public class Model
{
    enum NumberSensor { SENSOR20_80, SENSOR50_150, SENSOR20_40, SENSOR40_80 }
    
   
    @SuppressWarnings("empty-statement")
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
       
        int counterRequest = 0;
        int fullTimeSystem = 0;
        int systemTime = 0;
        int arrivalRequestsTime = 0; 
        int counterArrivalRequests = 0;
        
        int inputValue = 60;
        int valueHighChannel1 = 50;
        int valueHighChannel2 = 50;
        int valueHighChannel3 = 30;
        int valueHighChannel4 = 60;
        
        int valueLowChannel1 = 20;
        int valueLowChannel2 = 25;
        int valueLowChannel3 = 20;
        int valueLowChannel4 = 40;

        NumberSensor numSensor = null;
        Sensors sen = new Sensors();
        ChannelState state = null; 
        WriteIntoFile writer = new WriteIntoFile();
        WritelntoFile writer1 = new WritelntoFile();
        int counterRequestArray[] = new int[5];
        
        Random random = new Random();
        
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
        }
        
        
        while (true)
        {
            // 4. Моделирование выхода заявки из системы
            if (channel4.getState() != state.Free && channel4.getWorkTime() <= systemTime)
            {
                Request value = channel4.removeRequest();
                if (variant == 1)
                {
                    if (channel1.getState() == state.Free)
                    {
                        //int time = sen.sensor(numSensor.SENSOR20_80.ordinal());
                        int time = (random.nextInt(valueHighChannel1 - valueLowChannel1 + 1) + valueLowChannel1);
                        value.setTime(0);
                        value.updateTime(time);
                        channel1.addRequest(value, time);
                        channel1.setState(state.Work);
                        channel4.setState(state.Free);
                        channel4.setTime(0);
                    }
                    else
                    {
                        storageDevice1.addRequest(value);
                    }
                }
                else
                {
                    channel4.setState(state.Free);
                    channel4.setTime(0);
                }
                ++counterRequest;
            }
            
            //System.out.println("К-ство заявок в системе: " + (counterArrivalRequests - counterRequest));
            if (variant == 0)
                ++counterRequestArray[Math.abs(counterArrivalRequests - counterRequest)];
            
            // 5. Моделирование перехода заявки из 3 фазы в 4 фазу
            if (channel3.getState() != state.Free && channel3.getWorkTime() <= systemTime)
            {
                if (channel4.getState() == state.Free)
                {
                    Request value = channel3.removeRequest();
                    //int time = variant == 1 ? sen.sensor(numSensor.SENSOR40_80.ordinal()) : 60;
                    int time = variant == 1 ? (random.nextInt(valueHighChannel4 - valueLowChannel4 + 1) + valueLowChannel4) : 60;
                    value.updateTime(time);
                    channel4.addRequest(value, time);
                    channel4.setState(state.Work);
                    channel3.setState(state.Free);
                    channel3.setTime(0);
                }
                else
                {
                    channel3.setState(state.Lock);
                }
            }
            
            //System.out.println("К-ство заявок в системе: " + (counterArrivalRequests - counterRequest));
            if (variant == 0)
            ++counterRequestArray[Math.abs(counterArrivalRequests - counterRequest)];
            
            // 6. Обслуживание заявки в 3 фазе
            if (storageDevice3.getSize() > 0)
            {
                if (channel3.getState() == state.Free)
                {
                    Request value = storageDevice3.removeRequest();
                    //int time = variant == 1 ? sen.sensor(numSensor.SENSOR20_40.ordinal()) : 30;
                    int time = variant == 1 ? (random.nextInt(valueHighChannel3 - valueLowChannel3 + 1) + valueLowChannel3) : 30;
                    value.updateTime(time);
                    channel3.addRequest(value, time);
                    channel3.setState(state.Work);
                }
            }
            
            //System.out.println("К-ство заявок в системе: " + (counterArrivalRequests - counterRequest));
            if (variant == 0)
            ++counterRequestArray[Math.abs(counterArrivalRequests - counterRequest)];
            
            // 7. Моделирование перехода из 2 в 3 фазу
            for (int i = 0; i < channel2.size(); ++i)
            {
                if (channel2.get(i).getState() != state.Free && channel2.get(i).getWorkTime() <= systemTime)
                {
                    if (channel3.getState() == state.Free)
                    {
                        Request value = channel2.get(i).removeRequest();
                        //int time = variant == 1 ? sen.sensor(numSensor.SENSOR20_40.ordinal()) : 30; 
                        int time = variant == 1 ? (random.nextInt(valueHighChannel3 - valueLowChannel3 + 1) + valueLowChannel3) : 30;
                        value.updateTime(time);
                        channel3.addRequest(value, time);
                        channel3.setState(state.Work);
                    }
                    else
                    {
                        storageDevice3.addRequest(channel2.get(i).removeRequest());
                    }
                    channel2.get(i).setState(state.Free);
                    channel2.get(i).setTime(0);
                }
            }
            
            //System.out.println("К-ство заявок в системе: " + (counterArrivalRequests - counterRequest));
            if (variant == 0)
            ++counterRequestArray[Math.abs(counterArrivalRequests - counterRequest)];
            
            // 8. Обслуживание заявки во 2 фазе
            if (storageDevice2.getSize() > 0)
            {
                for (int i = 0; i < channel2.size(); ++i)
                {
                    if (channel2.get(i).getState() == state.Free)
                    {
                        Request value = storageDevice2.removeRequest();
                        //int time = variant == 1 ? sen.sensor(numSensor.SENSOR50_150.ordinal()) : 50;
                        int time = variant == 1 ? (random.nextInt(valueHighChannel2 - valueLowChannel2 + 1) + valueLowChannel2) : 50;
                        value.updateTime(time);
                        channel2.get(i).addRequest(value, time);
                        channel2.get(i).setState(state.Work);
                    }
                }
            }
            
            //System.out.println("К-ство заявок в системе: " + (counterArrivalRequests - counterRequest));
            if (variant == 0)
            ++counterRequestArray[Math.abs(counterArrivalRequests - counterRequest)];
            
            // 9. Моделирование перехода заявки из 1 во 2 фазу
            if (channel1.getState() != state.Free && channel1.getWorkTime() <= systemTime)
            {
                boolean flag = false;
                for (int i = 0; i < channel2.size(); ++i)
                {
                    if (channel2.get(i).getState() == state.Free)
                    {
                        Request value = channel1.removeRequest();
                        //int time = variant == 1 ? sen.sensor(numSensor.SENSOR50_150.ordinal()) : 50;
                        int time = variant == 1 ? (random.nextInt(valueHighChannel2 - valueLowChannel2 + 1) + valueLowChannel2) : 50;
                        value.updateTime(time);
                        channel2.get(i).addRequest(value, time);
                        channel2.get(i).setState(state.Work);
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    storageDevice2.addRequest(channel1.removeRequest());
                }
                
                channel1.setState(state.Free);
                channel1.setTime(0);
            }
                        
            if (variant == 0)
            {
                // Обработка заявки в 1 фазе
                if (storageDevice1.getSize() > 0)
                {
                    if (channel1.getState() == state.Free)
                    {
                        Request value = storageDevice1.removeRequest();
                        value.setTime(50);
                        channel1.addRequest(value, 50);
                        channel1.setState(state.Work);
                    }
                }
                
                // Поступление заявки на вход Q-схемы
                if (arrivalRequestsTime <= systemTime)
                {
                    ++counterArrivalRequests;
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
                    arrivalRequestsTime = inputValue;
                }
                //System.out.println("К-ство заявок в системе: " + (counterArrivalRequests - counterRequest));
                if (variant == 0)
                ++counterRequestArray[Math.abs(counterArrivalRequests - counterRequest)];
            }
            
            // Продвижение системного времени
            if (counterRequest < 100)
            {
                int minTime = variant == 0 ? arrivalRequestsTime : inputValue;
                
                if (minTime > channel1.getWorkTime() && channel1.getWorkTime() > 0)
                {
                    if (channel1.getState() == state.Free)
                    {
                        minTime = channel1.getWorkTime();
                    }
                }
                
                for (int i = 0; i < channel2.size(); ++i)
                {
                    if (minTime > channel2.get(i).getWorkTime() && channel2.get(i).getWorkTime() > 0)
                    {
                        if (channel2.get(i).getState() == state.Free)
                        {
                            minTime = channel2.get(i).getWorkTime();
                        }
                    }
                }
                
                if (minTime > channel3.getWorkTime() && channel3.getWorkTime() > 0)
                {
                    if (channel3.getState() == state.Free)
                    {
                        minTime = channel3.getWorkTime();
                    }
                }
                
                if (minTime > channel4.getWorkTime() && channel4.getWorkTime() > 0)
                {
                    if (channel4.getState() == state.Free)
                    {
                        minTime = channel4.getWorkTime();
                    }
                }
                
                systemTime += minTime;
            }
            else
            {
                break;
            }
        }
        
        
        System.out.println("Количество обработанных заявок: " + counterRequest + "\n");
        
        System.out.println("Количество обработанных заявок в 1-ом канале: " + channel1.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 1-ом канале: " + channel1.getFullTimeRequest() / channel1.getCounterRequest());
        double meanChannel1 = Math.abs((double)channel1.getFullTimeRequest() / systemTime * 100);
        System.out.printf("Процент загруженности канала: %8.2f", meanChannel1);
        System.out.println("%\n");
        
        double meanChannel2[] = new double[2];
        for (int i = 0; i < channel2.size(); ++i)
        {
            System.out.println("Количество обработанных заявок в канале (2, " + (i + 1) + "): " + channel2.get(i).getCounterRequest());
            System.out.println("Среднее время обработки заявки в канале (2, " + (i + 1) + "): " + channel2.get(i).getFullTimeRequest() / channel1.getCounterRequest());
            System.out.print("Процент загруженности канала (2, " + (i + 1) + "): ");
            meanChannel2[i] = Math.abs((double)channel2.get(i).getFullTimeRequest() / systemTime * 100);
            System.out.printf("%8.2f", meanChannel2[i]);
            System.out.println("%");
        }
        
        System.out.println("\nКоличество обработанных заявок в 3-ом канале: " + channel3.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 3-ом канале: " + channel3.getFullTimeRequest() / channel3.getCounterRequest());
        double meanChannel3 = Math.abs((double)channel3.getFullTimeRequest() / systemTime * 100);
        System.out.printf("Процент загруженности канала: %8.2f", meanChannel3);
        System.out.println("%");
        
        System.out.println("\nКоличество обработанных заявок в 4-ом канале: " + channel4.getCounterRequest());
        System.out.println("Среднее время обработки заявки в 4-ом канале: " + channel4.getFullTimeRequest() / channel4.getCounterRequest());
        double meanChannel4 = Math.abs((double)channel4.getFullTimeRequest() / systemTime * 100);
        System.out.printf("Процент загруженности канала: %8.2f", meanChannel4);
        System.out.println("%");
        
        System.out.println("\nКоличество заявок в 1 накопителе: " + storageDevice1.counterRequest());
        System.out.println("Количество заявок в 2 накопителе: " + storageDevice2.counterRequest());
        System.out.println("Количество заявок в 3 накопителе: " + storageDevice3.counterRequest());
        
        if (variant == 1)
        {
            double storageHarr[][] = { {0, 1, 3, 5, 7}, {1.0, 0.8, 0.63, 0.37, 0.2} };
            double channelHarr[][] = { {50.0, 35.0, 30.0, 22.0, 15.0}, {1.0, 0.8, 0.63, 0.37, 0.2} };

            double storageRating1 = 0;
            double storageRating2 = 0;
            double storageRating3 = 0;
            double channelRating1 = 0;
            double channelRating21 = 0;
            double channelRating22 = 0;
            double channelRating3 = 0;
            double channelRating4 = 0;

            int maxH1 = storageDevice1.counterRequest();
            int maxH2 = storageDevice2.counterRequest();
            int maxH3 = storageDevice3.counterRequest();

            double meanCh[] = { meanChannel1, meanChannel2[0], meanChannel2[1], meanChannel3, meanChannel4};

            // Расчет оценко и параметра оптимизации
            for (int i = 1; i < 5; ++i)
            {
                if (storageHarr[0][i - 1] <= maxH1 && maxH1 < storageHarr[0][i])
                {
                    storageRating1 = (maxH1 - storageHarr[0][i]) * (storageHarr[1][i] - storageHarr[1][i - 1]) / (storageHarr[0][i] - storageHarr[0][i - 1]) + storageHarr[1][i];
                }
            }

            if (storageRating1 == 0)
            {
                storageRating1 = storageHarr[1][4];
            }


            for (int i = 1; i < 5; ++i)
            {
                if (storageHarr[0][i - 1] <= maxH2 && maxH2 < storageHarr[0][i])
                {
                    storageRating2 = (maxH2 - storageHarr[0][i]) * (storageHarr[1][i] - storageHarr[1][i - 1]) / (storageHarr[0][i] - storageHarr[0][i - 1]) + storageHarr[1][i];
                }
            }

            if (storageRating2 == 0)
            {
                storageRating2 = storageHarr[1][4];
            }

            for (int i = 1; i < 5; ++i)
            {
                if (storageHarr[0][i - 1] <= maxH3 && maxH3 < storageHarr[0][i])
                {
                    storageRating3 = (maxH3 - storageHarr[0][i]) * (storageHarr[1][i] - storageHarr[1][i - 1]) / (storageHarr[0][i] - storageHarr[0][i - 1]) + storageHarr[1][i];
                }
            }

            if (storageRating3 == 0)
            {
                storageRating3 = storageHarr[1][4];
            }

            for (int i = 1; i < 5; ++i)
            {
                if (channelHarr[0][i - 1] >= meanCh[0] && meanCh[0] > channelHarr[0][i])
                {
                    channelRating1 = (meanCh[0] - channelHarr[0][i]) * (channelHarr[1][i] - channelHarr[1][i - 1]) / (channelHarr[0][i] - channelHarr[0][i - 1]) + channelHarr[1][i];
                }
            }

            if (channelRating1 == 0)
            {
                channelRating1 = channelHarr[1][4];
            }

            for (int i = 1; i < 5; ++i)
            {
                if (channelHarr[0][i - 1] >= meanCh[1] && meanCh[1] > channelHarr[0][i])
                {
                    channelRating21 = (meanCh[1] - channelHarr[0][i]) * (channelHarr[1][i] - channelHarr[1][i - 1]) / (channelHarr[0][i] - channelHarr[0][i - 1]) + channelHarr[1][i];
                }
            }

            if (channelRating21 == 0)
            {
                channelRating21 = channelHarr[1][4];
            }

            for (int i = 1; i < 5; ++i)
            {
                if (channelHarr[0][i - 1] >= meanCh[2] && meanCh[2] > channelHarr[0][i])
                {
                    channelRating22 = (meanCh[2] - channelHarr[0][i]) * (channelHarr[1][i] - channelHarr[1][i - 1]) / (channelHarr[0][i] - channelHarr[0][i - 1]) + channelHarr[1][i];
                }
            }

            if (channelRating22 == 0)
            {
                channelRating22 = channelHarr[1][4];
            }

            for (int i = 1; i < 5; ++i)
            {
                if (channelHarr[0][i - 1] >= meanCh[3] && meanCh[3] > channelHarr[0][i])
                {
                    channelRating3 = (meanCh[3] - channelHarr[0][i]) * (channelHarr[1][i] - channelHarr[1][i - 1]) / (channelHarr[0][i] - channelHarr[0][i - 1]) + channelHarr[1][i];
                }
            }

            if (channelRating3 == 0)
            {
                channelRating3 = channelHarr[1][4];
            }

            for (int i = 1; i < 5; ++i)
            {
                if (channelHarr[0][i - 1] >= meanCh[4] && meanCh[4] > channelHarr[0][i])
                {
                    channelRating4 = (meanCh[4] - channelHarr[0][i]) * (channelHarr[1][i] - channelHarr[1][i - 1]) / (channelHarr[0][i] - channelHarr[0][i - 1]) + channelHarr[1][i];
                }
            }

            if (channelRating4 == 0)
            {
                channelRating4 = channelHarr[1][4];
            }

            double paramOpt = Math.pow((storageRating1 * storageRating2 * storageRating3 * channelRating1 * channelRating21 * channelRating22 * channelRating3 * channelRating4), 1.0 / 7.0);

            System.out.println("Параметр оптимизации: " + paramOpt);
        }
        else
        {
            System.out.println("P3: " + (double)counterRequestArray[3] / (double)(counterRequestArray[3] + counterRequestArray[4]));
            System.out.println("P4: " + (double)counterRequestArray[4] / (double)(counterRequestArray[3] + counterRequestArray[4]));
        }
    }
}