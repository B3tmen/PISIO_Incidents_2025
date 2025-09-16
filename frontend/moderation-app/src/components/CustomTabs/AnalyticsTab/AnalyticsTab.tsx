import { Bar, Column, Pie } from '@ant-design/plots' ;     
import { analyticsService } from '@api/services/analyticsService';
import { useQuery } from '@tanstack/react-query';
import { Flex, Typography } from 'antd';   

function AnalyticsTab() {

  const { data: dailyData } = useQuery({
      queryKey: ['dailyCountProjection'],
      queryFn: async () => analyticsService.getByDay(),
  })
  const { data: typeData } = useQuery({
      queryKey: ['typeCountProjection'],
      queryFn: async () => analyticsService.getByType(),
  })
  const { data: locationData } = useQuery({
      queryKey: ['locationCountProjection'],
      queryFn: async () => analyticsService.getByLocation(),
  })


  const formattedData = dailyData?.map(item => ({
    ...item,
    reportedAt: new Date(item.reportedAt).toISOString().split('T')[0]
  }));

  const dailyDataConfig = { 
    data: formattedData,
    height : 400 , 
    xField : 'reportedAt', 
    yField : 'count' , 
    style : { 
      lineWidth : 2 
    }
  };

  const typeDataConfig = {
    data: typeData,
    angleField: 'count',
    colorField: 'type',
    label: {
      text: 'count',
      style: {
        fontWeight: 'bold',
      },
    },
    legend: {
      color: {
        title: false,
        position: 'right',
        rowPadding: 5,
      },
    },
  };

  const locationDataConfig = { 
    data: locationData,
    xField: 'count',
    yField: 'city',
    seriesField: 'country', // optional if you want colors by country
    legend: false,
    height: 400,
    barWidthRatio: 0.6,
    xAxis: { title: { text: 'Incident Count' } },
    yAxis: { title: { text: 'City' } },
  };

  return(
    <>
      <Flex vertical >
        <Typography.Title level={4}>Incidents by day</Typography.Title>
        <Column { ... dailyDataConfig } />
      </Flex>
      
      <Flex vertical >
        <Typography.Title level={4}>Incidents by Type</Typography.Title>
        <Pie { ... typeDataConfig } />
      </Flex>

      <Flex vertical >
        <Typography.Title level={4}>Incidents by location</Typography.Title>
        <Bar { ... locationDataConfig } />
      </Flex>
    </>
  );
}

export default AnalyticsTab;