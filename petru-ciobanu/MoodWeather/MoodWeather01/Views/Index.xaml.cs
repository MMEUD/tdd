using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using MoodWeather.Views.Controls;

namespace MoodWeather.Views
{
    public partial class Index : Page
    {
        public Index()
        {
            InitializeComponent();
            LoadControls();
            WebContext.Current.Authentication.LoggedOut += (se, ev) =>
                {
                    try
                    {
                        NavigationService.Navigate(new Uri("/Index", UriKind.Relative));
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine(e);
                    }
                };
        }


        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
        }


        public void LoadControls()
        {
            try
            {
                var weatherInfo = new WeatherInfo {Margin = new Thickness(50, 150, 0, 0)};
                ContainerBase.Children.Add(weatherInfo);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
    }
}