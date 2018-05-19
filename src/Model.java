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
       
        int counterRequest = 0;
        int fullTimeSystem = 0;
        int systemTime = 0;
        int arrivalRequestsTime = 0;       

        NumberSensor numSensor = null;
        Sensors sen = new Sensors();
        ChannelState state = null; 
        WriteIntoFile writer = new WriteIntoFile();
        WritelntoFile writer1 = new WritelntoFile();
        
        System.out.println("�������� ����� ������: ");
        System.out.println("0 - �����������;\n1 - ���������");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int variant = 0;
        try
        {
            String variantStr = reader.readLine();
            
            while (!variantStr.equals("0") && !variantStr.equals("1"))
            {
                System.out.println("������������ ����!");
                System.out.println("�������� ����� ������: ");
                System.out.println("0 - �����������;\n1 - ���������");
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
            // 4. ������������� ������ ������ �� �������
            if (channel4.getState() != state.Free && channel4.getWorkTime() <= systemTime)
            {
                Request value = channel4.removeRequest();
                fullTimeSystem += value.getTime();
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

            // 5. ������������� �������� ������ �� 3 ���� � 4 ����
            if (channel3.getState() != state.Free && channel3.getWorkTime() <= systemTime)
            {
                if (channel4.getState() == state.Free)
                {
                    Request value = channel3.removeRequest();
                    int time = variant == 1 ? sen.sensor(numSensor.SENSOR40_80.ordinal()) : 60;
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

            // 6. ������������ ������ � 3 ����
            if (storageDevice3.getSize() > 0)
            {
                if (channel3.getState() == state.Free)
                {
                    Request value = storageDevice3.removeRequest();
                    int time = variant == 1 ? sen.sensor(numSensor.SENSOR20_40.ordinal()) : 30;
                    value.updateTime(time);
                    channel3.addRequest(value, time);
                    channel3.setState(state.Work);
                }
            }

            // 7. ������������� �������� �� 2 � 3 ����
            for (int i = 0; i < channel2.size(); ++i)
            {
                if (channel2.get(i).getState() != state.Free && channel2.get(i).getWorkTime() <= systemTime)
                {
                    if (channel3.getState() == state.Free)
                    {
                        Request value = channel2.get(i).removeRequest();
                        int time = variant == 1 ? sen.sensor(numSensor.SENSOR20_40.ordinal()) : 30; 
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

            // 8. ������������ ������ �� 2 ����
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
                    }
                }
            }

            // 9. ������������� �������� ������ �� 1 �� 2 ����
            if (channel1.getState() != state.Free && channel1.getWorkTime() <= systemTime)
            {
                boolean flag = false;
                for (int i = 0; i < channel2.size(); ++i)
                {
                    if (channel2.get(i).getState() == state.Free)
                    {
                        Request value = channel1.removeRequest();
                        int time = variant == 1 ? sen.sensor(numSensor.SENSOR50_150.ordinal()) : 50;
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
                // ��������� ������ � 1 ����
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
                
                // ����������� ������ �� ���� Q-�����
                if (arrivalRequestsTime <= systemTime)
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
                    arrivalRequestsTime = 60;
                }
            }
            
            // ����������� ���������� �������
            if (counterRequest < 100)
            {
                int minTime = variant == 0 ? arrivalRequestsTime : 60;
                
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
        
        
        System.out.println("���������� ������������ ������: " + counterRequest + "\n");
        
        System.out.println("���������� ������������ ������ � 1-�� ������: " + channel1.getCounterRequest());
        System.out.println("������� ����� ��������� ������ � 1-�� ������: " + channel1.getFullTimeRequest() / channel1.getCounterRequest());
        System.out.printf("������� ������������� ������: %8.2f", (double)channel1.getFullTimeRequest() / systemTime * 100);
        System.out.println("%\n");
        
        for (int i = 0; i < channel2.size(); ++i)
        {
            System.out.println("���������� ������������ ������ � ������ (2, " + (i + 1) + "): " + channel2.get(i).getCounterRequest());
            System.out.println("������� ����� ��������� ������ � ������ (2, " + (i + 1) + "): " + channel2.get(i).getFullTimeRequest() / channel1.getCounterRequest());
            System.out.print("������� ������������� ������ (2, " + (i + 1) + "): ");
            System.out.printf("%8.2f", (double)channel2.get(i).getFullTimeRequest() / systemTime * 100);
            System.out.println("%");
        }
        
        System.out.println("\n���������� ������������ ������ � 3-�� ������: " + channel3.getCounterRequest());
        System.out.println("������� ����� ��������� ������ � 3-�� ������: " + channel3.getFullTimeRequest() / channel3.getCounterRequest());
        System.out.printf("������� ������������� ������: %8.2f", (double)channel3.getFullTimeRequest() / systemTime * 100);
        System.out.println("%");
        
        System.out.println("\n���������� ������������ ������ � 4-�� ������: " + channel4.getCounterRequest());
        System.out.println("������� ����� ��������� ������ � 4-�� ������: " + channel4.getFullTimeRequest() / channel4.getCounterRequest());
        System.out.printf("������� ������������� ������: %8.2f", (double)channel4.getFullTimeRequest() / systemTime * 100);
        System.out.println("%");
        
        System.out.println("\n���������� ������ � 1 ����������: " + storageDevice1.counterRequest());
        System.out.println("���������� ������ � 2 ����������: " + storageDevice2.counterRequest());
        System.out.println("���������� ������ � 3 ����������: " + storageDevice3.counterRequest());
    }
}