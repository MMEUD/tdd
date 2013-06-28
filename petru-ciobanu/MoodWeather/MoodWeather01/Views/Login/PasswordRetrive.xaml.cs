using System.Windows;
using System.Windows.Controls;

namespace MoodWeather.Views.Login
{
    public partial class PasswordRetrive : ChildWindow
    {
        public PasswordRetrive()
        {
            InitializeComponent();
        }

        private void OKButton_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
        }

        private void CancelButton_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = false;
        }
    }
}