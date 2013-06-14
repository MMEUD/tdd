using System.ServiceModel.DomainServices.Client.ApplicationServices;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using MoodWeather.Views.Login;

namespace MoodWeather.Views
{
    public partial class LoginPage : Page
    {
        public LoginPage()
        {
            InitializeComponent();
        }

        // Executes when the user navigates to this page.
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            WebContext.Current.Authentication.LoggedIn += Authentication_LoggedIn;
        }

        protected override void OnNavigatedFrom(NavigationEventArgs e)
        {
            WebContext.Current.Authentication.LoggedIn -= Authentication_LoggedIn;
        }

        private void Authentication_LoggedIn(object sender, AuthenticationEventArgs e)
        {
            NavigationService.Refresh();
        }

        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            var loginWindow = new LoginRegistrationWindow();
            loginWindow.Show();
        }
    }
}