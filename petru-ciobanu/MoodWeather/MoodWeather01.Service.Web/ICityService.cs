using System;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Collections.ObjectModel;

namespace MoodWeather.Service.Web
{
    
    [ServiceContract]
    public interface ICityService
    {

      [OperationContract]
      ObservableCollection<CityLists> GetCityList();
     
      [OperationContract]
      void InsertCity(string city ,string cityalternative, string country ,string countryCode,string countryCodeTeamco,string userId,string active);

      [OperationContract]
      void DeleteCity(int id);

      [OperationContract]
      void UpdateCity(int id, string city,string cityalternative, string country,string countryCode,string countryCodeTeamco, string userId,string active);
    }

    [DataContract]
    public class CityLists   //:INotifyPropertyChanged
    {

        #region Fields
        [DataMember(Name = "Id")]
        public int Id { get; set; }
        [DataMember(Name = "CityName")]
        public string CityName { get; set; }
        [DataMember(Name = "CityAlternative")]
        public string CityAlternative { get; set; }
        [DataMember(Name = "Country")]
        public string Country { get; set; }
        [DataMember(Name = "CountryCode")]
        public string CountryCode { get; set; }
        [DataMember(Name = "CountryCodeTeamco")]
        public string CountryCodeTeamco { get; set; }
        [DataMember(Name = "DateAdd")]
        public DateTime DateAdded { get; set; }
        [DataMember(Name = "DateModified")]
        public DateTime DateModified { get; set; }
        [DataMember]
        public string UserId { get; set; }
        [DataMember]
        public string Active { get; set; }
        #endregion
    }

   

}
