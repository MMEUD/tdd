using System.Windows;
using System.Windows.Controls;
using MoodWeather.Views.Login;

namespace MoodWeather.Views.Controls
{
    public partial class UserManagementControl : UserControl
    {
        public UserManagementControl()
        {
            InitializeComponent();
        }

        private void BtnSubmit_Click(object sender, RoutedEventArgs e)
        {
            // aspnet_MembershipDomainDataSource.SubmitChanges();
        }

        private void aspnet_MembershipDomainDataSource_LoadedData(object sender, LoadedDataEventArgs e)
        {
            if (e.HasError)
            {
                MessageBox.Show(e.Error.ToString(), "Load Error", MessageBoxButton.OK);
                e.MarkErrorAsHandled();
            }
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            var loginWindow = new LoginRegistrationWindow();
            loginWindow.loginForm.registerNow.Visibility = Visibility.Visible;
            loginWindow.Show();
        }
    }
}