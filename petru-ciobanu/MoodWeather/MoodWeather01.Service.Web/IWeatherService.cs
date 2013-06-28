using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ServiceModel;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.Serialization;
using System.ServiceModel.Web;
using System.IO;
using System.Text;

namespace MoodWeather.Service.Web
{
    [ServiceContract]
    public interface IWeatherService
    {
        [OperationContract]
        String CheckCity(string cityName, string countryCode);
     
        
        [OperationContract]
        ObservableCollection<CityList> SelectCity();

        [OperationContract]
        void GetCityLists();

        [OperationContract]
        void GetWeatherForecast(string countryCode, string countryCodeTeamco, string city,string cityalternative, string commandType,string cityId);

        [OperationContract]
        void InsertData(string cityName,string cityId, string countryCode, string condition, string sunrise, string sunset,
            string tempLow, string tempHigh, string icon, string skyIcon, DateTime date, string weekDay, string updateBy);

        [OperationContract]
        ObservableCollection<WeatherCondition> GetCityCondition();

        [OperationContract]
        ObservableCollection<WeatherCondition> SelectCityCondition(string cityName);

        [OperationContract]
        void StartDataCollect(string command);

        [OperationContract]
        void DeleteDatabase();
        
        [OperationContract]
        void WriteToLog(string msg);

        [OperationContract]
        void SaveXmlFile();


        [OperationContract]
        String UploadFileFtpAutomate(string fileStream); 

        [OperationContract]
        void CreateXmlBackup(string fileName, string content);


    }


    [DataContract]
    public class WeatherCondition
    {
        public event PropertyChangedEventHandler PropertyChanged;

        public void OnPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }
        #region Fields
        [DataMember]
        public int Id { get; set; }
        [DataMember]
        public string CityName { get; set; }
        [DataMember]
        public string CountryCode { get; set; }
        [DataMember]
        public string Condition { get; set; }
        [DataMember]
        public string Sunrise { get; set; }
        [DataMember]
        public string Sunset { get; set; }
        [DataMember]
        public string TempLow { get; set; }
        [DataMember]
        public string TempHigh { get; set; }
        [DataMember]
        public string Icon { get; set; }
        [DataMember]
        public string SkyIcon { get; set; }
        [DataMember]
        public DateTime Date { get; set; }
        [DataMember]
        public string WeekDay { get; set; }
        [DataMember]
        public DateTime LastUpdate { get; set; }
        [DataMember]
        public string UpdateBy { get; set; }
        #endregion


    }

    [DataContract]
    public class CityList
    {

        [DataMember]
        public string CityName { get; set; }
        [DataMember]
        public string CityId { get; set; }
        [DataMember]
        public string CountryCode { get; set; }
        [DataMember]
        public string CountryCodeTeamco { get; set; }
        [DataMember]
        public string CityAlternative { get; set; }

    }
}