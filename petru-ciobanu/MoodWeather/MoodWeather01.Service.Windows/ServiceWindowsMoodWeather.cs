using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.ServiceProcess;
using System.ServiceModel;
using MoodWeather.Service.Windows.WeatherServiceCondition;
using System.Collections.ObjectModel;
using System.Timers;
namespace MoodWeather.Service.Windows
{
    partial class ServiceWindowsMoodWeather : ServiceBase
    {
        public ServiceHost ServiceHosts;
        private  WeatherServiceClient _client;
        private ObservableCollection<ListaOrase> _listOrase;
        private int _countCity;
        private int _idx;
        Timer _timerCity = new Timer();

        public ServiceWindowsMoodWeather()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {

            try
            {
                WriteToLog("Service Started By Task Scheduler");
                ServiceHosts = new ServiceHost(typeof(ServiceWindowsMoodWeather));
                ServiceHosts.Open();
                StartService();
               
            }
            catch (Exception ex)
            {
                WriteToLogError("WindowsMoodWeather" + ex.Message);
            }
           
        }

     
        // 1 START SERVICE BY THE USER 
        public void StartService()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.Open();
                _client.StartDataCollectCompleted -= client_StartDataCollectCompleted;
                _client.StartDataCollectCompleted += client_StartDataCollectCompleted;
                _client.StartDataCollectAsync("serviceCommand");
            }
            catch (Exception e)
            {
                WriteToLogError("WindowsMoodWeather" + e.Message);
            }
        }
        private void client_StartDataCollectCompleted(object sender, AsyncCompletedEventArgs e)
        {
            _client.Close();
            WriteToLog("Start MoodWebService");
            DeleteDatabase();
        }

        //2 DELETE DATABASE 
        public void DeleteDatabase()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.Open();
                _client.DeleteDatabaseCompleted -= client_DeleteDatabaseCompleted;
                _client.DeleteDatabaseCompleted += client_DeleteDatabaseCompleted;
                _client.DeleteDatabaseAsync();
            }
            catch (Exception e)
            {
                WriteToLogError("WindowsMoodWeather" + e.Message);
            }
        }

        private void client_DeleteDatabaseCompleted(object sender, AsyncCompletedEventArgs e)
        {
            _client.Close();
            LoadCityList();
        }


        //3 LOAD CITY LISTS 
        public void LoadCityList()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.Open();
                _client.SelectCityCompleted -= client_SelectCityCompleted;
                _client.SelectCityCompleted += client_SelectCityCompleted;    
                _client.SelectCityAsync();
            }
            catch (Exception e)
            {
                WriteToLogError("WindowsMoodWeather" + e.Message);
            }
        }

        private void client_SelectCityCompleted(object sender, SelectCityCompletedEventArgs e)
        {
            _listOrase = new ObservableCollection<ListaOrase>();

            for (int i = 0; i < e.Result.Count(); i++)
            {
                _listOrase.Add(new ListaOrase { CityName = e.Result[i].CityName, CountryCode = e.Result[i].CountryCode, CountryCodeTeamco = e.Result[i].CountryCodeTeamco, CityAlternative = e.Result[i].CityAlternative, CityId = e.Result[i].CityId });
                if (_listOrase.Count == e.Result.Count())
                {
                    _countCity = _listOrase.Count();
                    EvaluateCity();
                }
            }
        }

        //4.EVALUATE CITY  LIST 
        private void EvaluateCity()
        {
            if (_idx < _countCity)
            {
                NextPosition(_idx);

            }
            else
            {
                if (_idx == _countCity)
                {
                  WriteToLog("Get Data From weather.com  completed!");
                  GenerateXmlFile();
                }
            }
        }

        //5 SEND REQUEST TO WEATHER.COM
        public void NextPosition(int nr)
        {
            try
            {

                SendWeather(_listOrase.ElementAt(nr).CountryCode, _listOrase.ElementAt(nr).CountryCodeTeamco, _listOrase.ElementAt(nr).CityName, _listOrase.ElementAt(nr).CityAlternative, "serviceCommands", _listOrase.ElementAt(nr).CityId);
                WriteToLog("Time Estimated for update is :" + (((_countCity - nr) * 30)) + " seconds");
                TimerCitys();
            }
            catch (Exception e)
            {
                WriteToLogError("WindowsMoodWeather" + e.Message);
            }

        }

   
        //6 INSERT RESULT FROM  WEATHER.COM INTO DATABASE
        public void SendWeather(string countrycode, string countrycodeteamco, string cityname, string cityalternative, string user, string cityId)
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.Open();
                _client.GetWeatherForecastCompleted -= client_GetWeatherForecastCompleted;
                _client.GetWeatherForecastCompleted += client_GetWeatherForecastCompleted;
                _client.GetWeatherForecastAsync(countrycode, countrycodeteamco, cityname, cityalternative, user, cityId);
            }
            catch (Exception e)
            {
                WriteToLogError("WindowsMoodWeather" + e.Message);
            }

        }

        private void client_GetWeatherForecastCompleted(object sender, AsyncCompletedEventArgs e)
        {
            _client.Close();
        }

        //TIMERS FOR TRIGGER WEATHER.COM
        private void TimerCitys()
        {
            _timerCity = new Timer {Interval = 30000};
            _timerCity.Elapsed += timerCity_Tick;
            _timerCity.Start();
        }

        private void timerCity_Tick(object source, ElapsedEventArgs e)
        {
            _timerCity.Stop();
            _idx++;
            EvaluateCity();

        }


        //7 GENERATE XML  FILE TO SERVER 
        private void GenerateXmlFile()
        {
            try
            {
                _client = new WeatherServiceClient();
                _client.Open();
                _client.SaveXmlFileCompleted -= client_saveXmlFileCompleted;     
                _client.SaveXmlFileCompleted += client_saveXmlFileCompleted;
                _client.SaveXmlFileAsync();
            }
            catch (Exception e)
            {
                WriteToLogError("WindowsMoodWeather"+e.Message);
            }
        }
   
        private void client_saveXmlFileCompleted(object sender, AsyncCompletedEventArgs e)
        {
            if (e.Error != null)
            {
                WriteToLogError("Generate Xml File Error ");
            }
            else
            {
                WriteToLog("Generate Xml File Complete ");
                _client.Close();
                Stop();
            }    

        }


        protected override void OnStop()
        {
            if (ServiceHosts != null)
            {
                ServiceHosts.Close();
                ServiceHosts = null;
                WriteToLog(" Service MoodWeather Stop");
            }
        }


        private void WriteToLog(string msg)
        {
            EventLog.WriteEntry("WindowsMoodWeather", msg, EventLogEntryType.Information,888);
        }


        private void WriteToLogError(string msg)
        {
            EventLog.WriteEntry("WindowsMoodWeather", msg, EventLogEntryType.Error,666);
        }


    }

    public class ListaOrase
    {
        public string CityName { get; set; }
        public string CityId { get; set; }
        public string CountryCode { get; set; }

        public string CountryCodeTeamco { get; set; }
        public string CityAlternative { get; set; }
    }
}
