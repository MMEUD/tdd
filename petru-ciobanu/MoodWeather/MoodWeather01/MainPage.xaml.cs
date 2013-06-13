using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using MoodWeather.LoginUI;

namespace MoodWeather
{
    /// <summary>
    ///     <see cref="UserControl" /> class providing the main UI for the application.
    /// </summary>
    public partial class MainPage : UserControl
    {
        /// <summary>
        ///     Creates a new <see cref="MainPage" /> instance.
        /// </summary>
        public MainPage()
        {
            InitializeComponent();
            loginContainer.Child = new LoginStatus();


            WebContext.Current.Authentication.LoggedOut += (se, ev) =>
                {
                    UserSection.Visibility = Visibility.Collapsed;
                    AdminSection.Visibility = Visibility.Collapsed;
                };

            WebContext.Current.Authentication.LoggedIn += (se, ev) =>
                {
                    UserSection.Visibility = Visibility.Visible;
                    AdminSection.Visibility = Visibility.Visible;
                };
        }

        /// <summary>
        ///     After the Frame navigates, ensure the <see cref="HyperlinkButton" /> representing the current page is selected
        /// </summary>
        private void ContentFrame_Navigated(object sender, NavigationEventArgs e)
        {
            foreach (UIElement child in LinksStackPanel.Children)
            {
                var hb = child as HyperlinkButton;
                if (hb != null && hb.NavigateUri != null)
                {
                    if (hb.NavigateUri.ToString().Equals(e.Uri.ToString()))
                    {
                        VisualStateManager.GoToState(hb, "ActiveLink", true);
                    }
                    else
                    {
                        VisualStateManager.GoToState(hb, "InactiveLink", true);
                    }
                }
            }
        }

        /// <summary>
        ///     If an error occurs during navigation, show an error window
        /// </summary>
        private void ContentFrame_NavigationFailed(object sender, NavigationFailedEventArgs e)
        {
            e.Handled = true;
            ErrorWindow.CreateNew(e.Exception);
        }


        //LOAD PAGE 
        public void loadPages(string pages)
        {
            ContentFrame.Navigate(new Uri(pages, UriKind.Relative));
        }
    }
}