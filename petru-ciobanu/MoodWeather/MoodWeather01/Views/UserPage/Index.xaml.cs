using System;
using System.Windows.Controls;

namespace MoodWeather.Views.UserPage
{
    public partial class Index : Page
    {
        public Index()
        {
            InitializeComponent();
            Title = "Weather Configuration";
            WebContext.Current.Authentication.LoggedOut +=
                (se, ev) => NavigationService.Navigate(new Uri("/Index", UriKind.Relative));
        }
    }
}