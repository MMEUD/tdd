using System;
using System.Collections.ObjectModel;
using System.Runtime.Serialization;
using System.ServiceModel;

namespace MoodWeather.Service.Web
{
    [ServiceContract]
    public interface IEventLogService
    {
        [OperationContract]
        ObservableCollection<EventSources> GetEvents();

        [OperationContract]
         String DeleteEvents(string test);
    }



    [DataContract]
    public class EventSources
    {
        [DataMember]
        public string Index { get; set; }

        [DataMember]
        public string Level { get; set; }

        [DataMember]
        public string Time { get; set; }

        [DataMember]
        public string Message { get; set; }

        [DataMember]
        public string Source { get; set; }

        [DataMember]
        public string EventId { get; set; }

    }

}

