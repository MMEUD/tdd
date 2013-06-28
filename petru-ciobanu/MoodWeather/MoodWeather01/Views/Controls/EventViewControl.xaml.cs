using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using MoodWeather.EventLogService;

namespace MoodWeather.Views.Controls
{
    public partial class EventViewControl : UserControl
    {
        private EventLogServiceClient _eventLogServiceClient;
        public EventViewControl()
        {
            InitializeComponent();
            if (WebContext.Current.Authentication.User.IsInRole("Admin") == true)
            {
                BtnClearEvents.IsEnabled = true;
            };
        }

        private void BtnServerStatus_Click(object sender, RoutedEventArgs e)
        {
            _eventLogServiceClient = new EventLogServiceClient();
            _eventLogServiceClient.GetEventsCompleted -= _eventLogServiceClient_GetEventsCompleted;
            _eventLogServiceClient.GetEventsCompleted += _eventLogServiceClient_GetEventsCompleted;
            _eventLogServiceClient.GetEventsAsync();
        }

        private void _eventLogServiceClient_GetEventsCompleted(object sender, GetEventsCompletedEventArgs e)
        {
            if (e.Result.Count == 0)
            {
                MessageBox.Show("NoDataFound");
            }
            else
            { 
               var  page = new PagedCollectionView(e.Result);
               RadDataPager1.Source = page;
               RadDataPager1.ItemCount = page.TotalItemCount;
               ErrorGrid.ItemsSource = page;
            }
        }

        private void BtnClearEvents_Click(object sender, RoutedEventArgs e)
        {
            _eventLogServiceClient = new EventLogServiceClient();
            _eventLogServiceClient.DeleteEventsCompleted -= _eventLogServiceClient_DeleteEventsCompleted;
           _eventLogServiceClient.DeleteEventsCompleted +=_eventLogServiceClient_DeleteEventsCompleted;
            _eventLogServiceClient.DeleteEventsAsync("");
        }

        private void _eventLogServiceClient_DeleteEventsCompleted(object sender, DeleteEventsCompletedEventArgs e)
        {
            MessageBox.Show(e.Result.ToString());
        }
    }
}
