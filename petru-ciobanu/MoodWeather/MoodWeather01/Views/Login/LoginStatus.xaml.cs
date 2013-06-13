using System.ServiceModel.DomainServices.Client.ApplicationServices;
using System.Windows;
using System.Windows.Controls;
using MoodWeather.Views.Login;

namespace MoodWeather.LoginUI
{
    /// <summary>
    ///     <see cref="UserControl" /> class that shows the current login status and allows login and logout.
    /// </summary>
    public partial class LoginStatus : UserControl
    {
        private readonly AuthenticationService authService = WebContext.Current.Authentication;

        /// <summary>
        ///     Creates a new <see cref="LoginStatus" /> instance.
        /// </summary>
        public LoginStatus()
        {
            InitializeComponent();

            welcomeText.SetBinding(TextBlock.TextProperty,
                                   WebContext.Current.CreateOneWayBinding("User.DisplayName",
                                                                          new StringFormatValueConverter(
                                                                              ApplicationStrings.WelcomeMessage)));
            authService.LoggedIn += Authentication_LoggedIn;
            authService.LoggedOut += Authentication_LoggedOut;
            UpdateLoginState();
        }

        private void LoginButton_Click(object sender, RoutedEventArgs e)
        {
            var loginWindow = new LoginRegistrationWindow();
            loginWindow.Show();
        }

        private void LogoutButton_Click(object sender, RoutedEventArgs e)
        {
            authService.Logout(logoutOperation =>
                {
                    if (logoutOperation.HasError)
                    {
                        ErrorWindow.CreateNew(logoutOperation.Error);
                        logoutOperation.MarkErrorAsHandled();
                    }
                }, /* userState */ null);
        }

        private void Authentication_LoggedIn(object sender, AuthenticationEventArgs e)
        {
            UpdateLoginState();
        }

        private void Authentication_LoggedOut(object sender, AuthenticationEventArgs e)
        {
            UpdateLoginState();
        }

        private void UpdateLoginState()
        {
            if (WebContext.Current.User.IsAuthenticated)
            {
                VisualStateManager.GoToState(this,
                                             (WebContext.Current.Authentication is WindowsAuthentication)
                                                 ? "windowsAuth"
                                                 : "loggedIn", true);
            }
            else
            {
                VisualStateManager.GoToState(this, "loggedOut", true);
            }
        }
    }
}