using System;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.ServiceModel;

namespace MoodWeather.Service.Web
{
    [ServiceBehavior]
    public class EventLogService : IEventLogService
    {

        public ObservableCollection<EventSources> GetEvents()
        {
            var listEvents = new ObservableCollection<EventSources>();
            var eventLog = new EventLog("Application");
            var query = from EventLogEntry el in eventLog.Entries
                        where el.Source == "WindowsMoodWeather"
                        orderby el.TimeGenerated descending
                        select new
                            {
                                Index = el.Index,
                                Level = el.EntryType,
                                Time = el.TimeGenerated,
                                Message = el.Message,
                                Source = el.Source,
#pragma warning disable 612,618
                                EventId = el.EventID
#pragma warning restore 612,618

                            };

            foreach (var result in query)
            {
                listEvents.Add(new EventSources
                    {
                        Index = result.Index.ToString(),
                        Level = result.Level.ToString(),
                        Time = result.Time.ToString(),
                        Message = result.Message,
                        Source = result.Source,
                        EventId = result.EventId.ToString()

                    });
            }

            return listEvents;
        }



        public String DeleteEvents(string command)
        {
            try
            {
               
                var eventLog = new EventLog("Application");
                eventLog.Source="WindowsMoodWeather";
                eventLog.Clear();
                eventLog.Dispose();
            }
            catch (Exception e)
            {
                command = e.ToString();
            }

          //  EventLog.DeleteEventSource("WindowsMoodWeather");

            return command;
        }
    }
}

