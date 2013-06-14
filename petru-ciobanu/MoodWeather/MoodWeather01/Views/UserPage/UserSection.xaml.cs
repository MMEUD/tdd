using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using MoodWeather.Views.Login;

namespace MoodWeather.Views.UserPage
{
    public partial class UserSection : Page
    {
        private LoginRegistrationWindow parentWindow;

        public UserSection()
        {
            InitializeComponent();
            WebContext.Current.Authentication.LoggedOut +=
                (se, ev) => { NavigationService.Navigate(new Uri("/Index", UriKind.Relative)); };
        }

        // Executes when the user navigates to this page.
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
        }

        /// <summary>
        ///     Sets the parent window for the current <see cref="LoginForm" />.
        /// </summary>
        /// <param name="window">The window to use as the parent.</param>
        public void SetParentWindow(LoginRegistrationWindow window)
        {
            parentWindow = window;
        }

        private void hyperlinkButton1_Click(object sender, RoutedEventArgs e)
        {
            parentWindow.NavigateToRegistration();
        }
    }
}